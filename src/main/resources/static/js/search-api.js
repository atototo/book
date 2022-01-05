/**
 * 도서명 입력후 엔터키 검색
 * @param page
 * @returns {boolean}
 */
$(document).ready(function(){

    $("#searchTitle").keydown(function (key) {

        if(key.keyCode === 13){//키가 13이면 실행 (엔터는 13)
            searchBooks(0);
        }

    });

});

/* [테이블 리스트 전역 변수 선언 실시] */
let tableList = []; // tableInsert 함수에서 for문을 돌면서 삽입 실시
let pageList = 5; // 한개의 페이지에 보여질 목록 개수
let pageMax = 3; // 최대 생성될 페이지 개수 (페이지를 더보려면 이전, 다음 버튼 클릭해야함)
let idx = 0; //idx 값 확인 후 동적 페이지 전환 및 데이터 표시
let pageStart = 1; //생성 시작할 페이지 번호


/**
 * 도서 검색 메소드
 * @param page
 * @returns {boolean}
 */
function searchBooks(page) {
    //조회구분
    let searchKind = $("#searchKind option:selected").val();

    //검색 키워드 설정
    let keyWord = "";

    //조회 구분이 타이틀일 경우
    if ("bookTitle" === searchKind) {
        keyWord = $("#searchTitle").val();
        const title = $('#searchTitle').val();
        //조회 타이틀 체크 : 입력 안됬을경우
        if (!title) {
            alert("책 제목을 입력해주세요");
            return false;
        }
        // 조회 구분이 정가 금액일 경우
    } else {
        const min = $("#searchPriceMin").val();
        const max = $("#searchPriceMax").val()
        keyWord = "가격범위 ("+min +"~" + max+")";
        const title = $('#searchTitle').val();

        //조회 금액 체크 : 둘다 입력 안됬을경우
        if (min === "0" && max === "0") {
            alert("금액을 입력해주세요");
            return false;
        }

        //조회 금액 체크 : 최소금액 안됬을경우
        if (min === "0" || !min) {
            alert("최소 금액을 입력해주세요");
            return false;
        }

        //조회 금액 체크 : 최대금액 안됬을경우
        if (max === "0" || !max) {
            alert("최대 금액을 입력해주세요");
            return false;
        }

        console.log(min);
        console.log(max);
        //조회 금액 체크 : 최소금액이 최대금액보다 같거나 클경우
        if( parseInt(min )>=  parseInt(max)) {
            alert("최소금액은 최대금액보다 클 수 없습니다.");
            return false;
        }
    }

    //도서 목록 조회
    $.ajax({
        url:"/searchBooks"
        , method : 'POST'
        , data : makeReqData(searchKind, page)
        , async:true
        , contentType: "application/json"
        , success :  function(resp){
            //도서목록 화면 표시
            makeTable(resp);
            //로컬 스토리지 검색어 저장 (-> 성공일 경우)
            storeSearchField(keyWord);
        }
        , error: function (e) {
            const resData = JSON.parse(JSON.stringify(e));
            console.log(resData);
            alert(resData.responseJSON.message);
            noDataRow();
        }
    })

}

/**
 * 조회 목록 화면 표시
 * @param resp
 */
function makeTable(resp){
    let i;
    const result = JSON.parse(JSON.stringify(resp));
    const pageable = result.pageable;
    tableList = result.content;
    pageList = pageable.pageSize; // 한개의 페이지에 보여질 목록 개수
    pageMax = result.totalPages; // 최대 생성될 페이지 개수 (페이지를 더보려면 이전, 다음 버튼 클릭해야함)
    idx = pageable.numberOfElements; //idx 값 확인 후 동적 페이지 전환 및 데이터 표시
    pageStart =  result.number; //생성 시작할 페이지 번호

    $('#dyn_ul').empty();
    const count = 1;
    for(i = 0; i<pageMax; i++){
        let insertUl = "<li class='page-item'>"; // 변수 선언
        if(i === pageStart){
            insertUl = insertUl + "<a class='page-link' style='color:orangered;' href='javascript:void(0)' onclick = 'callPage("+i+");'/>";
        } else {
            insertUl = insertUl + "<a class='page-link' href='javascript:void(0)' onclick = 'callPage("+i+");'/>";

        }
        insertUl = insertUl + String(i+1);
        insertUl = insertUl + "</a></li>";
        $("#dyn_ul").append(insertUl); //jquery append 사용해 동적으로 추가 실시
    }

    $('#dyn_tbody').empty();
    tableList.forEach(item => {
        // json 데이터 파싱 실시
        const jsonObject = JSON.parse(JSON.stringify(item)); //각 배열에 있는 jsonObject 참조
        const id = jsonObject.id;
        const title = jsonObject.title;
        const price = jsonObject.price;


        // 동적으로 리스트 추가
        let insertTr = ""; // 변수 선언
        insertTr += "<tr>"; // body 에 남겨둔 예시처럼 데이터 삽입
        insertTr += "<th scope='row'>" + id + "</th>"; // body 에 남겨둔 예시처럼 데이터 삽입
        insertTr += "<td>" + title + "</td>";
        insertTr += "<td>" + price + "</td>";
        insertTr += "</tr>";

        $("#dyn_tbody").append(insertTr);

    });
}

/**
 * 조회 목록 페이지 조회
 * @param pageNum
 */
function callPage(pageNum) {
    let searchKind = $("#searchKind option:selected").val();
    $.ajax({
        url:"/searchBooks"
        , method : 'POST'
        , data : makeReqData(searchKind, pageNum)
        , async: true
        , contentType: "application/json"
        , success :  function(resp){
            //도서목록 화면 표시
            makeTable(resp);
        }
        , error : function (e) {
            console.log(e);
            const resData = JSON.parse(JSON.stringify(e));
            console.log(resData);
            alert(resData.responseJSON.message);
            noDataRow();
        }
    })
}

/**
 * 요청 데이터 생성
 * @param searchKind
 * @param page
 * @returns {string}
 */
function makeReqData(searchKind, page) {
    let resData = "";

    // 리스트 생성
    const dataList = [];
    const PageSearchDto = {};

    if ("bookTitle" === searchKind) {
        PageSearchDto.cdSearch = searchKind;
        PageSearchDto.title =  $("#searchTitle").val();
        PageSearchDto.pageNum = parseInt(page);
        return JSON.stringify(PageSearchDto);
    } else {
        PageSearchDto.cdSearch = searchKind;
        PageSearchDto.minPrice =  $("#searchPriceMin").val();
        PageSearchDto.maxPrice =  $("#searchPriceMax").val();
        PageSearchDto.pageNum = parseInt(page);
        return JSON.stringify(PageSearchDto);
    }

}

/**
 * 조회 성공한 검색어 저장
 * @param value
 */
function storeSearchField(value) {
    //로컬스토리지 목록 조회
    const storage = localStorageFind(value);

    //해당 키워들 조회 된 경우가 있는 경우 카운트 +1 처리
    if(storage != null) {
        console.log(typeof localStorage.getItem(value));
        let strCnt = parseInt(localStorage.getItem(value));
        strCnt++;

        localStorage.setItem(value, strCnt.toString());

        //해당 키워드로 조회된 경우가 없는 경우 새로 추가
    } else {
        localStorage.setItem(value, '1');

    }
    makeSearchingKeyword();
}

function localStorageFind(value) {
    return localStorage.getItem(value);
}
