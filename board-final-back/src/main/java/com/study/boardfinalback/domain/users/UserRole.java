package com.study.boardfinalback.domain.users;


import java.util.Arrays;

/**
 * 유저 권한 클래스
 */
public enum UserRole {

    ROLE_GUEST("ROLE_GUEST"),
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String type;

    UserRole(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static UserRole getUserRole(String type) {

        return Arrays.stream(UserRole.values())
                .filter(boardType -> type.equals(boardType.getType()))
                .findAny()
                .orElse(UserRole.ROLE_GUEST);
    }

}
