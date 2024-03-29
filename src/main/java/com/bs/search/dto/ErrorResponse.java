package com.bs.search.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.io.Serializable;

/**
 * packageName : com.bs.search.dto
 * fileName : ErrorResponse
 * author : isbn8
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 6600165138863711638L;

    private int code;
    private String message;

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }

    public static ErrorResponse of(HttpStatus httpStatus, FieldError fieldError) {
        if (fieldError == null) {
            return new ErrorResponse(httpStatus.value(), "invalid params");
        } else {
            return new ErrorResponse(httpStatus.value(), fieldError.getDefaultMessage());
        }
    }
}
