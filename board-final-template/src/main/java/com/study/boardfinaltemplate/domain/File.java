package com.study.boardfinaltemplate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * 게시글 첨부 파일 클래스
 */
@Getter
@Alias(value = "File")
@NoArgsConstructor
public class File {

    private int fileSeq;
    private LocalDateTime dateCreated;
    private String originName;
    private String systemName;
    private String extension;
    private int boardSeq;

    @Builder
    public File(int fileSeq,
                String originName,
                String systemName,
                String extension,
                int boardSeq) {

        this.fileSeq = fileSeq;
        this.originName = originName;
        this.systemName = systemName;
        this.extension = extension;
        this.boardSeq = boardSeq;
    }

    public void setBoardSeq(int boardSeq) {
        this.boardSeq = boardSeq;
    }

}
