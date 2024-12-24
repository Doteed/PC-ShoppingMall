//수정 팝업
window.openEditPopup = function(entityType, entityId) {
	const userId = 'user01'; //임시 userId

	fetch(`/${entityType}/detail/${entityId}?userId=${userId}`)
		.then(response => response.json())
		.then(data => {
			const title = data.title;
			const pageContent = data.content;

			const popupWindow = window.open('', 'editPopup', 'width=800,height=600');
			const htmlContent = `
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <title>수정</title>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 20px; }
                        input, button { width: 100%; padding: 10px; margin: 5px 0; }
                    </style>
					<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
                    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
                </head>
                <body>
                    <input type="text" id="editorTitle" placeholder="제목을 입력하세요" value="${title}" />
                    <div id="editorContainer" style="height: 300px;"></div>
                    <button type="button" id="submitBtn">저장</button>
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

				//저장
				popupWindow.document.getElementById('submitBtn').onclick = function() {
					const title = popupWindow.document.getElementById('editorTitle').value;
					const content = editor.getMarkdown();

					//로그 확인용
					console.log('data:', {
						qaId: qaId,
						title: title,
						content: content,
					});


					fetch(`/${entityType}/update`, {
						method: 'PUT',
						headers: {
							'Content-Type': 'application/json',
						},
						body: JSON.stringify({
							userId,
							title,
							content,
							[`${entityType}Id`]: entityId,
						})
					})
						.then(response => response.json())
						.then(data => {
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
	const userId = 'user01'; //임시 userId
	if (confirm("정말 삭제하시겠습니까?")) {
		fetch(`/${entityType}/delete?${entityType}Id=${entityId}&userId=${userId}`, {
			method: 'DELETE',
		})
			.then(response => response.json())
			.then(data => {
				alert(data.message);
				location.reload();
			})
			.catch(error => {
				console.error("삭제 실패:", error);
			});
	}
};