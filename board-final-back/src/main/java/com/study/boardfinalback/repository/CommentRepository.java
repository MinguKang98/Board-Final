package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommentRepository {

    List<Comment> getCommentListByBoardSeq(int BoardSeq);

    Optional<Comment> getCommentBySeq(int CommentSeq);

    void addComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(int commentSeq);

}
