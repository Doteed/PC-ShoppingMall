//수정 팝업
window.openEditPopup = function(entityType, entityId, mode) {
	const isUpdate = mode === 'update' ? true : false;
	const url = isUpdate ? `/${entityType}/detail/${entityId}?`
		: `/${entityType}/insert-editor?`;

	fetch(url)
		.then(response => response.json())
		.then(data => {
			//답변 완료된 글을 수정 시
			if (entityType === 'qna' && data.answer !== null && data.answer !== undefined) {
				alert('답변완료된 글은 수정이 불가능합니다');
				return; //중단
			}
			let title = '';
			let pageContent = '';
			let rating = null;

			if (data) {
				// 수정 시 기존 데이터 채우기
				title = data.title;
				pageContent = data.content;
				if (entityType === "review") {
					rating = data.rating;
				}
			}

			const popupWindow = window.open('', 'editPopup', 'width=800,height=600');
			const htmlContent = `
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <title>${entityId ? '수정' : '작성'}</title>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 20px; }
                        input, button { width: 100%; padding: 10px; margin: 5px 0; }
                    </style>
                    <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
                    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
                </head>
                <body>
                    <input type="text" id="editorTitle" placeholder="제목을 입력하세요" value="${title}" />
                    ${entityType === "review" ? `
                        <select id="editorRating">
                            <option value="1" ${rating === 1 ? "selected" : ""}>1점</option>
                            <option value="2" ${rating === 2 ? "selected" : ""}>2점</option>
                            <option value="3" ${rating === 3 ? "selected" : ""}>3점</option>
                            <option value="4" ${rating === 4 ? "selected" : ""}>4점</option>
                            <option value="5" ${rating === 5 ? "selected" : ""}>5점</option>
                        </select>
                    ` : ''}
                    <div id="editorContainer" style="height: 300px;"></div>
                    <button type="button" id="submitBtn">${entityId ? '수정' : '작성'} 저장</button>
                </body>
                </html>
            `;

			popupWindow.document.write(htmlContent);
			popupWindow.document.close();

			popupWindow.onload = function() {
				const editor = new toastui.Editor({
					el: popupWindow.document.querySelector('#editorContainer'),
					height: '450px',
					initialEditType: 'markdown',
					initialValue: pageContent,
					previewStyle: 'vertical',
					placeholder: '내용을 입력해 주세요.',
					hooks: {
						async addImageBlobHook(blob, callback) {
							try {
								const formData = new FormData();
								formData.append('image', blob);

								const response = await fetch('/tui-editor/image-upload', {
									method: 'POST',
									body: formData,
								});

								const filename = await response.text();
								const imageUrl = `/tui-editor/image-print?filename=${filename}`;
								callback(imageUrl, 'image alt attribute');
							} catch (error) {
								console.error('업로드 실패 : ', error);
							}
						}
					}
				});

				// 저장 버튼
				popupWindow.document.getElementById('submitBtn').onclick = function() {
					const title = popupWindow.document.getElementById('editorTitle').value;
					const content = editor.getMarkdown();
					let rating = null;

					if (entityType === 'review') {
						const editorRatingElement = popupWindow.document.getElementById('editorRating');
						if (editorRatingElement) {
							rating = editorRatingElement.value;
						}
					}

					const bodyData = {
						title,
						content,
					};

					if (isUpdate) {
						bodyData[`${entityType}Id`] = entityId;
					} else {
						bodyData.orderId = entityId; //review 작성시 주문번호 넘김
					}

					if (rating !== null) {
						bodyData.rating = rating;
					}

					const endpoint = isUpdate ? `/${entityType}/update` : `/${entityType}/insert`;

					fetch(endpoint, {
						method: isUpdate ? 'PUT' : 'POST',
						headers: {
							'Content-Type': 'application/json',
						},
						body: JSON.stringify(bodyData),
					})
						.then(response => response.json())
						.then(data => {
							console.log(data);
							if (data.success) {
								alert(data.message);
								popupWindow.close();
								location.reload();
							} else {
								alert(data.message);
							}
						})
						.catch(error => {
							console.error('저장 실패:', error);
							alert('저장 중 오류가 발생했습니다.');
						});
				};
			};
		})
		.catch(error => {
			console.error('페이지 내용을 불러오는 중 오류 발생:', error);
		});
};

//삭제
window.deleteEntity = function(entityType, entityId) {
    if (confirm("정말 삭제하시겠습니까?")) {
        let url = `/${entityType}/delete?${entityType}Id=${entityId}`;
        let method = '';

        if (entityType === 'review') {
            method = 'PUT'; // 상태만 업데이트
        } else if (entityType === 'qna') {
            method = 'DELETE';
        }

        fetch(url, {
            method: method,
        })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            location.reload();
        })
        .catch(error => {
            console.error('삭제 실패:', error);
        });
    }
};