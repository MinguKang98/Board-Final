package com.study.boardfinalback.error.boards;

import com.study.boardfinalback.error.common.Messages;

/**
 * /boards/{type}/{boardSeq} 에서 type이 지원하지 경우 throw BoardTypeException
 */
public class BoardTypeNotFoundException extends RuntimeException{

    public BoardTypeNotFoundException() {
        super(Messages.NO_BOARD_TYPE_MESSAGE);
    }

}
