package com.bs.search.common;

/**
 * packageName : com.bs.search.common=
 * fileName : ApiEnum
 * author : yelee
 * date : 2021-12-04
 * description : API 관련 기본 설정 값 정의
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public enum ApiEnum {

    TARGET_KEY("target"),
    TARGET_VALUE("title"),
    QUERY_KEY("query"),
    QUERY_VALUE("카카오");

    private final String value;

    ApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
