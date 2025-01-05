document.addEventListener('DOMContentLoaded', () => {
    const decreaseButton = document.getElementById('decrease-quantity');
    const increaseButton = document.getElementById('increase-quantity');
    const quantityInput = document.getElementById('quantity');
    const totalPriceElement = document.getElementById('total-price');
	const tabs = document.querySelectorAll('.tab');

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

    // "장바구니" 버튼 클릭 이벤트
    document.getElementById('add-to-cart').addEventListener('click', () => {
        alert('장바구니에 추가되었습니다.');
        // 장바구니 추가 로직을 여기에 구현
    });

    // "구매하기" 버튼 클릭 이벤트
    document.getElementById('buy-now').addEventListener('click', () => {
        alert('구매 페이지로 이동합니다.');
        // 구매하기 로직을 여기에 구현
        //window.location.href = '/purchase'; // 구매 페이지로 이동 (예시 URL)
    });
	
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
