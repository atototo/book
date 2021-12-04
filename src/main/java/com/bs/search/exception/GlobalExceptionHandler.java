package com.bs.search.exception;

import com.bs.search.common.ErrorCode;
import com.bs.search.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName : com.bs.search.exception
 * fileName : GlobalExceptionHandler
 * author : yelee
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(BooksNotFoundException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handlePersonNotFoundException(BooksNotFoundException ex) {
      return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
   }


   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
      log.info("MethodArgumentNotValidException 발생!!! url:{}, trace:{}",request.getRequestURI(), e.getStackTrace());
      ErrorResponse errorResponse =  ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.UNMATCH_TYPE_VALUE.getDescription());
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
   }

}
