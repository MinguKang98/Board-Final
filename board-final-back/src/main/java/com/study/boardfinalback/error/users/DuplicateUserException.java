package com.study.boardfinalback.error.users;

import com.study.boardfinalback.error.common.Messages;

import java.util.Map;

public class DuplicateUserException extends RuntimeException{

    private Map<String, String> errors;

    public DuplicateUserException(Map<String, String> errors) {
        super(Messages.DUPLICATED_USER_MESSAGE);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
