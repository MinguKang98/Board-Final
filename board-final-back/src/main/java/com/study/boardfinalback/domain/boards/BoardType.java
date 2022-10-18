package com.study.boardfinalback.domain.boards;

import com.study.boardfinalback.domain.users.UserRole;
import com.study.boardfinalback.error.boards.BoardTypeNotFoundException;

import java.util.Arrays;

/**
 * 게시글의 종류를 나타내는 클래스. 게시글의 이름, 읽기 권한, 쓰기 권한에 대한 정보를 가지고 있다.
 */
public enum BoardType {

    NOTIFY("notify", UserRole.ROLE_GUEST, UserRole.ROLE_ADMIN),
    FREE("free", UserRole.ROLE_GUEST, UserRole.ROLE_MEMBER),
    MEMBER("member", UserRole.ROLE_MEMBER, UserRole.ROLE_MEMBER),
    NEWS("news", UserRole.ROLE_GUEST, UserRole.ROLE_ADMIN);

    private String type;
    private UserRole readPermission;
    private UserRole writePermission;

    BoardType(String type, UserRole readPermission, UserRole writePermission) {
        this.type = type;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }

    public String getType() {
        return this.type;
    }

    public UserRole getReadPermission() {
        return readPermission;
    }

    public UserRole getWritePermission() {
        return writePermission;
    }

    public static BoardType getBoardType(String type) {

        return Arrays.stream(BoardType.values())
                .filter(boardType -> boardType.getType().equals(type))
                .findAny()
                .orElseThrow(BoardTypeNotFoundException::new);
    }

    public boolean isReadPermissionGuest() {
        return this.getReadPermission() == UserRole.ROLE_GUEST;
    }

    public boolean isReadPermissionMember() {
        return this.getReadPermission() == UserRole.ROLE_MEMBER;
    }

    public boolean isReadPermissionAdmin() {
        return this.getReadPermission() == UserRole.ROLE_ADMIN;
    }

    public boolean isWritePermissionGuest() {
        return this.getWritePermission() == UserRole.ROLE_GUEST;
    }

    public boolean isWritePermissionMember() {
        return this.getWritePermission() == UserRole.ROLE_MEMBER;
    }

    public boolean isWritePermissionAdmin() {
        return this.getWritePermission() == UserRole.ROLE_ADMIN;
    }

}
