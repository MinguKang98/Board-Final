package com.study.boardfinalback.error.comments;

import com.study.boardfinalback.error.common.Messages;
import com.study.boardfinalback.error.common.ResourceNotFoundException;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super(Messages.NO_COMMENT_MESSAGE);
    }
}
