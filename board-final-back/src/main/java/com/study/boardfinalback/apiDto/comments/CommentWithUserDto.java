package com.study.boardfinalback.apiDto.comments;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 댓글 정보와 유저 아이디가 담긴 DTO
 */
@Getter
@Setter
public class CommentWithUserDto {

    private int commentSeq;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String content;
    private int userSeq;
    private String userId;

}
