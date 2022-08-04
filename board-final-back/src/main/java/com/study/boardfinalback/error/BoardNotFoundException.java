package com.study.boardfinalback.error;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException() {
        super(Messages.NO_BOARD_MESSAGE);
    }
}
