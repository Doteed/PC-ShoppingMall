    document.addEventListener('DOMContentLoaded', () => {
        let currentIndex = 0;
        const images = document.querySelectorAll('.banner-image');
        const totalImages = images.length;
        const bannerImages = document.getElementById('bannerImages');
        const prevBtn = document.getElementById('prevBtn');
        const nextBtn = document.getElementById('nextBtn');

        // 슬라이드 이동
        function moveToSlide(index) {
            currentIndex = index;
            if (currentIndex < 0) currentIndex = totalImages - 1; // 첫 번째 이미지 이전 -> 마지막
            if (currentIndex >= totalImages) currentIndex = 0; // 마지막 이미지 다음 -> 첫 번째
            bannerImages.style.transform = `translateX(-${currentIndex * 100}%)`;
        }

        // 자동 슬라이드
        function autoSlide() {
            moveToSlide(currentIndex + 1);
        }
        let slideInterval = setInterval(autoSlide, 4000); // 4초마다 자동 이동

        // 버튼 이벤트
        prevBtn.addEventListener('click', () => {
            moveToSlide(currentIndex - 1);
            resetAutoSlide();
        });
        nextBtn.addEventListener('click', () => {
            moveToSlide(currentIndex + 1);
            resetAutoSlide();
        });

        // 자동 슬라이드 리셋
        function resetAutoSlide() {
            clearInterval(slideInterval); // 기존 슬라이드 중지
            slideInterval = setInterval(autoSlide, 4000); // 다시 시작
        }
    });
