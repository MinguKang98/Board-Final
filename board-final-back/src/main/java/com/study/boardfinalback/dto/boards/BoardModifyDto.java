package com.study.boardfinalback.dto.boards;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * Board 수정 시 사용되는 DTO
 */
@Getter
@Setter
public class BoardModifyDto {

    @Size(min = 3, max = 100, message = "제목은 3글자에서 100글자 사이여야 합니다.")
    private String title;

    @Size(min = 3, max = 2000, message = "본문은 3글자에서 2000글자 사이여야 합니다.")
    private String content;

}
