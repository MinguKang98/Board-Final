package com.study.boardfinalback.apiExceptionHandler;

import com.study.boardfinalback.apiDto.ErrorResponse;
import com.study.boardfinalback.error.users.AuthenticationException;
import com.study.boardfinalback.error.users.AuthorizationException;
import com.study.boardfinalback.error.users.DuplicateUserException;
import com.study.boardfinalback.error.users.PasswordChangeDtoValidException;
import lombok.extern.slf4j.Slf4j;
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
public class UserExceptionHandler {

    /**
     * DuplicateUserException 처리
     *
     * @param e
     * @return : 409 with ErrorResponse
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException e) {
        log.info(e.getMessage());
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
        log.info(e.getMessage());
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
        log.info(e.getMessage());
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
        log.info(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
