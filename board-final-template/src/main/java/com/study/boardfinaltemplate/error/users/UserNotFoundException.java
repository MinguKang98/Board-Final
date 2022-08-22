package com.study.boardfinaltemplate.error.users;

import com.study.boardfinaltemplate.error.common.Messages;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;

/**
 * 존재하지 않는 User 접근 시 throw
 */
public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(Messages.NO_USER_MESSAGE);
    }
}
