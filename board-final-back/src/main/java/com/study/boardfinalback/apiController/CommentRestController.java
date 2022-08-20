package com.study.boardfinalback.apiController;

import com.study.boardfinalback.annotation.CurrentUser;
import com.study.boardfinalback.apiDto.comments.CommentContentDto;
import com.study.boardfinalback.apiDto.comments.CommentWithUserDto;
import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.service.boards.BoardService;
import com.study.boardfinalback.service.comments.CommentQueryService;
import com.study.boardfinalback.service.comments.CommentService;
import com.study.boardfinalback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Comment 관련 api 요청 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;
    private final CommentQueryService commentQueryService;
    private final BoardService boardService;

    /**
     * 입력받은 boardSeq을 가지는 Board의 모든 Comment를 가져온다
     *
     * @param boardSeq : Comment를 가져올 Board의 boardSeq
     * @return : 200 with CommentWithUserDto List
     */
    @GetMapping("/api/boards/{boardSeq}/comments")
    public ResponseEntity<List<CommentWithUserDto>> comments(@PathVariable("boardSeq") int boardSeq) {

        boardService.getBoardBySeq(boardSeq);

        List<CommentWithUserDto> commentWithUserDtoList = commentQueryService.getCommentWithUserDtoListByBoardSeq(boardSeq);
        return new ResponseEntity(commentWithUserDtoList, HttpStatus.OK);
    }

    /**
     * 입력받은 댓글 정보를 사용해 댓글을 작성한다.
     *
     * @param boardSeq : 댓글을 작성할 게시글의 boardSeq
     * @param commentContentDto : 댓글 내용 담긴 DTO
     * @param currentUser : 현재 로그인한 유저
     * @return : 201
     */
    @PostMapping("/api/boards/{boardSeq}/comments")
    public ResponseEntity write(@PathVariable("boardSeq") int boardSeq,
                                @Valid CommentContentDto commentContentDto,
                                @CurrentUser User currentUser) {

        boardService.getBoardBySeq(boardSeq);

        Comment comment = Comment.builder()
                .content(commentContentDto.getContent())
                .boardSeq(boardSeq)
                .userSeq(currentUser.getUserSeq())
                .build();

        int commentSeq = commentService.addComment(comment);
        log.info("댓글이 생성되었습니다. commentSeq={}", commentSeq);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment의 정보를 가져온다.
     *
     * @param commentSeq : 가져올 Comment의 commentSeq
     * @return : 200 with Comment
     */
    @GetMapping("/api/comments/{commentSeq}")
    public ResponseEntity<Comment> commentDetail(@PathVariable("commentSeq") int commentSeq) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment를 수정한다.
     *
     * @param commentSeq : 수정할 Comment의 commentSeq
     * @param commentContentDto : 댓글 내용 담긴 DTO
     * @param currentUser : 현재 로그인한 유저
     * @return : 204
     */
    @PutMapping("/api/comments/{commentSeq}")
    public ResponseEntity modify(@PathVariable("commentSeq") int commentSeq,
                                 @Valid CommentContentDto commentContentDto,
                                 @CurrentUser User currentUser) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        UserUtils.checkAuthorization(currentUser, comment.getUserSeq());

        Comment newComment = Comment.builder()
                .commentSeq(commentSeq)
                .content(commentContentDto.getContent())
                .build();

        commentService.updateComment(newComment);
        log.info("댓글이 수정되었습니다. commentSeq={}", commentSeq);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment를 삭제한다.
     *
     * @param commentSeq : 삭제할 Comment의 commentSeq
     * @param currentUser : 현재 로그인한 유저
     * @return : 204
     */
    @DeleteMapping("/api/comments/{commentSeq}")
    public ResponseEntity delete(@PathVariable("commentSeq") int commentSeq,
                                 @CurrentUser User currentUser) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        UserUtils.checkAuthorization(currentUser, comment.getUserSeq());

        commentService.deleteComment(commentSeq, comment.getBoardSeq());
        log.info("댓글이 삭제되었습니다. commentSeq={}", commentSeq);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
