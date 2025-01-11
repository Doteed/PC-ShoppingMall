document.addEventListener('DOMContentLoaded', () => {
	const floatingBar = document.getElementById('floating-bar');
	const toggleButton = document.getElementById('toggle-floating-bar');
    const unitPrice = parseInt(document.getElementById('floating-price').textContent.replace(/[^\d]/g, ''), 10);
    const floatingQuantityInput = document.getElementById('floating-quantity');
    const floatingPriceElement = document.getElementById('floating-price');
    
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
	
	// "장바구니" 버튼 클릭 이벤트
	document.getElementById('floating-add-to-cart').addEventListener('click', () => {
	        // 장바구니 페이지로 이동
		alert('장바구니에 추가되었습니다.');
	    //window.location.href = '/cart';
	 });

	 // "구매하기" 버튼 클릭 이벤트
	 document.getElementById('floating-buy-now').addEventListener('click', () => {
	        // 구매 페이지로 이동
		alert('구매하기 페이지로 이동합니다.');
	   // window.location.href = '/checkout';
	 });

    // 초기 가격 설정
    updateFloatingPrice();
});
