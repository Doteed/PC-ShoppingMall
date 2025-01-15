document.addEventListener('DOMContentLoaded', () => {
    const decreaseButton = document.getElementById('decrease-quantity');
    const increaseButton = document.getElementById('increase-quantity');
    const quantityInput = document.getElementById('quantity');
    const totalPriceElement = document.getElementById('total-price');
	const tabs = document.querySelectorAll('.tab');
	// 로그인 상태 확인
	const isLoggedIn = document.body.dataset.loggedIn === "true";
	// 장바구니 및 구매하기 버튼
	const addToCartButton = document.getElementById('add-to-cart');
	const buyNowButton = document.getElementById('buy-now');
	
    // 가격 정보 가져오기
    const unitPrice = parseInt(totalPriceElement.textContent.replace(/[^\d]/g, ''), 10);

    // 총 가격 업데이트 함수
    function updateTotalPrice() {
        const quantity = parseInt(quantityInput.value, 10) || 1;
        const totalPrice = unitPrice * quantity;
        totalPriceElement.textContent = `판매가: ${totalPrice.toLocaleString()}원`;
    }

    // 수량 감소 버튼 클릭 이벤트
    decreaseButton.addEventListener('click', () => {
        let currentQuantity = parseInt(quantityInput.value, 10) || 1;
        if (currentQuantity > 1) {
            quantityInput.value = currentQuantity - 1;
            updateTotalPrice();
        }
    });

    // 수량 증가 버튼 클릭 이벤트
    increaseButton.addEventListener('click', () => {
        let currentQuantity = parseInt(quantityInput.value, 10) || 1;
        quantityInput.value = currentQuantity + 1;
        updateTotalPrice();
    });

    // 수량 직접 입력 이벤트
    quantityInput.addEventListener('input', () => {
        let currentQuantity = parseInt(quantityInput.value, 10);
        if (!currentQuantity || currentQuantity < 1) {
            quantityInput.value = 1;
        }
        updateTotalPrice();
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
		         window.location.href = '/my/cart'; // 장바구니 페이지로 이동
		     }
		 } else if (action === 'buy-now') {
		     window.location.href = '/my/order'; // 구매 페이지로 이동
		 }
	};

	// 이벤트 리스너 연결
	addToCartButton.addEventListener('click', (e) => handleButtonClick(e, 'add-to-cart'));
	buyNowButton.addEventListener('click', (e) => handleButtonClick(e, 'buy-now'));
	
	// 탭 클릭 시 스크롤 이벤트
	    tabs.forEach(tab => {
	        tab.addEventListener('click', () => {
	            const targetId = tab.getAttribute('data-target'); // 클릭한 탭의 data-target 속성 값
	            const targetElement = document.querySelector(targetId);

	            if (targetElement) {
	                const offsetTop = targetElement.getBoundingClientRect().top + window.pageYOffset;

	                window.scrollTo({
	                    top: offsetTop - 20, // 탑에 여유 공간을 추가
	                    behavior: 'smooth'
	                });
	            }
	        });
	    });

    // 초기 총 가격 설정
    updateTotalPrice();
});
