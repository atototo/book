package com.bs.search.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * packageName : com.bs.search.exception
 * fileName : ApiNodataException
 * author : isbn8
 * date : 2021-12-26
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-26       isbn8         최초 생성
 */
@Slf4j
public class ApiNodataException extends RuntimeException {

    private static final long serialVersionUID = 2513506309216502071L;

    @Autowired
    private ApplicationContext context;

    public ApiNodataException(String message) {
        super(message);
        log.error(message);
        //슬랙 또는 카톡으로 개발자에게 알림
        // WAS 종료 : application 떠있어도 무의미
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
        System.exit(exitCode);
    }
}
