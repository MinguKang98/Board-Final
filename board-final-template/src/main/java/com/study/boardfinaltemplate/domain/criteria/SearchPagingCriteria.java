package com.study.boardfinaltemplate.domain.criteria;

import lombok.Builder;

/**
 * Board의 검색 조건, 첫 게시글 번호, 노출되는 게시글 갯수 가지는 클래스
 */
public class SearchPagingCriteria {

    private String dateCreatedFrom;
    private String dateCreatedTo;
    private Integer categorySeq;
    private String text;
    private int firstBoardNum;
    private int boardCountPerPage;

    @Builder
    public SearchPagingCriteria(String dateCreatedFrom,
                                String dateCreatedTo,
                                Integer categorySeq,
                                String text,
                                int firstBoardNum,
                                int boardCountPerPage) {

        this.dateCreatedFrom = dateCreatedFrom;
        this.dateCreatedTo = dateCreatedTo;
        this.categorySeq = categorySeq;
        this.text = text;
        this.firstBoardNum = firstBoardNum;
        this.boardCountPerPage = boardCountPerPage;
    }

}
