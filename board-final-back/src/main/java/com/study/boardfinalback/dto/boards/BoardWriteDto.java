package com.study.boardfinalback.dto.boards;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Board 등록 시 사용되는 DTO
 */
@Getter
@Setter
public class BoardWriteDto {

    @Size(min = 3, max = 200)
    private String title;

    @Size(min = 3, max = 2000)
    private String content;

    @NotBlank(message = "유저는 필수 입력 사항입니다.")
    private int userSeq;

    @NotBlank(message = "카테고리는 필수 입력 사항입니다.")
    private int categorySeq;

}
