package com.study.boardfinalback.dto.comments;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 댓글 내용 담는 DTO
 */
@Getter
@Setter
public class CommentRequest {

    @Size(min = 3, max = 200, message = "댓글은 3글자에서 200글자 사이여야 합니다.")
    @NotBlank(message = "댓글을 입력하세요")
    private String content;

}
