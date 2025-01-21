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
        fetch('/product/category/graphicCardproducts/graphicCardCategoryFilter', {
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
	        productList.innerHTML = products.map(graphicCard => `
	            <div class="product-item">
	                <div class="product-image">
	                    <img src="/images/product/graphicCard/gc${graphicCard.graphicCardId}.png" alt="Product Image" style="width:120px; height:120px;">
	                </div>
	                <div class="product-info">
	                    <a href="/graphicCardproducts/${graphicCard.graphicCardId}">
	                        <h2>${graphicCard.productName}</h2>
	                    </a>
	                    <p>제조사: ${graphicCard.manufacturer}, 침셋 제조사: ${graphicCard.chipsetManufacturer}, 칩셋: ${graphicCard.chipset}, VGA 길이: ${graphicCard.vgaLength}mm</p>
	                    <p>출시일: ${graphicCard.releaseDate}</p>
	                </div>
	                <div class="product-price">
	                    <p>${graphicCard.formattedPrice}</p>
	                    <button class="add-to-cart-btn" data-case-id="${graphicCard.graphicCardId}">장바구니</button>
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
								const currentUrl = window.location.href; //현재 URL http://localhost:8080/coolerproducts/3
								const urlParts = currentUrl.split('/');
								const categoryWithProducts = urlParts[3];  //coolerproducts 부분 추출
								const category = categoryWithProducts.replace('products', ''); //products를 제거
											
								const categoryNames = ["cpu", "ssd", "cooler", "graphicCard", "hdd", "power", 'memory', "mainboard", "case"];
											
								// 카테고리의 번호(인덱스)를 찾기
								const categoryIndex = categoryNames.indexOf(category);
											
								const partsId = button.getAttribute('data-case-id');
								const productId = (5 * categoryIndex) + parseInt(partsId, 10);
								
						        const isLoggedIn = document.body.getAttribute('data-is-logged-in') === 'true';
								const quantity = 1; // 수량을 1로 고정
						        if (!isLoggedIn) {
						            const confirmLogin = confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?");
						            if (confirmLogin) {
						                window.location.href = '/loginform';
						            }
						         } else {
						            fetch(`/cart/insert/${productId}?quantity=${quantity}&productType=${category}`, {
						               method: 'POST',
						               headers: { 'Content-Type': 'application/json' },
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
						         }
						    });
						 });
					}

    // 초기 요청 실행
    sendFilterRequest();
});
