document.addEventListener('DOMContentLoaded', () => {
	const floatingBar = document.getElementById('floating-bar');
	const toggleButton = document.getElementById('toggle-floating-bar');
	const unitPrice = parseInt(document.getElementById('floating-price').textContent.replace(/[^\d]/g, ''), 10);
	const floatingQuantityInput = document.getElementById('floating-quantity');
	const floatingPriceElement = document.getElementById('floating-price');
	// 로그인 상태 확인
	const isLoggedIn = document.body.getAttribute('data-is-logged-in') === 'true';
	// 장바구니 및 구매하기 버튼
	const addToCartButton = document.getElementById('floating-add-to-cart');
	const buyNowButton = document.getElementById('floating-buy-now');

	// 가격 업데이트 함수
	function updateFloatingPrice() {
		const quantity = parseInt(floatingQuantityInput.value, 10) || 1;
		const totalPrice = unitPrice * quantity;
		floatingPriceElement.textContent = `가격: ${totalPrice.toLocaleString()}원`;
	}

	// `-` 버튼 클릭 이벤트
	document.getElementById('decrease-floating-quantity').addEventListener('click', () => {
		let currentQuantity = parseInt(floatingQuantityInput.value, 10) || 1;
		if (currentQuantity > 1) {
			floatingQuantityInput.value = currentQuantity - 1;
			updateFloatingPrice();
		}
	});

	// `+` 버튼 클릭 이벤트
	document.getElementById('increase-floating-quantity').addEventListener('click', () => {
		let currentQuantity = parseInt(floatingQuantityInput.value, 10) || 1;
		floatingQuantityInput.value = currentQuantity + 1;
		updateFloatingPrice();
	});

	// 수량 입력 이벤트
	floatingQuantityInput.addEventListener('input', () => {
		let currentQuantity = parseInt(floatingQuantityInput.value, 10);
		if (!currentQuantity || currentQuantity < 1) {
			floatingQuantityInput.value = 1;
		}
		updateFloatingPrice();
	});

	// 장바구니 버튼 실행
	addToCartButton.addEventListener('click', () => {
		if (!isLoggedIn) {
			const userAction = confirm("로그인이 필요합니다. 로그인하시겠습니까?");
			if (userAction) {
				window.location.href = '/loginform';
			}
			return;
		}

		const currentUrl = window.location.href; //현재 URL http://localhost:8080/coolerproducts/3
		const urlParts = currentUrl.split('/');
		const categoryWithProducts = urlParts[3];  //coolerproducts 부분 추출
		const category = categoryWithProducts.replace('products', ''); //products를 제거

		const categoryNames = ["cpu", "ssd", "cooler", "graphicCard", "hdd", "power", 'memory', "mainboard", "case"];

		// 카테고리의 번호(인덱스)를 찾기
		const categoryIndex = categoryNames.indexOf(category);

		const partsId = addToCartButton.getAttribute('data-case-id');
		const productId = (5 * categoryIndex) + parseInt(partsId, 10);

		const quantity = parseInt(floatingQuantityInput.value, 10); // 수량 가져오기

		fetch(`/cart/insert/${productId}?quantity=${quantity}&productType=${category}`, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Failed to add product to cart');
				}
				const userAction = confirm("장바구니에 담았습니다. 장바구니로 이동 하시겠습니까?");
				if (userAction) {
					window.location.href = '/my/cart';
				}
			})
			.catch(error => console.error('Error adding to cart:', error));
	});

	// 구매하기 버튼 실행
	buyNowButton.addEventListener('click', () => {
		if (!isLoggedIn) {
			const userAction = confirm("로그인이 필요합니다. 로그인하시겠습니까?");
			if (userAction) {
				window.location.href = '/loginform';
			}
			return;
		}

		const currentUrl = window.location.href;
		const urlParts = currentUrl.split('/');
		const productIdFromUrl = parseInt(urlParts[urlParts.length - 1]); // URL에서 productId 추출

		const categoryWithProducts = urlParts[3];  //coolerproducts 추출
		const category = categoryWithProducts.replace('products', ''); //cooler

		const categoryNames = ["cpu", "ssd", "cooler", "graphicCard", "hdd", "power", 'memory', "mainboard", "case"];

		// 카테고리의 번호(인덱스)를 찾기
		const categoryIndex = categoryNames.indexOf(category);

		const productId = (5 * categoryIndex) + productIdFromUrl;
		const quantities = parseInt(floatingQuantityInput.value, 10); // 수량 가져오기
		const totalPrice = unitPrice * quantities;
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

				const orderData = {
					productId,
					paymentMethod,
					addressee,
					address,
					detailAddress,
					phone,
					amount: totalPrice,
					quantity: quantities,
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


	// 초기 가격 설정
	updateFloatingPrice();
});
