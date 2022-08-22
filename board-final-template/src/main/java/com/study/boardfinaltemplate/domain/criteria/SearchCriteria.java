package com.study.boardfinaltemplate.domain.criteria;

import lombok.Builder;
import lombok.Getter;

/**
 * Board의 검색 조건 가지는 클래스
 */
@Getter
public class SearchCriteria {

    private String dateCreatedFrom;
    private String dateCreatedTo;
    private Integer categorySeq;
    private String text;
    private Integer curPage;

    @Builder
    public SearchCriteria(String dateCreatedFrom,
                          String dateCreatedTo,
                          Integer categorySeq,
                          String text,
                          Integer curPage) {

        this.dateCreatedFrom = dateCreatedFrom;
        this.dateCreatedTo = dateCreatedTo;
        this.categorySeq = categorySeq;
        this.text = text;
        this.curPage = curPage;
    }

}
