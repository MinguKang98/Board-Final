package com.study.boardfinalback.restcontroller;

import com.study.boardfinalback.annotation.CurrentUser;
import com.study.boardfinalback.dto.comments.CommentRequest;
import com.study.boardfinalback.dto.comments.CommentWithUserDto;
import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.service.boards.BoardService;
import com.study.boardfinalback.service.comments.CommentQueryService;
import com.study.boardfinalback.service.comments.CommentService;
import com.study.boardfinalback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
     * @return : CommentWithUserDto List
     */
    @GetMapping("/api/boards/{boardSeq}/comments")
    public ResponseEntity<List<CommentWithUserDto>> comments(@PathVariable("boardSeq") int boardSeq) {

        boardService.getBoardBySeq(boardSeq);

        List<CommentWithUserDto> commentWithUserDtoList = commentQueryService.getCommentWithUserDtoListByBoardSeq(boardSeq);
        return ResponseEntity.ok(commentWithUserDtoList);
    }

    /**
     * 입력받은 댓글 정보를 사용해 댓글을 작성한다.
     *
     * @param boardSeq       : 댓글을 작성할 게시글의 boardSeq
     * @param commentRequest : 댓글 내용 담긴 DTO
     * @param currentUser    : 현재 로그인한 유저
     * @return : void
     */
    @PostMapping("/api/boards/{boardSeq}/comments")
    public ResponseEntity write(@PathVariable("boardSeq") int boardSeq,
                                @Valid @RequestBody CommentRequest commentRequest,
                                @CurrentUser User currentUser) {

        boardService.getBoardBySeq(boardSeq);

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .boardSeq(boardSeq)
                .userSeq(currentUser.getUserSeq())
                .build();

        int commentSeq = commentService.addComment(comment);
        log.info("댓글이 생성되었습니다. commentSeq={}", commentSeq);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/comments/{seq}")
                .buildAndExpand(commentSeq)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment의 정보를 가져온다.
     *
     * @param commentSeq : 가져올 Comment의 commentSeq
     * @return : Comment
     */
    @GetMapping("/api/comments/{commentSeq}")
    public ResponseEntity<Comment> commentDetail(@PathVariable("commentSeq") int commentSeq) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        return ResponseEntity.ok(comment);
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment를 수정한다.
     *
     * @param commentSeq     : 수정할 Comment의 commentSeq
     * @param commentRequest : 댓글 내용 담긴 DTO
     * @param currentUser    : 현재 로그인한 유저
     * @return : void
     */
    @PutMapping("/api/comments/{commentSeq}")
    public ResponseEntity modify(@PathVariable("commentSeq") int commentSeq,
                                 @Valid @RequestBody CommentRequest commentRequest,
                                 @CurrentUser User currentUser) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        UserUtils.checkAuthorization(currentUser, comment.getUserSeq());

        Comment newComment = Comment.builder()
                .commentSeq(commentSeq)
                .content(commentRequest.getContent())
                .build();

        commentService.updateComment(newComment);
        log.info("댓글이 수정되었습니다. commentSeq={}", commentSeq);

        return ResponseEntity.noContent().build();
    }

    /**
     * 입력받은 commentSeq을 가지는 Comment를 삭제한다.
     *
     * @param commentSeq  : 삭제할 Comment의 commentSeq
     * @param currentUser : 현재 로그인한 유저
     * @return : void
     */
    @DeleteMapping("/api/comments/{commentSeq}")
    public ResponseEntity delete(@PathVariable("commentSeq") int commentSeq,
                                 @CurrentUser User currentUser) {

        Comment comment = commentService.getCommentBySeq(commentSeq);
        UserUtils.checkAuthorization(currentUser, comment.getUserSeq());

        commentService.deleteComment(commentSeq);
        log.info("댓글이 삭제되었습니다. commentSeq={}", commentSeq);

        return ResponseEntity.noContent().build();
    }

}
