package com.study.boardfinalback.restcontroller;

import com.study.boardfinalback.annotation.CurrentUser;
import com.study.boardfinalback.annotation.LoginRequired;
import com.study.boardfinalback.domain.users.UserRole;
import com.study.boardfinalback.dto.boards.*;
import com.study.boardfinalback.domain.*;
import com.study.boardfinalback.domain.boards.Board;
import com.study.boardfinalback.domain.boards.BoardType;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.users.User;
import com.study.boardfinalback.error.boards.BoardTypeNotMatchException;
import com.study.boardfinalback.service.boards.BoardQueryService;
import com.study.boardfinalback.service.boards.BoardService;
import com.study.boardfinalback.service.FileService;
import com.study.boardfinalback.utils.FileUtils;
import com.study.boardfinalback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Board 관련 api 요청 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final FileService fileService;
    private final FileUtils fileUtils;


    /**
     * String type을 BoardType 으로 매핑
     *
     * @param webDataBinder
     */
    @InitBinder(value = "type")
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(BoardType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(BoardType.getBoardType(text));
            }
        });
    }

    /**
     * 검색 조건에 해당하는 Board의 총 갯수와 Board List 담긴 BoardListDto return
     *
     * @param boardType      : 게시글 종류
     * @param searchCriteria : 검색 조건
     * @return : totalBoardCount and BoardWithUserAndCategoryDto List
     */
    @GetMapping("/api/boards/{type}")
    public ResponseEntity<BoardListResponse> boardPagingList(
            @PathVariable("type") BoardType boardType,
            SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalBoardCount(boardType, searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getBoardDtoList(boardType, searchCriteria, pagingCriteria);

        BoardListResponse boardListResponse = BoardListResponse.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return ResponseEntity.ok(boardListResponse);
    }


    /**
     * 유효성 검사 후 게시글 등록 정보를 사용하여 게시글 등록
     *
     * @param boardType         : 게시글 종류
     * @param currentUser       : 현재 로그인한 유저
     * @param boardWriteRequest : 게시글 등록 정보 담긴 DTO
     * @return : void
     * @throws IOException
     */
    @LoginRequired
    @PostMapping("/api/boards/{type}")
    public ResponseEntity write(@PathVariable("type") BoardType boardType,
                                @CurrentUser User currentUser,
                                @Valid BoardWriteRequest boardWriteRequest) throws IOException {

        if ((boardType == BoardType.NOTIFY || boardType == BoardType.NEWS) && currentUser.getRole() != UserRole.ROLE_ADMIN) {
            throw new AuthenticationException();
        }

        Board board = Board.builder()
                .title(boardWriteRequest.getTitle())
                .content(boardWriteRequest.getContent())
                .fileExist(boardWriteRequest.isFileExist())
                .boardType(boardType)
                .userSeq(currentUser.getUserSeq())
                .categorySeq(
                        (boardType == BoardType.NOTIFY || boardType == BoardType.NEWS) ? 1 : boardWriteRequest.getCategorySeq()
                )
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardWriteRequest.getFiles());

        int boardSeq = boardService.addBoard(board, addFileList);
        log.info("게시글이 생성되었습니다. boardSeq={}", boardSeq);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{seq}")
                .buildAndExpand(boardSeq)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * 입력받은 boardSeq 가지는 board return
     *
     * @param boardType : 게시글 종류
     * @param boardSeq  : return 할 게시글의 boardSeq
     * @return : Board
     */
    @GetMapping(value = {"/api/boards/{type}/{boardSeq}"})
    public ResponseEntity<Board> boardDetail(@PathVariable("type") BoardType boardType,
                                             @PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            throw new BoardTypeNotMatchException();
        }


        return ResponseEntity.ok(board);
    }


    /**
     * 입력받은 boardSeq을 가지는 member board return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : Board
     */
    @LoginRequired
    @GetMapping("/api/boards/member/{boardSeq}")
    public ResponseEntity<Board> memberBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.MEMBER) {
            throw new BoardTypeNotMatchException();
        }

        return ResponseEntity.ok(board);
    }


    /**
     * 유효성 검사 후 게시글 수정 정보를 사용하여 게시글 수정
     *
     * @param boardType          : 게시글 종류
     * @param boardSeq           : 수정할 게시글의 boardSeq
     * @param currentUser        : 현재 사용자
     * @param boardModifyRequest : 게시글 수정 정보 담긴 DTO
     * @return : void
     * @throws IOException
     */
    @LoginRequired
    @PutMapping("/api/boards/{type}/{boardSeq}")
    public ResponseEntity modify(@PathVariable("type") BoardType boardType,
                                 @PathVariable("boardSeq") int boardSeq,
                                 @CurrentUser User currentUser,
                                 @Valid BoardModifyRequest boardModifyRequest) throws IOException {

        Board originBoard = boardService.getBoardBySeq(boardSeq);
        if (originBoard.getBoardType() != boardType) {
            throw new BoardTypeNotMatchException();
        }
        UserUtils.checkAuthorization(currentUser, originBoard.getUserSeq());

        Board newBoard = Board.builder()
                .boardSeq(boardSeq)
                .title(boardModifyRequest.getTitle())
                .content(boardModifyRequest.getContent())
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardModifyRequest.getAddFiles(), boardSeq);
        List<File> deleteFileList = boardModifyRequest.getDeleteFiles().stream()
                .map(f -> fileService.getFileBySeq(f))
                .collect(Collectors.toList());

        boardService.updateBoard(newBoard, addFileList, deleteFileList);
        log.info("게시글이 수정되었습니다. boardSeq={}", boardSeq);

        return ResponseEntity.noContent().build();
    }

    /**
     * 입력받은 boardSeq 가지는 Board 삭제
     *
     * @param boardType   : 게시글 종류
     * @param boardSeq    : 삭제할 게시글의 boardSeq
     * @param currentUser : 현재 사용자
     * @return : void
     */
    @LoginRequired
    @DeleteMapping("/api/boards/{type}/{boardSeq}")
    public ResponseEntity delete(@PathVariable("type") BoardType boardType,
                                 @PathVariable("boardSeq") int boardSeq,
                                 @CurrentUser User currentUser) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            throw new BoardTypeNotMatchException();
        }
        UserUtils.checkAuthorization(currentUser, board.getUserSeq());

        boardService.deleteBoard(boardSeq);
        log.info("게시글이 삭제되었습니다. boardSeq={}", boardSeq);

        return ResponseEntity.noContent().build();
    }


    /**
     * 검색 조건에 해당하는 Board 총 갯수 return
     *
     * @param boardType      : 게시글 종류
     * @param searchCriteria : 검색 조건
     * @return : total board count
     */
    @GetMapping("/api/boards/{type}/count")
    public ResponseEntity<BoardCountResponse> boardCount(@PathVariable("type") BoardType boardType,
                                                         SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalBoardCount(boardType, searchCriteria);
        BoardCountResponse boardCountResponse = BoardCountResponse.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return ResponseEntity.ok(boardCountResponse);
    }


    /**
     * 입력받은 boardSeq을 가지는 Board의 visitCount 1 중가
     *
     * @param boardSeq : visitCount를 증가 시킬 Board의 boardSeq
     * @return : void
     */
    @PutMapping("/api/boards/{boardSeq}/visit-count")
    public ResponseEntity updateVisitCount(@PathVariable("boardSeq") int boardSeq) {

        boardService.updateVisitCount(boardSeq);

        return ResponseEntity.noContent().build();
    }

}
