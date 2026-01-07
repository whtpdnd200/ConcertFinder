// 동적 페이징을 위한 비동기 통신 함수 및 페이징 처리 함수
function postData() {

    // 게시글 리스트와 페이징에 필요한 정보들을 저장하기 위한 리스트
    items = {
        posts: {
            result: "",
            message: "",
            data: {
                content: [],
                page: { number: 0, totalPages: 0, first: true, last: true }
            }
        },
        currentCategory: 'A', // 현재 선택된 카테고리 저장
        pageRange: [], // 페이지 블럭이 들어갈 리스트
        // 파라미터에 default값 명시해서 첫 페이지에서도 잘 동작 하도록 설정
        loadPosts(page = 0, category = this.currentCategory) {
            this.currentCategory = category; // 파라미터로 들어온 카테고리 값으로 선택된 카테고리 값 변경
            let size = 5; // 한 페이지당 보여줄 게시글의 갯수
            let concertId = $("#idStorageTag").data("concert-id"); // 해당 하는 콘서트 페이지에서 작성된 게시글만 보여주기 위해 콘서트 id값 저장
            // 자바스크립트의 fetch 메서드를 사용해 비동기 통신으로 API 데이터 가져옴
            fetch('/post/' + concertId + '/list?page=' + page + '&size=' + size + '&category=' + category)
                .then(res => res.json())
                .then(data => {
                    this.posts = data; // content와 page 정보가 한 번에 들어옴
                    this.calculatePageRange();
                });
        },
        // 페이지 번호 5개씩 끊어서 보여주는 로직
        calculatePageRange() {
            const current = this.posts.data.page.number; // 0부터 시작
            const total = this.posts.data.page.totalPages; // 총 페이지 블럭 갯수
            const rangeSize = 5; // 화면에 한 번에 보여줄 페이지 블럭 개수

            // 페이지 블럭의 시작 번호를 저장 하는 변수
            let start = Math.floor(current / rangeSize) * rangeSize;
            // 페이지 블럭의 마지막 번호를 저장하는 변수
            let end = Math.min(start + rangeSize, total);

            this.pageRange = [];
            for (let i = start + 1; i <= end; i++) {
                this.pageRange.push(i);
            }
        },
        // 페이지를 이동 시킬때 실행 되는 함수
        changePage(p) {
            if (p >= 0 && p < this.posts.data.page.totalPages) {
                this.loadPosts(p);
            }
        }
    }
    //console.log(items);
    return items;
}

function getPostDetailUrl(postId) {

    return "/post/" + postId;
}

function getCategory(code) {
    let name = '';
    if(code == 'F') {
        return '자유글';
    }
    if(code == 'T') {
        return '티켓 양도';
    }
    if(code == 'R') {
        return '동행 모집';
    }
}

function getDate(date) {

    return date.split('T')[0];
}