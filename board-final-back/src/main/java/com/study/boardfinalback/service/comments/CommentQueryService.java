package com.study.boardfinalback.service.comments;

import com.study.boardfinalback.apiDto.comments.CommentWithUserDto;
import com.study.boardfinalback.repository.comments.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * join된 Board 클래스 관련 비즈니스 로직 구현 클래스
 */
@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentQueryRepository commentQueryRepository;

    /**
     * 입력받은 boardSeq에 해당하는 Comment를 User와 join하여 List 형태로 return
     *
     * @param boardSeq : return 할 Comment의 boardSeq
     * @return : 입력받은 boardSeq 가지는 CommentWithUserDto List
     */
    public List<CommentWithUserDto> getCommentWithUserDtoListByBoardSeq(int boardSeq) {

        return commentQueryRepository.getCommentWithUserDtoListByBoardSeq(boardSeq);
    }

}
