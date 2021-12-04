package com.bs.search.exception;

import com.bs.search.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName : com.bs.search.exception
 * fileName : GlobalExceptionHandler
 * author : yelee
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       isbn8         최초 생성
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(BooksNotFoundException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handlePersonNotFoundException(BooksNotFoundException ex) {
      return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
   }
}
