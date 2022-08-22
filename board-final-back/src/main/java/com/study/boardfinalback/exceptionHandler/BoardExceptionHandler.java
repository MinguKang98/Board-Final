package com.study.boardfinalback.exceptionHandler;

import com.study.boardfinalback.dto.ErrorResponse;
import com.study.boardfinalback.error.boards.BoardTypeNotFoundException;
import com.study.boardfinalback.error.boards.BoardTypeNotMatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Board Exception 처리하는 클래스
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BoardExceptionHandler {

    /**
     * BoardTypeNotMatchException 처리
     *
     * @param e
     * @return : 400 with ErrorResponse
     */
    @ExceptionHandler(BoardTypeNotMatchException.class)
    public ResponseEntity<ErrorResponse> handleBoardTypeNotMatchException(BoardTypeNotMatchException e) {
        log.info(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * BoardTypeNotFoundException 처리
     *
     * @param e
     * @return : 400 with ErrorResponse
     */
    @ExceptionHandler(BoardTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardTypeNotFoundException(BoardTypeNotFoundException e) {
        log.info(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
