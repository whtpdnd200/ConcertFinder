function ratingComponent() {
    return {
        rating: 3.5,        // 기본값 3.5점 클릭시 확정되는 점수
        hoverRating: 0,   // 마우스 올린 임시 점수

        getStarClass(i) {
            // 마우스가 올라가 있으면 hoverRating 기준, 아니면 확정된 rating 기준
            let current = this.hoverRating || this.rating;

            // i(해당 조각의 값)가 현재 점수보다 작거나 같으면 꽉 찬 별
            return i <= current ? 'bi-star-fill text-warning' : 'bi-star text-muted';


        }
    }
}


function reviewData() {
    return {
        reviews: {
            result: "",
            message: "",
            data: {
                content: [],
                page: { number: 0, totalPages: 0, first: true, last: true }
            },
        },
        orderType: "desc",
        reviewPageRange: [],
        // 타임리프 태그에서 concertId를 가져옴
        concertId: $("#reviewSubmitBtn").data("area-code"),

        loadReviews(page = 0) {
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            let size = 5;
            fetch('/review/' + this.concertId + '?page=' + page + '&size=' + size + '&orderType=' + this.orderType)
                .then(res => {
                    if(!res.ok) {
                        return res.json().then(err => {
                            throw new Error(err.message);
                        });
                    }
                    return res.json();
                })
                .then(data => {
                    this.reviews = data;
                    this.calculatePageRange();
                })
                .catch(error => alert(error));
        },

        reviewChangePage(page) {
            if (page >= 0 && page < this.reviews.data.page.totalPages) {
                this.loadReviews(page);
            }
        },

        calculatePageRange() {
            const current = this.reviews.data.page.number; // 0부터 시작
            const total = this.reviews.data.page.totalPages; // 총 페이지 블럭 갯수
            const rangeSize = 5; // 화면에 한 번에 보여줄 페이지 블럭 개수

            // 페이지 블럭의 시작 번호를 저장 하는 변수
            let start = Math.floor(current / rangeSize) * rangeSize;
            // 페이지 블럭의 마지막 번호를 저장하는 변수
            let end = Math.min(start + rangeSize, total);

            this.reviewPageRange = [];
            for (let i = start + 1; i <= end; i++) {
                this.reviewPageRange.push(i);
            }
        },

        renderStars(rating) {
            let starsHtml = '';
            let head = Math.floor(rating);
            let hasHalf = (rating % 1 !== 0);

            // 꽉 찬 별
            for (let i = 0; i < head; i++) {
                starsHtml += '<i class="bi bi-star-fill"></i>';
            }
            // 반쪽 별
            if (hasHalf) {
                starsHtml += '<i class="bi bi-star-half"></i>';
            }
            // 빈 별 (5개 기준)
            let emptyCount = 5 - head - (hasHalf ? 1 : 0);
            for (let i = 0; i < emptyCount; i++) {
                starsHtml += '<i class="bi bi-star"></i>';
            }
            return starsHtml;
        },

        formatDate(dateStr) {
            return dateStr.split('T')[0];
        }
    }

}