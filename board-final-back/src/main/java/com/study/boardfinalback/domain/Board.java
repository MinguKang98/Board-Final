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

    public Board updateBoard(String inputTitle,
                             String inputContent,
                             Boolean inputFileExist) {

        String originTitle = this.getTitle();
        String originContent = this.getContent();
        Boolean originFileExist = this.getFileExist();

        if (!originTitle.equals(inputTitle)) {
            this.title = inputTitle;
        }
        if (!originContent.equals(inputContent)) {
            this.content = inputContent;
        }
        if (originFileExist != inputFileExist) {
            this.fileExist = inputFileExist;
        }

        return this;
    }

}
