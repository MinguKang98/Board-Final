package com.study.boardfinalback.error.boards;

import com.study.boardfinalback.error.common.Messages;
import com.study.boardfinalback.error.common.ResourceNotFoundException;

public class BoardNotFoundException extends ResourceNotFoundException {
    public BoardNotFoundException() {
        super(Messages.NO_BOARD_MESSAGE);
    }
}
