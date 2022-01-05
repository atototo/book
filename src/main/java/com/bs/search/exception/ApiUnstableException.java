package com.bs.search.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClientException;

/**
 * packageName : com.bs.search.exception
 * fileName : ApiUnstableException
 * author : isbn8
 * date : 2021-12-26
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-26       isbn8         최초 생성
 */
@Slf4j
public class ApiUnstableException extends RestClientException {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ApplicationContext context;


    public ApiUnstableException() {
        super("");
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
        System.exit(exitCode);
    }

}