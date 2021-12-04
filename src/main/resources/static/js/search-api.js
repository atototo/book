/* [테이블 리스트 전역 변수 선언 실시] */
let tableList = []; // tableInsert 함수에서 for문을 돌면서 삽입 실시
let pageList = 5; // 한개의 페이지에 보여질 목록 개수
let pageMax = 3; // 최대 생성될 페이지 개수 (페이지를 더보려면 이전, 다음 버튼 클릭해야함)
var idx = 0; //idx 값 확인 후 동적 페이지 전환 및 데이터 표시
var pageStart = 1; //생성 시작할 페이지 번호


function searchBooks(page) {
    searchKind = $("#searchKind option:selected").val();

    let keyWord = "";

    if ("bookTitle" == searchKind) {
        keyWord = $("#searchTitle").val();
    } else {
        const min = $("#searchPriceMin").val();
        var max = $("#searchPriceMax").val()
        keyWord = "가격범위 ("+min +"~" + max+")";
    }
    storeSearchField(keyWord);

    $.ajax({
        url:"/searchBooks"
        , method : 'POST'
        , data : makeReqData(searchKind, page)
        , contentType: "application/json; charset=UTF-8"
        , success :  function(resp){
            console.log(resp);
            makeTable(resp);
        }
        , error : function (e) {
            console.log(e);
            const resData =JSON.parse(JSON.stringify(e));

            alert(resData.responseJSON.message);
        }
    })

}

function makeTable(resp){
    var result = JSON.parse(JSON.stringify(resp));
    var pageable = result.pageable;
    tableList = result.content;
    pageList = pageable.pageSize; // 한개의 페이지에 보여질 목록 개수
    pageMax = result.totalPages; // 최대 생성될 페이지 개수 (페이지를 더보려면 이전, 다음 버튼 클릭해야함)
    idx = pageable.numberOfElements; //idx 값 확인 후 동적 페이지 전환 및 데이터 표시
    pageStart =  result.number; //생성 시작할 페이지 번호

    $("#dyn_ul").empty();
    var count = 1;
    for(var i=0; i<pageMax; i++){
        var insertUl = "<li class='page-item'>"; // 변수 선언
        if(i == pageStart){
            insertUl = insertUl + "<a class='page-link' style='color:orangered;' href='javascript:void(0)' onclick = 'callPage("+i+");'/>";
        } else {
            insertUl = insertUl + "<a class='page-link' href='javascript:void(0)' onclick = 'callPage("+i+");'/>";

        }
        insertUl = insertUl + String(i+1);
        insertUl = insertUl + "</a></li>";
        $("#dyn_ul").append(insertUl); //jquery append 사용해 동적으로 추가 실시
    }
    $("#dyn_tbody").empty();
    for(var i=0; i<tableList.length; i++){
        // json 데이터 파싱 실시
        console.log()
        var jsonObject = JSON.parse(JSON.stringify(tableList[i])); //각 배열에 있는 jsonObject 참조
        var id = jsonObject.id;
        var title = jsonObject.title;
        var price = jsonObject.price;


        // 동적으로 리스트 추가
        var insertTr = ""; // 변수 선언
        insertTr += "<tr>"; // body 에 남겨둔 예시처럼 데이터 삽입
        insertTr += "<th scope='row'>" + id + "</th>"; // body 에 남겨둔 예시처럼 데이터 삽입
        insertTr += "<td>" + title + "</td>";
        insertTr += "<td>" + price + "</td>";
        insertTr += "</tr>";

        $("#dyn_tbody").append(insertTr);

    }
}

function callPage(pageNum) {

    $.ajax({
        url:"/searchBooks"
        , data : makeReqData(searchKind, pageNum)
        , contentType: "application/json; charset=UTF-8"
        , method : 'POST'
        , success :  function(resp){
            makeTable(resp);
        }
    })
}

function makeReqData(searchKind, page) {
    let resData = "";

    // 리스트 생성
    var dataList = new Array() ;
    var PageSearchDto = new Object() ;

    if ("bookTitle" === searchKind) {
        PageSearchDto.cdSearch = searchKind;
        PageSearchDto.title =  $("#searchTitle").val();
        PageSearchDto.pageNum = parseInt(page);

        console.log(JSON.stringify(PageSearchDto));
        return JSON.stringify(PageSearchDto);
    } else {
        PageSearchDto.cdSearch = searchKind;
        PageSearchDto.minPrice =  $("#searchPriceMin").val();
        PageSearchDto.maxPrice =  $("#searchPriceMax").val();
        PageSearchDto.pageNum = parseInt(page);

        console.log(JSON.stringify(PageSearchDto));
        return JSON.stringify(PageSearchDto);
    }

}



//검색어 저장
function storeSearchField(value) {

    var storage = localStorageFind(value);

    if(storage != null) {
        console.log(typeof localStorage.getItem(value));
        var strCnt = parseInt(localStorage.getItem(value));
        strCnt++;

        localStorage.setItem(value, strCnt.toString());
    } else {
        localStorage.setItem(value, 1);

    }
    makeSearchingKeyword();
}

function localStorageFind(value) {

    var storage = localStorage.getItem(value);
    return storage;
}


