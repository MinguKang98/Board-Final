package com.study.boardfinalback.repository;

import com.study.boardfinalback.apiDto.comments.CommentWithUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * join된 comments 테이블 관련 쿼리 결과를 Comment DTO 클래스에 매핑하는 interface
 */
@Mapper
@Repository
public interface CommentQueryRepository {

    /**
     * 입력받은 boardSeq에 해당하는 Comment를 User와 join하여 List 형태로 return
     *
     * @param boardSeq : return 할 Comment의 boardSeq
     * @return : 입력받은 boardSeq 가지는 CommentWithUserDto List
     */
    List<CommentWithUserDto> getCommentWithUserDtoListByBoardSeq(int boardSeq);

}
