document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.tab-button');
    const filterForm = document.getElementById('filter-form');
    const productList = document.querySelector('.product-list');

    // 초기 정렬 조건
    let currentSort = 'newest';

    // 탭 클릭 이벤트 처리
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // 모든 버튼의 활성화 상태 초기화
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // 정렬 조건 업데이트
            currentSort = tab.getAttribute('data-sort');

            // 필터링 및 정렬 요청 보내기
            sendFilterRequest();
        });
    });

    // 필터 변경 이벤트 처리
    filterForm.addEventListener('change', () => {
        sendFilterRequest();
    });

    // 필터 및 정렬 조건을 서버로 보내는 함수
    function sendFilterRequest() {
        // 폼 데이터를 가져와 JSON 객체로 변환
        const formData = new FormData(filterForm);
        const filterData = {};

        formData.forEach((value, key) => {
            if (!filterData[key]) {
                filterData[key] = [];
            }
            filterData[key].push(value);
        });

        // 정렬 조건 추가
        filterData.sort = [currentSort];
		console.log('Sending filter data:', filterData); // 디버깅 로그
        // 서버로 POST 요청 전송
        fetch('/product/category/caseproducts/caseCategoryFilter', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(filterData),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
			.then(data => updateProductList(data))
            .catch(error => console.error('Error fetching filtered products:', error));
    }

    // 제품 목록을 업데이트하는 함수
	function updateProductList(products) {
	        productList.innerHTML = products.map(Case => `
	            <div class="product-item">
	                <div class="product-image">
	                    <img src="/images/product/case/case${Case.caseId}.png" alt="Product Image" style="width:120px; height:120px;">
	                </div>
	                <div class="product-info">
	                    <a href="/caseproducts/${Case.caseId}">
	                        <h2>${Case.productName}</h2>
	                    </a>
	                    <p>제조사: ${Case.manufacturer}, 제품 분류: ${Case.productClassification}, 케이스 크기: ${Case.caseSize}, 지원 파워 규격: ${Case.powerSupplyStandard}, 지원 보드 규격: ${Case.supportedBoardStandard}, 13.3.cm베이: ${Case.bay13_3CM}, VGA길이: ${Case.vgaLength} 8.9cm베이: ${Case.bay8_9CM}</p>
	                    <p>출시일: ${Case.releaseDate}</p>
	                </div>
	                <div class="product-price">
	                    <p>${Case.formattedPrice}</p>
	                    <button class="add-to-cart-btn" data-case-id="${Case.caseId}">장바구니</button>
	                </div>
	            </div>
	        `).join('');
			// 장바구니 버튼 이벤트 추가
			addCartButtonEvents();
	    }
		// 장바구니 버튼 이벤트 처리 함수
			function addCartButtonEvents() {
				const cartButtons = document.querySelectorAll('.add-to-cart-btn');
				cartButtons.forEach(button => {
				    button.addEventListener('click', (event) => {
						event.preventDefault(); // 기본 폼 제출 동작 방지
						
				        const productId = button.getAttribute('data-case-id');
				        const isLoggedIn = document.body.getAttribute('data-is-logged-in') === 'true';
						const quantity = 1; // 수량을 1로 고정
						
						if (!productId) {
						    console.error('Product ID is missing');
						    return;
						}
									
				        if (!isLoggedIn) {
				            const confirmLogin = confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?");
				            if (confirmLogin) {
				                window.location.href = '/loginform';
				            }
							return;
				         }
				            fetch(`/cart/insert/${productId}?quantity=${quantity}`, {
				               method: 'POST',
				               headers: { 'Content-Type': 'application/json' },
				               body: JSON.stringify({ productId, quantity }),
				          })
				               .then(response => {
				                   if (!response.ok) {
				                       throw new Error('Failed to add to cart');
				                   }
				                   const confirmCart = confirm("장바구니에 담겼습니다. 장바구니 페이지로 이동하시겠습니까?");
				                   if (confirmCart) {
				                       window.location.href = '/my/cart';
				                   }
				                })
				                .catch(error => console.error('Error adding to cart:', error));
				    });
				 });
			}

    // 초기 요청 실행
    sendFilterRequest();
});
