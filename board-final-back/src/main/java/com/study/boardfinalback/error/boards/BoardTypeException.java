package com.study.boardfinalback.error.boards;

import com.study.boardfinalback.error.common.Messages;

/**
 * /api/boards/{type}/{boardSeq} boardSeq에 해당하는 Board의 type과 입력받은 type이 다른 경우 throw BoardTypeException
 */
public class BoardTypeException extends RuntimeException{

    public BoardTypeException() {
        super(Messages.BOARD_WRONG_TYPE_MESSAGE);
    }

}
