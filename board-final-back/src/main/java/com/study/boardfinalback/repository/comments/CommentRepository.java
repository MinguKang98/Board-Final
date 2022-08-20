package com.study.boardfinalback.repository.comments;

import com.study.boardfinalback.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommentRepository {

    List<Comment> getCommentListByBoardSeq(int boardSeq);

    Optional<Comment> getCommentBySeq(int commentSeq);

    void addComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(int commentSeq);

}
