package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Board;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.criteria.SearchPagingCriteria;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void 게시글_모두_가져오기() throws Exception {
        ///when
        List<Board> boardList = boardRepository.getBoardList();

        //then
        assertThat(boardList.size()).isEqualTo(3);
    }

    @Test
    public void 게시글_카테고리_가져오기() throws Exception {
        //given
        int categorySeq = 2;

        ///when
        List<Board> boardList = boardRepository.getBoardListByCategorySeq(categorySeq);

        //then
        assertThat(boardList.size()).isEqualTo(2);
    }

    @Test
    public void 검색조건_게시글_총_갯수() throws Exception {
        //given
        SearchCriteria search = SearchCriteria.builder()
                .dateCreatedFrom("2022-07-01")
                .dateCreatedTo("2022-08-06")
                .categorySeq(2)
                .text("test")
                .curPage(1)
                .build();

        ///when
        int total = boardRepository.getTotalBoardCountBySearchCriteria(search);

        //then
        assertThat(total).isEqualTo(2);
    }

    @Test
    public void 검색조건_게시글_모두_가져오기() throws Exception {
        //given
        SearchCriteria search = SearchCriteria.builder()
                .dateCreatedFrom("2022-07-01")
                .dateCreatedTo("2022-08-06")
                .categorySeq(2)
                .text("test")
                .curPage(1)
                .build();
        int total = boardRepository.getTotalBoardCountBySearchCriteria(search);

        PagingCriteria paging = PagingCriteria.builder()
                .curPage(search.getCurPage())
                .totalBoardCount(total)
                .build();

        SearchPagingCriteria searchPaging = SearchPagingCriteria.builder()
                .dateCreatedFrom(search.getDateCreatedFrom())
                .dateCreatedTo(search.getDateCreatedTo())
                .categorySeq(search.getCategorySeq())
                .text(search.getText())
                .firstBoardNum(paging.getFirstBoardNum())
                .boardCountPerPage(paging.getBoardCountPerPage())
                .build();

        ///when
        List<Board> boardList = boardRepository.getBoardListBySearchPagingCriteria(searchPaging);

        //then
        assertThat(boardList.size()).isEqualTo(2);
    }

    @Disabled
    @Test
    public void 게시글_작성() throws Exception {
        //given
        Board board = Board.builder()
                .title("test 4")
                .content("test 4")
                .fileExist(false)
                .userSeq(2)
                .categorySeq(2)
                .build();

        ///when
        boardRepository.addBoard(board);
        int boardSeq = board.getBoardSeq();

        //then
        Optional<Board> board1 = boardRepository.getBoardBySeq(boardSeq);
        assertThat(board1.get().getTitle()).isEqualTo("test 4");
    }

    @Test
    public void 게시글_수정() throws Exception {
        //given
        Board board = Board.builder()
                .boardSeq(1)
                .title("test 123")
                .content("test 123")
                .fileExist(false)
                .build();

        ///when
        boardRepository.updateBoard(board);

        //then
        Optional<Board> board1 = boardRepository.getBoardBySeq(1);
        assertThat(board1.get().getTitle()).isEqualTo("test 123");
    }

    @Test
    public void 게시글_삭제() throws Exception {
        //given
        int boardSeq = 1;

        ///when
        boardRepository.deleteBoard(boardSeq);

        //then
        Optional<Board> board = boardRepository.getBoardBySeq(boardSeq);
        assertThat(board.isEmpty()).isTrue();
    }

    @Test
    public void 게시글_조회수_업데이트() throws Exception {
        //given
        int boardSeq = 1;
        Optional<Board> originBoard = boardRepository.getBoardBySeq(boardSeq);
        int visitCount = originBoard.get().getVisitCount();

        ///when
        boardRepository.updateVisitCount(boardSeq);

        //then
        Optional<Board> newBoard = boardRepository.getBoardBySeq(boardSeq);
        assertThat(newBoard.get().getVisitCount()).isEqualTo(visitCount + 1);
    }

    @Test
    public void 게시글_댓글수_업데이트() throws Exception {
        //given
        int boardSeq = 1;
        Optional<Board> originBoard = boardRepository.getBoardBySeq(boardSeq);
        int commentCount = originBoard.get().getCommentCount();

        ///when
        boardRepository.updateCommentCount(boardSeq);

        //then
        Optional<Board> newBoard = boardRepository.getBoardBySeq(boardSeq);
        assertThat(newBoard.get().getCommentCount()).isEqualTo(commentCount + 1);
    }

}