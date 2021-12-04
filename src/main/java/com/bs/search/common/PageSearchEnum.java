package com.bs.search.common;

/**
 * packageName : com.bs.search.common
 * fileName : PageSearchEnum
 * author : isbn8
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       isbn8         최초 생성
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
