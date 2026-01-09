function commentData() {

    // 댓글 리스트와 페이징에 필요한 정보들을 저장하기 위한 리스트
    items = {
        comments: {
            result: "",
            message: "",
            data: {
                content: [],
                page: { number: 0, totalPages: 0, first: true, last: true }
            }
        },
        commentPageRange: [], // 페이지 블럭이 들어갈 리스트
        // 파라미터에 default값 명시해서 첫 페이지에서도 잘 동작 하도록 설정
        loadComments(page = 0) {
            let size = 5; // 한 페이지당 보여줄 댓글의 갯수
            let postId = $("#postIdTag").data("id"); // 해당 하는 게시글 페이지에서 작성된 댓글만 보여주기 위해 게시글 id값 저장
            // 자바스크립트의 fetch 메서드를 사용해 비동기 통신으로 API 데이터 가져옴
            fetch('/comment/' + postId + '/list?page=' + page + '&size=' + size)
                .then(res => res.json())
                .then(data => {
                    this.comments = data; // content와 page 정보가 한 번에 들어옴
                    this.calculatePageRange();
                });
        },
        // 페이지 번호 5개씩 끊어서 보여주는 로직
        calculatePageRange() {
            const current = this.comments.data.page.number; // 0부터 시작
            const total = this.comments.data.page.totalPages; // 총 페이지 블럭 갯수
            const rangeSize = 5; // 화면에 한 번에 보여줄 페이지 블럭 개수

            // 페이지 블럭의 시작 번호를 저장 하는 변수
            let start = Math.floor(current / rangeSize) * rangeSize;
            // 페이지 블럭의 마지막 번호를 저장하는 변수
            let end = Math.min(start + rangeSize, total);

            this.commentPageRange = [];
            for (let i = start + 1; i <= end; i++) {
                this.commentPageRange.push(i);
            }
        },
        // 페이지를 이동 시킬때 실행 되는 함수
        changePage(p) {
            if (p >= 0 && p < this.comments.data.page.totalPages) {
                this.loadComments(p);
            }
        }
    }
    console.log(items);
    return items;
}

function getCommentDeleteId(commentId) {

    return "commentDeleteBtn" + commentId;
}

function getDate(date) {

    let TIME_ZONE = 9 * 60 * 60 * 1000;

    let dateText = new Date(date);


    return new Date(dateText.getTime() + TIME_ZONE).toISOString().replace('T', ' ').slice(0, -5);;
}