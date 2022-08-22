package com.study.boardfinaltemplate.dto.comments;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * 댓글 수정에 쓰이는 DTO
 */
@Getter
@Setter
public class CommentModifyDto {

    @Positive(message = "댓글 정보는 필수 사항입니다.")
    private int commentSeq;

    @Positive(message = "게시글 정보는 필수 사항입니다.")
    private int boardSeq;
    
    @Size(min = 3, max = 200, message = "댓글은 3글자에서 200글자 사이여야 합니다.")
    private String content;

}
