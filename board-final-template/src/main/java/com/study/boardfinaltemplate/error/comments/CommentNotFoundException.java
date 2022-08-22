package com.study.boardfinaltemplate.error.comments;

import com.study.boardfinaltemplate.error.common.Messages;
import com.study.boardfinaltemplate.error.common.ResourceNotFoundException;

/**
 * 존재하지 않는 Comment 접근 시 throw
 */
public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super(Messages.NO_COMMENT_MESSAGE);
    }
}
