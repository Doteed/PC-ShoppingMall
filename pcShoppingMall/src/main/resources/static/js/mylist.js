/**
 * 
 */

//qa
document.addEventListener('DOMContentLoaded', function() {
	const qaTitles = document.querySelectorAll('.qa-title');
	qaTitles.forEach((title) => {
		title.addEventListener('click', function() {
			const qaId = this.getAttribute('data-id');
			const userId = 'user01'; //임시 userId

			fetch(`/qa/detail/${qaId}?userId=${userId}`)
				.then(response => response.json())
				.then(data => {
					const entityDetailContainer = document.getElementById('entityDetailContainer');
					const timestamp = data.date;
					const date = new Date(timestamp);
					const year = date.getFullYear();
					const month = String(date.getMonth() + 1).padStart(2, '0');
					const day = String(date.getDate()).padStart(2, '0');
					const formattedDate = `${year}-${month}-${day}`;
					entityDetailContainer.innerHTML = `
			                        <h3>${data.title}</h3>
			                        <p>문의일: ${formattedDate}</p>
			                        <p>내용: ${data.content}</p>
			                        <div class="d-flex">
			                            <button type="button" class="btn btn-primary" onclick="openEditPopup('qa', ${qaId})">수정</button>
			                            <button type="button" class="btn btn-danger" onclick="deleteEntity('qa', ${qaId})">삭제</button>
			                        </div>
			                    `;
				})
				.catch(error => {
					console.error('Q&A 상세 내용 가져오기 실패:', error);
				});
		});
	});

	//review
	const key = "${param.key}";

	const writtenTab = document.getElementById("written-myreviews-tab");
	const writeTab = document.getElementById("write-myreviews-tab");
	const writtenContent = document.getElementById("written-myreviews");
	const writeContent = document.getElementById("write-myreviews");

	if (key === "write-myreviews") {
		//작성할 탭
		writtenTab.classList.remove("active");
		writeTab.classList.add("active");

		writtenContent.classList.remove("show", "active");
		writeContent.classList.add("show", "active");
	} else if (key === "written-myreviews") {
		//작성된 탭
		writeTab.classList.remove("active");
		writtenTab.classList.add("active");

		writeContent.classList.remove("show", "active");
		writtenContent.classList.add("show", "active");
	}
});