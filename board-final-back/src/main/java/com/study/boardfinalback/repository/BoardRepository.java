package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Board;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.criteria.SearchPagingCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BoardRepository {

    List<Board> getBoardList();

    List<Board> getBoardListByCategorySeq(int categorySeq);

    int getTotalBoardCountBySearchCriteria(SearchCriteria searchCriteria);

    List<Board> getBoardListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    Optional<Board> getBoardBySeq(int boardSeq);

    void addBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(int boardSeq);

    void updateVisitCount(int boardSeq);

    void increaseCommentCount(int boardSeq);

    void decreaseCommentCount(int boardSeq);

}
