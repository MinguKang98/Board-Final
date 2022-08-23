package com.study.boardfinalback.dto.boards;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글의 총 갯수와 페이징 된 게시글 목록을 보여주는 DTO
 */
@Getter
@Setter
@Builder
public class BoardListResponse {

    private int totalBoardCount;
    private List<BoardWithUserAndCategoryDto> boardList;

}
