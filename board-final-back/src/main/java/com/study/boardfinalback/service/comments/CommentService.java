package com.study.boardfinalback.service.comments;

import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.error.comments.CommentNotFoundException;
import com.study.boardfinalback.repository.boards.BoardRepository;
import com.study.boardfinalback.repository.comments.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public List<Comment> getCommentListByBoardSeq(int boardSeq) {

        return commentRepository.getCommentListByBoardSeq(boardSeq);
    }

    public Comment getCommentBySeq(int commentSeq) {

        return commentRepository.getCommentBySeq(commentSeq)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional
    public int addComment(Comment comment) {

        commentRepository.addComment(comment);
        boardRepository.increaseCommentCount(comment.getBoardSeq());

        return comment.getCommentSeq();
    }

    @Transactional
    public void updateComment(Comment comment) {

        commentRepository.updateComment(comment);
    }

    @Transactional
    public void deleteComment(int commentSeq, int boardSeq) {

        commentRepository.deleteComment(commentSeq);
        boardRepository.decreaseCommentCount(boardSeq);
    }

}
