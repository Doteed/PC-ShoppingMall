document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("placeOrderBtn").addEventListener("click", function() {
		const selectedCartIds = [];
		const selectedItems = document.querySelectorAll(".item-checkbox:checked");
		selectedItems.forEach((item) => {
			selectedCartIds.push(item.dataset.cartId);
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

		const popupWindow = window.open("", "orderPopup", "width=400,height=600");

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
            <td><input type="text" id="address" required /></td>
          </tr>
          <tr>
            <td>전화번호</td>
            <td><input type="text" id="phone" required /></td>
          </tr>
          <tr>
            <td colspan="2" style="text-align: center;">
              <button type="button" id="submitOrderBtn">결제 진행</button>
            </td>
          </tr>
        </tbody>
      </table>
    `;

		setTimeout(() => {
			const submitOrderBtn = popupWindow.document.getElementById(
				"submitOrderBtn"
			);

			submitOrderBtn.addEventListener("click", function() {
				const addressee = popupWindow.document.getElementById(
					"addressee"
				).value;
				const address = popupWindow.document.getElementById("address").value;
				const phone = popupWindow.document.getElementById("phone").value;
				const paymentMethod =
					popupWindow.document.getElementById("paymentMethod").value;

				const orderData = {
					cartIds: selectedCartIds,
					paymentMethod,
					addressee,
					address,
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

			// 서버에 결제 요청
			await requestTossPayment(orderData);
			popupWindow.close();
		} catch (error) {
			console.error("결제 처리 중 오류 발생:", error);
			alert("결제 처리 중 오류 발생: " + error.message);
			popupWindow.close();
		}
	}

	async function requestTossPayment(orderData) {
		try {
			console.log("orderData:", orderData);

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
					failUrl: "http://localhost:8080/payment/fail"
				}),
			});

			const responseData = await response.json();
			console.log("결제 응답 데이터 :", responseData);

			if (response.ok && responseData.status === "READY") {
				window.location.href = responseData.url;
				await insertOrderData(orderData);
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

	/*
	async function completeOrder(orderData) {
		try {
			const response = await fetch("order/insertFromCart", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					body: JSON.stringify(orderData),
				}),
			});

			if (!response.ok) {
				const errorData = await response.json();
				throw new Error(errorData.message || "주문 저장 실패");
			}

			const responseBody = await response.json();
			console.log("주문 저장 성공:", responseBody);
		} catch (error) {
			console.error("주문 저장 중 오류 발생:", error);
			throw error;
		}
	}*/

	async function confirmPayment(paymentKey, orderId, amount, orderData) {
		console.log("confirmPayment 호출 전 데이터:", { paymentKey, orderId, amount, orderData });
		try {
			const response = await fetch("/api/payment/confirm", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ paymentKey, orderId, amount }),
			});

			if (!response.ok) {
				throw new Error(`결제 확인 실패: ${await response.text()}`);
			}

			alert("결제가 성공적으로 처리되었습니다");

			await insertOrderData(orderData);
		} catch (error) {
			console.error("결제 확인 중 오류:", error);
			alert("결제 확인 중 오류가 발생했습니다");
		}
	}

	async function insertOrderData(orderData) {
		try {
			const response = await fetch("/order/insertFromCart", {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(orderData)
			});

			if (!response.ok) {
				throw new Error("주문 저장 실패");
			}

			console.log("주문 데이터 저장 성공");

			// 주문 저장 후 5초 후 my/cart로 이동
			setTimeout(() => {
				window.location.href = "/my/cart";
			}, 5000); // 5초 대기
		} catch (error) {
			console.error("주문 저장 중 오류 발생:", error);
			throw error;
		}
	}
});
