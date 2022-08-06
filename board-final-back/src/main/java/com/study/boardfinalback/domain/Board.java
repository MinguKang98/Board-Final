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
    BoardType boardType;
    int userSeq;
    int categorySeq;

    @Builder
    public Board(int boardSeq,
                 String title,
                 String content,
                 int visitCount,
                 int commentCount,
                 Boolean fileExist,
                 BoardType boardType,
                 int userSeq,
                 int categorySeq) {

        this.boardSeq = boardSeq;
        this.title = title;
        this.content = content;
        this.visitCount = visitCount;
        this.commentCount = commentCount;
        this.fileExist = fileExist;
        this.boardType = boardType;
        this.userSeq = userSeq;
        this.categorySeq = categorySeq;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileExist(Boolean fileExist) {
        this.fileExist = fileExist;
    }

}
