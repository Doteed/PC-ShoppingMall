// 날짜 포멧팅
function formatDate(timestamp) {
    const date = new Date(timestamp);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

document.addEventListener('DOMContentLoaded', function() {
    // QnA & Review
    const entityTitles = document.querySelectorAll('.my-title');
    entityTitles.forEach((title) => {
        title.addEventListener('click', function() {
			
            const entityType = this.getAttribute('data-type');
            const entityId = this.getAttribute('data-id');

            if (entityType === 'qna' || entityType === 'review') {
                fetch(`/${entityType}/detail/${entityId}?`)
                    .then(response => response.json())
                    .then(data => {
                        const entityDetailContainer = document.getElementById('entityDetailContainer');

                        let contentHTML = `
                            <table class="table table-bordered">
                                <tbody>
                                    <tr>
                                        <td><strong>제목</strong></td>
                                        <td>${data.title}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>작성일</strong></td>
                                    `;
                        if (entityType === 'qna') {
                            contentHTML += `
								<td>${formatDate(data.createdDate)}</td>
							    </tr>
                                <tr>
                                    <td><strong>내용</strong></td>
                                    <td>${data.content}</td>
                                </tr>`;
                            if (data.answer !== null && data.answer !== undefined) {
                                contentHTML += `
                                <tr>
									<td><strong>답변</strong></td>
								    <td>${data.answer}</td>
                                </tr>`;
                            } else {
                                contentHTML += `
								<tr>
									<td><strong>답변</strong></td>
									<td>답변이 없습니다.</td></tr>
								</tr>`;
                            }
                        } else if (entityType === 'review') {
                            contentHTML += `
								<td>${formatDate(data.date)}</td>
								</tr>
                                <tr>
                                    <td><strong>평점</strong></td>
                                    <td>${data.rating}</td>
                                </tr>
                                <tr>
                                    <td><strong>내용</strong></td>
                                    <td>${data.content}</td>
                                </tr>`;
                        }

                        contentHTML += `</tbody></table>`;
                        contentHTML += `
                            <button type="button" class="btn btn-primary" onclick="openEditPopup('${entityType}', ${entityId}, 'update')">수정</button>
                            <button type="button" class="btn btn-danger" onclick="deleteEntity('${entityType}', ${entityId})">삭제</button>`;

                        entityDetailContainer.innerHTML = contentHTML;
                        entityDetailContainer.style.display = 'block';
                    })
                    .catch(error => {
                        console.error(`${entityType} 상세 내용 가져오기 실패:`, error);
                    });
            }
        });
    });

    // Order 팝업
    let popupWindow = null;
    const orderDetailButtons = document.querySelectorAll('.order-detail-button');
    orderDetailButtons.forEach((button) => {
        button.addEventListener('click', function(event) {
            const orderId = event.target.getAttribute('data-order-id');

            if (popupWindow && !popupWindow.closed) {
                popupWindow.close();
            }

            fetch(`/order/detail/${orderId}?`)
                .then(response => response.json())
                .then(data => {
                    popupWindow = window.open('', 'OrderDetail', 'width=600,height=400');

                    const link = popupWindow.document.createElement('link');
                    link.rel = 'stylesheet';
                    link.href = 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css';
                    popupWindow.document.head.appendChild(link);

                    const popupContent = `
                        <h3>주문 상세</h3>
                        <table class="table table-bordered">
                            <tbody>
                                <tr>
                                    <td>상품명</td>
                                    <td>${data.productName}</td>
                                </tr>
                                <tr>
                                    <td>주문일</td>
                                    <td>${formatDate(data.orderDate)}</td>
                                </tr>
                                <tr>
                                    <td>주문 상태</td>
                                    <td>${data.deliveryStatus}</td>
                                </tr>
                                <tr>
                                    <td>총 금액</td>
                                    <td>${data.totalPrice} 원</td>
                                </tr>
                            </tbody>
                        </table>
                        <h4>배송 정보</h4>
                        <form id="addressForm">
                            <div class="mb-3">
                                <label for="addressee" class="form-label">수령인</label>
                                <input type="text" class="form-control" id="addressee" value="${data.addressee || ''}" required>
                            </div>
                            <div class="mb-3">
                                <label for="phone" class="form-label">연락처</label>
                                <input type="text" class="form-control" id="phone" value="${data.phone || ''}" required placeholder="010-1234-5678" maxlength="13">
                            </div>
                            <div class="mb-3">
                                <label for="address" class="form-label">주소</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="address" value="${data.address || ''}" readonly required>
                                    <button type="button" class="btn btn-secondary" id="searchAddressBtn">주소 검색</button>
                                </div>
                            </div>
							<div class="mb-3">
							    <label for="addressDetail" class="form-label">상세 주소</label>
							    <input type="text" class="form-control" id="addressDetail" value="${data.addressDetail || ''}" required>
							</div>
                            <button type="button" class="btn btn-primary" id="updateBtn">수정</button>
                            <button type="button" class="btn btn-danger" id="cancelBtn">주문 취소</button>
                        </form>
                    `;

                    popupWindow.document.body.innerHTML = popupContent;

                    // 다음 주소 API
                    const searchAddressBtn = popupWindow.document.getElementById('searchAddressBtn');
                    searchAddressBtn.addEventListener('click', () => {
                        new daum.Postcode({
                            oncomplete: function(data) {
                                const fullAddress = data.address;
                                popupWindow.document.getElementById('address').value = fullAddress;
                            }
                        }).open();
                    });

                    // 전화번호 포맷팅
                    const phoneInput = popupWindow.document.getElementById('phone');
                    phoneInput.addEventListener('input', (event) => {
                        const value = event.target.value.replace(/[^0-9]/g, '');
                        let formattedValue = '';

                        if (value.length <= 3) {
                            formattedValue = value;
                        } else if (value.length <= 7) {
                            formattedValue = `${value.slice(0, 3)}-${value.slice(3)}`;
                        } else {
                            formattedValue = `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
                        }

                        event.target.value = formattedValue;
                    });

                    // 저장
                    const updateBtn = popupWindow.document.getElementById('updateBtn');
                    updateBtn.addEventListener('click', () => {
                        const deliveryId = data.deliveryId;
                        const addressee = popupWindow.document.getElementById('addressee').value;
                        const phone = popupWindow.document.getElementById('phone').value;
                        const address = popupWindow.document.getElementById('address').value;

                        fetch(`/order/update`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({
                                deliveryId: deliveryId,
                                userId: userId,
                                addressee,
                                phone,
                                address,
                            }),
                        })
                            .then((response) => {
                                if (response.ok) {
                                    alert('정보가 성공적으로 저장되었습니다!');
                                    popupWindow.close();
                                } else {
                                    throw new Error('저장 실패');
                                }
                            })
                            .catch((error) => {
                                console.error('정보 저장 실패:', error);
                                alert('정보 저장에 실패했습니다.');
                            });
                    });

                    // 주문 취소
                    const cancelBtn = popupWindow.document.getElementById('cancelBtn');
                    cancelBtn.addEventListener('click', () => {
                        const confirmCancel = confirm('정말 주문을 취소하시겠습니까?');
                        if (!confirmCancel) return;

                         fetch(`/order/cancle?orderId=${data.orderId}`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                        })
                            .then((response) => {
                                if (response.ok) {
                                    alert('주문이 성공적으로 취소되었습니다!');
                                    popupWindow.close();
									location.reload();
                                } else {
                                    throw new Error('주문 취소 실패');
                                }
                            })
                            .catch((error) => {
                                console.error('주문 취소 실패:', error);
                                alert('주문 취소에 실패했습니다.');
                            });
                    });
                })
                .catch((error) => {
                    console.error('주문 상세 내용 가져오기 실패:', error);
                });
        });
    });
});

//팝업 창 닫기
function closeWindow() {
	window.close();
}

//tab
const key = "${param.key}";

const writtenTab = document.getElementById("written-myreviews-tab");
const writeTab = document.getElementById("write-myreviews-tab");
const writtenContent = document.getElementById("written-myreviews");
const writeContent = document.getElementById("write-myreviews");
const entityDetailContainer = document.getElementById("entityDetailContainer");

if (key === "write-myreviews") {
	//작성할 탭
	console.log("123");
	writtenTab.classList.remove("active");
	writeTab.classList.add("active");

	writtenContent.classList.remove("show", "active");
	writeContent.classList.add("show", "active");

	writtenContent.style.display = "none";
	writeContent.style.display = "block";
	
	entityDetailContainer.style.setProperty('display', 'none', 'important');
} else if (key === "written-myreviews") {
	//작성된 탭
	
	writeTab.classList.remove("active");
	writtenTab.classList.add("active");

	writeContent.classList.remove("show", "active");
	writtenContent.classList.add("show", "active");
	
	writeContent.style.display = "none";
	writtenContent.style.display = "block";
	
	entityDetailContainer.style.display = "block";
}