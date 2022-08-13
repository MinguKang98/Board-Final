package com.study.boardfinalback.controller;

import com.study.boardfinalback.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 내부에서 발생하는 Exception 처리하는 클래스
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BusinessException 발생 시 return /errors/404.html
     *
     * @param e
     * @return : /errors/404.html
     */
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e) {
        log.info(e.getMessage());
        return "/errors/404";
    }

    /**
     * HttpRequestMethodNotSupportedException 발생 시 return /errors/404.html
     *
     * @param e
     * @return : /errors/404.html
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info(e.getMessage());
        return "/errors/404";
    }

    /**
     * RuntimeException 발생 시 return /errors/500.html
     *
     * @param e
     * @return : /errors/500.html
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e) {
        log.info(e.getMessage());
        return "/errors/500";
    }

}
