package com.study.boardfinalback.repository.boards;

import com.study.boardfinalback.dto.boards.BoardWithUserAndCategoryDto;
import com.study.boardfinalback.domain.criteria.SearchPagingCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * join된 boards 테이블 관련 쿼리 결과를 Board DTO 클래스에 매핑하는 interface
 */
@Mapper
@Repository
public interface BoardQueryRepository {

    /**
     * 검색 조건과 페이징 조건에 해당하는 FreeBoard를 User, Category와 join하여 List 형태로 return
     * 
     * @param searchPagingCriteria : 검색 기준
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    List<BoardWithUserAndCategoryDto> getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    /**
     * 검색 조건과 페이징 조건에 해당하는 NotifyBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchPagingCriteria : 검색 기준
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    List<BoardWithUserAndCategoryDto> getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    /**
     * 검색 조건과 페이징 조건에 해당하는 MemberBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchPagingCriteria : 검색 기준
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    List<BoardWithUserAndCategoryDto> getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

    /**
     * 검색 조건과 페이징 조건에 해당하는 NewsBoard를 User, Category와 join하여 List 형태로 return
     *
     * @param searchPagingCriteria : 검색 기준
     * @return : 조건에 해당하는 BoardWithUserAndCategoryDto List
     */
    List<BoardWithUserAndCategoryDto> getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(SearchPagingCriteria searchPagingCriteria);

}
