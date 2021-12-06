package com.bs.search.common;

import lombok.Getter;

/**
 * packageName : com.bs.search.common
 * fileName : ErrorEnum
 * author : yelee
 * date : 2021-12-04
 * description : error 관련 기본 설정 정의
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public enum ErrorCode {

    NOT_FOND(30, "검색결과가 없습니다."),
    NOT_NULL(10, "필수값이 누락되었습니다"),
    UNWATCH_TYPE_VALUE(20, "검색 조건이 맞지 않습니다.");

    @Getter
    private final int code;

    @Getter
    private final String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
