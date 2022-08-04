package com.study.boardfinalback.error;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException() {
        super(Messages.NO_COMMENT_MESSAGE);
    }
}
