package com.bs.search.exception;

import com.bs.search.common.ErrorCode;
import com.bs.search.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;

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
   public ResponseEntity<ErrorResponse> handleBooksNotFoundException(BooksNotFoundException ex) { //메소드명 수정 필요했다,.
      ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
      log.info(">>>>>>>>>>>>>>>>>>> BooksNotFoundException error");
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
      log.info("MethodArgumentNotValidException 발생!!! url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
      ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.UNWATCH_TYPE_VALUE.getDescription());
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
   }



   @ExceptionHandler(RuntimeException.class)
   public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {
      log.info("RuntimeException 발생!!! ");
      ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.DB_ACCESS_FAIL.getDescription());
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
   }


}
