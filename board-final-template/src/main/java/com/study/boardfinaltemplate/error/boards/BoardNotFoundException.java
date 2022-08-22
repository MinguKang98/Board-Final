package com.study.boardfinaltemplate.error.boards;

import com.study.boardfinaltemplate.error.common.Messages;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;

/**
 * 존재하지 않는 Board 접근 시 throw
 */
public class BoardNotFoundException extends ResourceNotFoundException {
    public BoardNotFoundException() {
        super(Messages.NO_BOARD_MESSAGE);
    }
}
