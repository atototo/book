
$(document).ready(function() {
    makeSearchingKeyword();
});


function chgSearchForm(value){
    console.log(value);
    if ("bookTitle" == value) {
        $("#titleForm").show();
        $("#priceForm").hide();
        $("[id^='searchPrice']").val(0);
    } else {
        $("#titleForm").hide();
        $("#priceForm").show();
        $("#searchTitle").val("");
    }
}

function makeSearchingKeyword() {
    var length = localStorage.length;
    if (length < 1) {
        return;
    }

    var keywordMap = JSON.parse(JSON.stringify((localStorage)));

    var innerTxt = "";
    for (var i=0; i<length; i++) {

        var tmpkey =    localStorage.key(i);
        innerTxt += " <span class='badge rounded-pill bg-primary'>"+tmpkey+"-"+localStorage.getItem(tmpkey)+"</span>";
    }

    $("#listSearched").empty();
    $("#listSearched").append(innerTxt);

}