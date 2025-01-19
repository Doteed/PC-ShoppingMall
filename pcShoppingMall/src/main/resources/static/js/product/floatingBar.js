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

			    fetch(`/cart/insert/${productId}?quantity=${quantity}`, {
			        method: 'POST',
			        headers: { 'Content-Type': 'application/json' },
			        body: JSON.stringify({ productId, quantity }), // 데이터 전송
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

			    const productId = buyNowButton.getAttribute('data-case-id');
			    const quantity = parseInt(floatingQuantityInput.value, 10); // 수량 가져오기

			    fetch(`/cart/insert/${productId}?quantity=${quantity}`, {
			        method: 'POST',
			        headers: { 'Content-Type': 'application/json' },
			        body: JSON.stringify({ productId, quantity }), // 데이터 전송
			    })
			    .then(response => {
			        if (!response.ok) {
			            throw new Error('Failed to proceed to order');
			        }
			        window.location.href = '/my/cart'; // 성공 시 장바구니 페이지로 이동
			    })
			    .catch(error => console.error('Error processing order:', error));
			});


    // 초기 가격 설정
    updateFloatingPrice();
});
