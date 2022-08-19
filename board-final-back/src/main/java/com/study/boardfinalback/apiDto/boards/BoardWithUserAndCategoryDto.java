package com.study.boardfinalback.apiDto.boards;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 게시글 정보와 유저 아이디, 카테고리 이름이 담긴 DTO
 */
@Getter
@Setter
public class BoardWithUserAndCategoryDto {

    int boardSeq;
    LocalDateTime dateCreated;
    LocalDateTime dateUpdated;
    String title;
    int visitCount;
    int commentCount;
    Boolean fileExist;
    int userSeq;
    String userId;
    int categorySeq;
    String categoryName;

}
