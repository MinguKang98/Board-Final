package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.error.boards.BoardTypeNotFoundException;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;
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
     * 존재하지 않는 resource 접근 시 return /errors/404.html
     *
     * @param e
     * @return : /errors/404.html
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleBusinessException(ResourceNotFoundException e) {
        log.info(e.getMessage());
        return "/errors/404";
    }

    /**
     * 지원하지 않는 HttpRequestMethod 사용 시 return /errors/404.html
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
     * 게시글의 종류가 올바르지 않을 시 return /errors/404.html
     *
     * @param e
     * @return : /errors/404.html
     */
    @ExceptionHandler(BoardTypeNotFoundException.class)
    public String handleBoardTypeNotFoundException(BoardTypeNotFoundException e) {
        log.info(e.getMessage());
        return "/errors/404";
    }

    /**
     * 내부적인 오류 발생 시 return /errors/500.html
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
