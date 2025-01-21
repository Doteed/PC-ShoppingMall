// 날짜 포맷팅
export function formatDate(timestamp) {
	const date = new Date(timestamp);
	return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

// 번호 포맷팅
export function formatPhone(phone) {
	// 숫자만 남기기
	const digits = phone.replace(/[^0-9]/g, '');
	let formattedPhone = '';

	if (digits.length <= 3) {
		formattedPhone = digits;
	} else if (digits.length <= 7) {
		formattedPhone = `${digits.slice(0, 3)}-${digits.slice(3)}`;
	} else {
		formattedPhone = `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7, 11)}`;
	}

	return formattedPhone;
}

/*export function initializeAddressSearch(inputElementId) {
	new daum.Postcode({
		oncomplete: function(data) {
			const fullAddress = data.address;
			const inputElement = document.getElementById(inputElementId);
			if (inputElement) {
				inputElement.value = fullAddress;
			}
		}
	}).open();
}*/

document.addEventListener('DOMContentLoaded', function() {
	const link = document.createElement('link');
	link.rel = 'stylesheet';
	link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css';
	document.head.appendChild(link);
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
									<td><strong>비밀글</strong></td>
								    <td>
								    ${data.isSecret === 1 ? '<i class="fas fa-lock"></i> 비밀글' : '일반글'}
								</td>
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
									<td><strong>상품명</strong></td>
								    <td>${data.productName}</td>
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
	const orderDetailButtons = document.querySelectorAll('.detail-button');
	orderDetailButtons.forEach((button) => {
		button.addEventListener('click', function(event) {
			const orderId = event.target.getAttribute('data-order-id');

			if (popupWindow && !popupWindow.closed) {
				popupWindow.close();
			}

			fetch(`/order/detail/${orderId}?`)
				.then(response => response.json())
				.then(data => {
					popupWindow = window.open('', 'OrderDetail', 'width=1000,height=800');

					const link = popupWindow.document.createElement('link');
					link.rel = 'stylesheet';
					link.href = 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css';
					popupWindow.document.head.appendChild(link);

					const popupContent = `
                        <h3>주문 상세</h3>
                        <table class="table table-bordered">
                            <tbody>
                                <tr>
									<td>주문번호</td>
								    <td>${data.order.orderId}</td>
									<td>주문일</td>
									<td>${formatDate(data.order.orderDate)}</td>
                                </tr>
                            </tbody>
                        </table>
						<h4>상품정보</h4>
						   <table class="table table-bordered">
						       <thead>
						           <tr>
						               <th>상품명</th>
						               <th>금액</th>
						               <th>주문상태</th>
						           </tr>
						       </thead>
						       <tbody>
						           <tr>
						               <td>${data.order.productName}</td>
						               <td>${data.order.totalPrice} 원</td>
						               <td>${data.order.deliveryStatus}</td>
						           </tr>
						       </tbody>
						   </table>
						<h4>구매자 정보</h4>
						<table class="table table-bordered">
							<tbody>
						        <tr>
									<td>주문자</td>
									<td>${data.userId}</td>
									<td>연락처</td>
									<td>${formatPhone(data.phone)}</td>
						        </tr>
						    </tbody>
						</table>
						<form id="addressForm">
						       <table class="table table-bordered">
						           <tbody>
						               <tr>
						                   <td>수령인</td>
						                   <td><input type="text" class="form-control" id="addressee" value="${data.order.addressee || ''}" required></td>
						               </tr>
									   <tr>
									   	   <td>연락처</td>
						                   <td><input type="text" class="form-control" id="phone" value="${formatPhone(data.order.phone)}" required placeholder="010-1234-5678" maxlength="13"></td>
						               </tr>
						               <tr>
						                   <td>주소</td>
						                   <td colspan="3">
						                       <div class="input-group">
						                           <input type="text" class="form-control" id="address" value="${data.order.address || ''}" readonly required>
						                           <button type="button" class="btn btn-secondary" id="searchAddressBtn">주소 검색</button>
						                       </div>
						                   </td>
						               </tr>
						               <tr>
						                   <td>상세주소</td>
						                   <td colspan="3"><input type="text" class="form-control" id="detailAddress" value="${data.order.detailAddress || ''}" required></td>
						               </tr>
						           </tbody>
						       </table>
						       <div class="mt-3">
						           <button type="button" class="btn btn-primary" id="updateBtn">수정</button>
						           <button type="button" class="btn btn-danger" id="cancelBtn">주문 취소</button>
						       </div>
						   </form>
                    `;

					popupWindow.document.body.innerHTML = popupContent;

					const cancelBtn = popupWindow.document.getElementById('cancelBtn');
					const updateBtn = popupWindow.document.getElementById('updateBtn');

					if (data.order.deliveryStatus === '취소') {
						// 버튼 비활성화
						cancelBtn.disabled = true;
						cancelBtn.classList.add('disabled');
						updateBtn.disabled = true;
						updateBtn.classList.add('disabled');

						// 안내 메시지 추가
						const message = popupWindow.document.createElement('p');
						message.classList.add('text-danger');
						message.textContent = '취소된 주문은 수정하거나 취소할 수 없습니다.';
						popupWindow.document.body.appendChild(message);
					}

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

					// 저장
					updateBtn.addEventListener('click', () => {
						const deliveryId = data.order.deliveryId;
						const addressee = popupWindow.document.getElementById('addressee').value;
						const phone = popupWindow.document.getElementById('phone').value;
						const address = popupWindow.document.getElementById('address').value;
						const detailAddress = popupWindow.document.getElementById('detailAddress').value;

						fetch(`/order/update`, {
							method: 'PUT',
							headers: {
								'Content-Type': 'application/json',
							},
							body: JSON.stringify({
								deliveryId: deliveryId,
								addressee,
								phone,
								address,
								detailAddress,
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
					cancelBtn.addEventListener('click', () => {
						const confirmCancel = confirm('정말 주문을 취소하시겠습니까?');
						if (!confirmCancel) return;

						fetch(`/order/cancle?orderId=${data.order.orderId}`, {
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