document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.tab-button');
    const filterForm = document.getElementById('filter-form');

    // 초기 정렬 조건
    let currentSort = 'newest';

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // 모든 버튼의 활성화 상태 초기화
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // 정렬 조건 가져오기
            currentSort = tab.getAttribute('data-sort');

            // 필터링 및 정렬 요청 보내기
            sendFilterRequest();
        });
    });

    // 필터링 요청 보내기
    filterForm.addEventListener('change', () => {
        sendFilterRequest();
    });

    function sendFilterRequest() {
        // 폼 데이터를 가져옴
		const formData = new FormData(filterForm);
		const filterData = {};
	
		
		// FormData를 JSON 객체로 변환
		formData.forEach((value, key) => {
		   if (!filterData[key]) {
		          filterData[key] = [];
		        }
		        filterData[key].push(value);
		});
		
        // 정렬 조건 추가
        filterData.sort = [currentSort];

        // 서버로 요청
        fetch('/product/category/cpuproducts/cpuCategoryFilter', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(filterData)
        })
            .then(response => response.json())
            .then(data => {
                // 제품 목록 업데이트
                const productList = document.querySelector('.product-list');
                productList.innerHTML = data.map(cpu => `
                    <div class="product-item">
                        <div class="product-image">
                            <img src="/images/product/cpu/cpu${cpu.cpuId}.png" alt="Product Image" style="width:120px; height:120px;">
                        </div>
                        <div class="product-info">
							<a href="/cpuproducts/${cpu.cpuId}">
								<h2>${cpu.productName}</h2>
							</a>
                            	<p>제조사: ${cpu.manufacturer}, CPU 종류: ${cpu.intelType || cpu.amdType}, 소켓: ${cpu.socket}, 코어: ${cpu.coreTypes}, 메모리: ${cpu.supportedMemoryStandard}</p>
                            	<p>출시일: ${cpu.releaseDate}</p>
                        </div>
                        <div class="product-price">
                            <p>${cpu.formattedPrice}</p>
                            <button type="button">장바구니</button>
                        </div>
                    </div>
                `).join('');
            });
    }

    // 초기 요청
    sendFilterRequest();
});
