package com.study.boardfinaltemplate.error.common;

/**
 * 존재하지 않는 resource 접근 시 throw
 */
public class ResourceNotFoundException extends RuntimeException {
    protected ResourceNotFoundException(String message) {
        super(message);
    }
}

