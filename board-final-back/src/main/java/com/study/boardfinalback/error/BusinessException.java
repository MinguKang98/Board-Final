package com.study.boardfinalback.error;

public class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}

