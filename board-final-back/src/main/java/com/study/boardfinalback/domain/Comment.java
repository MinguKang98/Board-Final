package com.study.boardfinalback.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 게시글의 댓글 클래스
 */
@Getter
@Alias(value = "Comment")
@NoArgsConstructor
public class Comment {

    private int commentSeq;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String content;
    private int boardSeq;
    private int userSeq;

    @Builder
    public Comment(int commentSeq,
                   String content,
                   int boardSeq,
                   int userSeq) {

        this.commentSeq = commentSeq;
        this.content = content;
        this.boardSeq = boardSeq;
        this.userSeq = userSeq;
    }

}
