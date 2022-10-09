package com.study.boardfinalback.exceptionHandler;

import com.study.boardfinalback.dto.ErrorResponse;
import com.study.boardfinalback.error.users.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * User Exception 처리하는 클래스
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    /**
     * DuplicateUserException 처리
     *
     * @param e
     * @return : 409 with ErrorResponse
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * AuthenticationException 처리
     *
     * @param e
     * @return : 401 with ErrorResponse
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * AuthorizationException 처리
     *
     * @param e
     * @return : 403 with ErrorResponse
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * PasswordChangeDtoValidException 처리
     *
     * @param e
     * @return : 400 with ErrorResponse
     */
    @ExceptionHandler(PasswordChangeDtoValidException.class)
    public ResponseEntity<ErrorResponse> handlePasswordChangeDtoValidException(PasswordChangeDtoValidException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * UserStatusException 처리
     *
     * @param e
     * @return : 400 with ErrorResponse
     */
    @ExceptionHandler(UserStatusException.class)
    public ResponseEntity<ErrorResponse> handleUserStatusException(UserStatusException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(new HashMap<>())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
