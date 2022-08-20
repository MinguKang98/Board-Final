package com.study.boardfinalback.apiDto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * error가 생겼을 때 사용하는 DTO
 */
@Getter
public class ErrorResponse {

    private int status;
    private String message;
    private Map<String, String> errors;

    @Builder
    public ErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

}
