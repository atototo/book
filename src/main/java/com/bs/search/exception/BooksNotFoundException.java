package com.bs.search.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * packageName : com.bs.search.exception
 * fileName : BooksNotFoundEception
 * author : isbn8
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       isbn8         최초 생성
 */
@Slf4j
public class BooksNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Book Entity가 존재하지 않습니다";

    public BooksNotFoundException(String message) {
        super(MESSAGE);
        log.error(MESSAGE);
    }

}
