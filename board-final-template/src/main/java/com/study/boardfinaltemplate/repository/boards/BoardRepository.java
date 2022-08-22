package com.study.boardfinaltemplate.repository.boards;

import com.study.boardfinaltemplate.domain.boards.Board;
import com.study.boardfinaltemplate.domain.criteria.SearchCriteria;
import com.study.boardfinaltemplate.domain.criteria.SearchPagingCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BoardRepository {

    List<Board> getBoardList();

    int getTotalFreeBoardCountBySearchCriteria(SearchCriteria searchCriteria);

    List<Board> getFreeBoardListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    int getTotalNotifyBoardCountBySearchCriteria(SearchCriteria searchCriteria);

    List<Board> getNotifyBoardListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    int getTotalMemberBoardCountBySearchCriteria(SearchCriteria searchCriteria);

    List<Board> getMemberBoardListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    int getTotalNewsBoardCountBySearchCriteria(SearchCriteria searchCriteria);

    List<Board> getNewsBoardListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    Optional<Board> getBoardBySeq(int boardSeq);

    void addBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(int boardSeq);

    void updateVisitCount(int boardSeq);

    void increaseCommentCount(int boardSeq);

    void decreaseCommentCount(int boardSeq);

}
