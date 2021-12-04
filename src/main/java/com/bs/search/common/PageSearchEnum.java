package com.bs.search.common;

/**
 * packageName : com.bs.search.common
 * fileName : PageSearchEnum
 * author : yelee
 * date : 2021-12-04
 * description : search 조건항목 정의
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public enum PageSearchEnum {

    SEARCH_BY_TITLE("bookTitle"),
    SEARCH_BY_PRICE_BETWEEN("bookPrice");

    private final String value;

    PageSearchEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
