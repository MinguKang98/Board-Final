package com.study.boardfinalback.domain.boards;

import com.study.boardfinalback.error.boards.BoardTypeNotFoundException;

import java.util.Arrays;

public enum BoardType {

    NOTIFY("notify"),
    FREE("free"),
    MEMBER("member"),
    NEWS("news");

    private String type;

    BoardType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static BoardType getBoardType(String type) {

        return Arrays.stream(BoardType.values())
                .filter(boardType -> boardType.getType().equals(type))
                .findAny()
                .orElseThrow(BoardTypeNotFoundException::new);
    }

}
