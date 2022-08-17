package com.study.boardfinalback.error.users;

import com.study.boardfinalback.error.common.Messages;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException() {
        super(Messages.UNAUTHORIZED_USER_MESSAGE);
    }
}
