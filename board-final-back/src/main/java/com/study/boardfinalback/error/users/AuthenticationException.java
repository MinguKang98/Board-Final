package com.study.boardfinalback.error.users;

import com.study.boardfinalback.error.common.Messages;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super(Messages.UNAUTHENTICATED_USER_MESSAGE);
    }
}
