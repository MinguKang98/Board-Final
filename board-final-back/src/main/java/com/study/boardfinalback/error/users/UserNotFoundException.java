package com.study.boardfinalback.error.users;

import com.study.boardfinalback.error.common.Messages;
import com.study.boardfinalback.error.common.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(Messages.NO_USER_MESSAGE);
    }
}
