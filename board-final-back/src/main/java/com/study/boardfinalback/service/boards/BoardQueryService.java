package com.study.boardfinalback.service.boards;

import com.study.boardfinalback.apiDto.boards.BoardWithUserAndCategoryDto;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.criteria.SearchPagingCriteria;
import com.study.boardfinalback.repository.boards.BoardQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * join된 Board 클래스 관련 비즈니스 로직 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardQueryRepository boardQueryRepository;

    /**
     * 검색 조건과 페이징 조건에 해당하는 FreeBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    public List<BoardWithUserAndCategoryDto> getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(
            SearchCriteria searchCriteria,
            PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardQueryRepository.getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건과 페이징 조건에 해당하는 NotifyBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    public List<BoardWithUserAndCategoryDto> getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(
            SearchCriteria searchCriteria,
            PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardQueryRepository.getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건과 페이징 조건에 해당하는 MemberBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    public List<BoardWithUserAndCategoryDto> getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(
            SearchCriteria searchCriteria,
            PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardQueryRepository.getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건과 페이징 조건에 해당하는 NewsBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    public List<BoardWithUserAndCategoryDto> getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(
            SearchCriteria searchCriteria,
            PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardQueryRepository.getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchPagingCriteria);
    }

}
