package com.study.boardfinalback.apiExceptionHandler;

import com.study.boardfinalback.apiDto.ErrorResponse;
import com.study.boardfinalback.error.boards.BoardTypeException;
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
     * BoardTypeException 처리
     *
     * @param e
     * @return : 400 with ErrorResponse
     */
    @ExceptionHandler(BoardTypeException.class)
    public ResponseEntity<ErrorResponse> handleBoardTypeException(BoardTypeException e) {
        log.info(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
