package com.study.boardfinalback.error.users;

import com.study.boardfinalback.error.common.Messages;

import java.util.Map;

public class PasswordChangeDtoValidException extends RuntimeException{
    private Map<String, String> errors;

    public PasswordChangeDtoValidException(Map<String, String> errors) {
        super(Messages.PASSWORD_CHANGE_DTO_INVALID_MESSAGE);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
