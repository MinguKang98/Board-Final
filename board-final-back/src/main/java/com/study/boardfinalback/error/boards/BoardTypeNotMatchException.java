package com.study.boardfinalback.error.boards;

import com.study.boardfinalback.error.common.Messages;

/**
 * /api/boards/{type}/{boardSeq} boardSeq에 해당하는 Board의 type과 입력받은 type이 다른 경우 throw BoardTypeException
 */
public class BoardTypeNotMatchException extends RuntimeException{

    public BoardTypeNotMatchException() {
        super(Messages.BOARD_TYPE_NOT_MATCH_MESSAGE);
    }

}
