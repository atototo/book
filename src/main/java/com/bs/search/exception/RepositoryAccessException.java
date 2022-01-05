package com.bs.search.exception;

import com.bs.search.common.ErrorCode;
import com.bs.search.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * packageName : com.bs.search.exception
 * fileName : RepositoryAccessException
 * author : isbn8
 * date : 2021-12-26
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-26       isbn8         최초 생성
 */
@Slf4j
public class RepositoryAccessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public RepositoryAccessException() {
        super(ErrorCode.DB_ACCESS_FAIL.getDescription());
        log.error(ErrorCode.DB_ACCESS_FAIL.getDescription());
    }
}
