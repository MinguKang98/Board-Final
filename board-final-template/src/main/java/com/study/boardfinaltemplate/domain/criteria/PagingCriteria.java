package com.study.boardfinaltemplate.domain.criteria;

import lombok.Builder;
import lombok.Getter;

/**
 * Board의 페이징 조건 가지는 클래스
 */
@Getter
public class PagingCriteria {

    private int curPage;
    private int boardCountPerPage = 10;
    private int pageSize = 10;
    private int totalBoardCount;
    private int firstBoardNum;
    private int lastBoardNum;
    private int totalPageCount;
    private int firstPage;
    private int lastPage;

    @Builder
    public PagingCriteria(Integer curPage, int totalBoardCount) {
        this.curPage = (curPage == null) ? 1 : curPage;
        this.totalBoardCount = totalBoardCount;
        this.pageSetting();
    }

    public void pageSetting() {
        totalPageCount = (totalBoardCount - 1) / boardCountPerPage + 1;

        firstBoardNum = (curPage - 1) * boardCountPerPage;
        lastBoardNum = firstBoardNum + boardCountPerPage - 1;
        if (lastBoardNum > totalBoardCount) lastBoardNum = totalBoardCount - 1;

        firstPage = (curPage - 1) / pageSize * pageSize + 1;
        lastPage = firstPage + pageSize - 1;
        if (lastPage > totalPageCount) lastPage = totalPageCount;
    }

}
