document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('qnaForm').addEventListener('submit', function (event) {
        const isSecretChecked = document.getElementById('isSecret').checked; // 체크 여부
        const passwordField = document.getElementById('password'); // 비밀번호 필드
        const password = passwordField.value.trim();

        if (isSecretChecked && password === '') { 
            alert('비밀번호를 입력하세요!');
            passwordField.focus();
            event.preventDefault();
            return;
        }

        if (!isSecretChecked && password !== '') { 
            const confirmSave = confirm('비밀번호를 설정하시겠습니까?'); 
            if (confirmSave) {
                document.getElementById('isSecret').checked = true;
            } else {
                event.preventDefault();
            }
        }
    });
});
