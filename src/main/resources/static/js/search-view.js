//화면 최초 호출시 키워드, 기본 목록 화면 표시
$(document).ready(function() {
    makeSearchingKeyword();
    noDataRow();
});

/**
 * 검색구분에 따른 화면 변경 표시
 * @param value
 */
function chgSearchForm(value){
    console.log(value);
    if ("bookTitle" === value) {
        $("#titleForm").show();
        $("#priceForm").hide();
        $("[id^='searchPrice']").val(0);
    } else {
        $("#titleForm").hide();
        $("#priceForm").show();
        $("#searchTitle").val("");
    }
}

/**
 * 조회 목록 없을 경우 그려지는 기본 row
 */
function noDataRow() {

    let insertDefaultTr = ""; // 변수 선언
    insertDefaultTr += "<tr >"; // body 에 남겨둔 예시처럼 데이터 삽입
    insertDefaultTr += "<th scope='row' colspan ='3'> 조회 된 도서 목록이 없습니다 </th>"; // body 에 남겨둔 예시처럼 데이터 삽입
    insertDefaultTr += "</tr>";
    $("#dyn_tbody").empty();
    $("#dyn_tbody").append(insertDefaultTr);
}

/**
 * 조회목록 키워드 상단 노출
 */
function makeSearchingKeyword() {
    const length = localStorage.length;
    if (length < 1) {
        return;
    }
    let keywordMap = JSON.parse(JSON.stringify((localStorage)));
    let innerTxt = "";

    for (let i=0; i<length; i++) {

        const key = localStorage.key(i);
        innerTxt += " <span class='badge rounded-pill bg-primary'>"+key+" : [ "+localStorage.getItem(key)+" ]</span>";
    }

    //기존 키워드 리셋
    $('#listSearched').empty();
    //키워드 새로 화면 노출
    $('#listSearched').append(innerTxt);

}