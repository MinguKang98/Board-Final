package com.study.boardfinaltemplate.dto.comments;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

/**
 * 댓글 삭제 시 쓰이는 DTO
 */
@Getter
@Setter
public class CommentDeleteDto {

    @Positive(message = "댓글 정보는 필수 사항입니다.")
    private int commentSeq;

    @Positive(message = "게시글 정보는 필수 사항입니다.")
    private int boardSeq;

}
