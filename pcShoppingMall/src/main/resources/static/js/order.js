document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("placeOrderBtn").addEventListener("click", function() {
		const selectedCartIds = [];
		const selectedProductDetails = []; // 제품 정보 저장
		const selectedItems = document.querySelectorAll(".item-checkbox:checked");
		selectedItems.forEach((item) => {
			selectedCartIds.push(item.dataset.cartId);
			const row = item.closest("tr");
			const productName = row.querySelector("td:nth-child(3)").textContent;
			const quantity = row.querySelector("td:nth-child(5)").textContent.trim();
			selectedProductDetails.push(`${productName} (${quantity}개)`);
		});

		if (selectedCartIds.length === 0) {
			alert("선택된 항목이 없습니다.");
			return;
		}

		const totalPriceText = document.getElementById("totalPriceText");
		const totalPrice = parseFloat(
			totalPriceText.textContent.replace(/[^\d.-]/g, "")
		);

		if (isNaN(totalPrice) || totalPrice <= 0) {
			alert("유효하지 않은 금액입니다.");
			return;
		}

		const popupWindow = window.open("", "orderPopup", "width=600,height=600");

		popupWindow.document.body.innerHTML = `
      <h3>주문</h3>
      <table class="table">
        <tbody>
          <tr>
            <td>결제 방식</td>
            <td>
              <select id="paymentMethod">
                <option value="카드">신용카드</option>
                <option value="가상계좌">무통장입금</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>수령인</td>
            <td><input type="text" id="addressee" required /></td>
          </tr>
          <tr>
		  	<td>주소</td>
		  	<td colspan="3">
		  		<div class="input-group">
		  			<input type="text" class="form-control" id="address" readonly required>
		  			<button type="button" class="btn btn-secondary" id="searchAddressBtn">주소 검색</button>
		  		</div>
		  	</td>
		  </tr>
		  <tr>
		  	<td>상세주소</td>
		  	<td colspan="3"><input type="text" class="form-control" id="detailAddress" required></td>
		  </tr>
          <tr>
            <td>전화번호</td>
            <td><input type="text" id="phone" required /></td>
          </tr>
		  <tr id="orderDetailsRow"></tr>
          <tr>
            <td colspan="2" style="text-align: center;">
              <button type="button" id="submitOrderBtn">결제 진행</button>
            </td>
          </tr>
        </tbody>
      </table>
    `;

		const detailsRow = popupWindow.document.getElementById("orderDetailsRow");
		if (detailsRow) {
			detailsRow.innerHTML = `
	       <td colspan="2">
	         <h4>주문 내역</h4>
	         <ul>
	           ${selectedProductDetails.map((detail) => `<li>${detail}</li>`).join("")}
	         </ul>
	         <p>총 금액: <strong>${totalPrice.toLocaleString()} 원</strong></p>
	       </td>
	     `;
		} else {
			console.error("주문 내역을 표시할 <tr> 요소를 찾을 수 없습니다.");
		}

		//다음 주소 API
		const searchAddressBtn = popupWindow.document.getElementById('searchAddressBtn');
		searchAddressBtn.addEventListener('click', () => {
			new daum.Postcode({
				oncomplete: function(data) {
					const fullAddress = data.address;
					popupWindow.document.getElementById('address').value = fullAddress;
				}
			}).open();
		});

		setTimeout(() => {
			const submitOrderBtn = popupWindow.document.getElementById(
				"submitOrderBtn"
			);

			submitOrderBtn.addEventListener("click", function() {
				const addressee = popupWindow.document.getElementById(
					"addressee"
				).value;
				const address = popupWindow.document.getElementById("address").value;
				const detailAddress = popupWindow.document.getElementById("detailAddress").value;
				const phone = popupWindow.document.getElementById("phone").value;
				const paymentMethod =
					popupWindow.document.getElementById("paymentMethod").value;

				const flag = document.getElementById("placeOrderBtn").dataset.flag;
				const orderData = {
					cartIds: selectedCartIds,
					paymentMethod,
					addressee,
					address,
					detailAddress,
					phone,
					amount: totalPrice,
				};

				handlePayment(orderData, popupWindow);
			});
		}, 100);
	});

	async function handlePayment(orderData, popupWindow) {
		try {
			console.log("결제 요청 시작:", orderData);

			const responseData = await requestTossPayment(orderData);

			if (responseData && responseData.status === "READY") {
				window.location.href = responseData.url;
				popupWindow.close();

				window.addEventListener('load', async () => {
					try {
						await confirmPayment(responseData.paymentKey, responseData.orderId, orderData.amount);
						alert("주문이 완료되었습니다.");
					} catch (error) {
						console.error("주문 완료 처리 중 오류 발생:", error);
					}
				});
			}

		} catch (error) {
			console.error("결제 처리 중 오류 발생:", error);
			alert("결제 처리 중 오류 발생: " + error.message);
			popupWindow.close();
		}
	}

	async function requestTossPayment(orderData) {
		try {
			console.log("orderData:", orderData);
			// orderData를 세션 스토리지에 저장
			sessionStorage.setItem("orderData", JSON.stringify(orderData));

			// 서버로 결제 요청
			const response = await fetch("/api/payment/request", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					method: orderData.paymentMethod === "카드" ? "CARD" : "VIRTUAL_ACCOUNT",
					amount: orderData.amount,
					orderId: `order-${Date.now()}`,
					orderName: "장바구니 결제",
					customerName: orderData.addressee,
					successUrl: "http://localhost:8080/payment/success",
					failUrl: "http://localhost:8080/payment/fail",
				}),
			});

			const responseData = await response.json();
			console.log("결제 응답 데이터 :", responseData);

			if (response.ok && responseData.status === "READY") {
				return responseData;
			} else {
				console.error("결제 실패:", responseData);
				throw new Error(responseData.message || "결제 요청 실패");
			}
		} catch (error) {
			console.error("결제 요청 중 오류 발생:", error);
			alert("결제 요청 중 오류가 발생했습니다.");
			throw error;
		}
	}
});
