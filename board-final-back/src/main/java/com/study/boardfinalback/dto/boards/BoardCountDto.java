package com.study.boardfinalback.dto.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 총 갯수를 보여주는 DTO
 */
@Getter
@Setter
@Builder
public class BoardCountDto {

    private int totalBoardCount;

}
