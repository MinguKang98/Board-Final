package com.study.boardfinalback.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 게시글 클래스
 */
@Getter
@Alias(value = "Board")
@NoArgsConstructor
public class Board {

    int boardSeq;
    LocalDateTime dateCreated;
    LocalDateTime dateUpdated;
    String title;
    String content;
    int visitCount;
    int commentCount;
    Boolean fileExist;
    int userSeq;
    int categorySeq;

    @Builder
    public Board(int boardSeq,
                 String title,
                 String content,
                 int visitCount,
                 int commentCount,
                 Boolean fileExist,
                 int userSeq,
                 int categorySeq) {

        this.boardSeq = boardSeq;
        this.title = title;
        this.content = content;
        this.visitCount = visitCount;
        this.commentCount = commentCount;
        this.fileExist = fileExist;
        this.userSeq = userSeq;
        this.categorySeq = categorySeq;
    }

}
