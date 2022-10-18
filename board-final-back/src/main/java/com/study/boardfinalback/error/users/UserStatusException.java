package com.study.boardfinalback.error.users;

/**
 * 유저 상태 관련 예외
 */
public class UserStatusException extends RuntimeException{
    public UserStatusException(String message) {
        super(message);
    }
}
