document.addEventListener('DOMContentLoaded', () => {
	const floatingBar = document.getElementById('floating-bar');
	const toggleButton = document.getElementById('toggle-floating-bar');
    const unitPrice = parseInt(document.getElementById('floating-price').textContent.replace(/[^\d]/g, ''), 10);
    const floatingQuantityInput = document.getElementById('floating-quantity');
    const floatingPriceElement = document.getElementById('floating-price');
	// 로그인 상태 확인
	const isLoggedIn = document.body.dataset.loggedIn === "true";
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
	
	// 버튼 클릭 이벤트 핸들러
	const handleButtonClick = (event, action) => {
		if (!isLoggedIn) {
	    // 로그인 필요 얼트 메시지
		    const userAction = confirm("로그인이 필요합니다. 로그인하시겠습니까?");
			if (userAction) {
			     window.location.href = '/loginform'; // 로그인 페이지로 이동
			 }
			 return; // 동작 중단
		}

		if (action === 'add-to-cart') {
			const userAction = confirm("장바구니에 담았습니다. 장바구니로 이동 하시겠습니까?");
		    if (userAction) {
			    window.location.href = '/cart'; // 장바구니 페이지로 이동
			 }
		} else if (action === 'buy-now') {
			  window.location.href = '/buynow'; // 구매 페이지로 이동
		}
	};

	// 이벤트 리스너 연결
	addToCartButton.addEventListener('click', (e) => handleButtonClick(e, 'add-to-cart'));
	buyNowButton.addEventListener('click', (e) => handleButtonClick(e, 'buy-now'));

    // 초기 가격 설정
    updateFloatingPrice();
});
