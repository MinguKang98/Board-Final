package com.study.boardfinalback.restcontroller;

import com.study.boardfinalback.annotation.CurrentUser;
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
     * 검색 조건에 해당하는 FreeBoard의 총 갯수와 FreeBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardListDto
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<BoardListDto> freeBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return ResponseEntity.ok(boardListDto);
    }

    /**
     * 검색 조건에 해당하는 NotifyBoard의 총 갯수와 NotifyBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardListDto
     */
    @GetMapping("/api/boards/notify")
    public ResponseEntity<BoardListDto> notifyBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return ResponseEntity.ok(boardListDto);
    }

    /**
     * 검색 조건에 해당하는 MemberBoard의 총 갯수와 MemberBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardListDto
     */
    @GetMapping("/api/boards/member")
    public ResponseEntity<BoardListDto> memberBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return ResponseEntity.ok(boardListDto);
    }

    /**
     * 검색 조건에 해당하는 NewsBoard의 총 갯수와 NewsBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardListDto
     */
    @GetMapping("/api/boards/news")
    public ResponseEntity<BoardListDto> newsBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return ResponseEntity.ok(boardListDto);
    }

    /**
     * 유효성 검사 후 게시글 등록 정보를 사용하여 게시글 등록
     *
     * @param boardType     : 게시글 종류
     * @param currentUser   : 현재 로그인한 유저
     * @param boardWriteDto : 게시글 등록 정보 담긴 DTO
     * @return : void
     * @throws IOException
     */
    @PostMapping("/api/boards/{type}")
    public ResponseEntity write(@PathVariable("type") BoardType boardType,
                                @CurrentUser User currentUser,
                                @Valid BoardWriteDto boardWriteDto) throws IOException {

        Board board = Board.builder()
                .title(boardWriteDto.getTitle())
                .content(boardWriteDto.getContent())
                .fileExist(boardWriteDto.isFileExist())
                .boardType(boardType)
                .userSeq(currentUser.getUserSeq())
                .categorySeq(
                        (boardType == BoardType.NOTIFY || boardType == BoardType.NEWS) ? 1 : boardWriteDto.getCategorySeq()
                )
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardWriteDto.getFiles());

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
     * 입력받은 boardSeq을 가지는 FreeBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : Board
     */
    @GetMapping("/api/boards/free/{boardSeq}")
    public ResponseEntity<Board> freeBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.FREE) {
            throw new BoardTypeNotMatchException();
        }

        return ResponseEntity.ok(board);
    }

    /**
     * 입력받은 boardSeq을 가지는 NotifyBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : Board
     */
    @GetMapping("/api/boards/notify/{boardSeq}")
    public ResponseEntity<Board> notifyBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.NOTIFY) {
            throw new BoardTypeNotMatchException();
        }

        return ResponseEntity.ok(board);
    }

    /**
     * 입력받은 boardSeq을 가지는 NewsBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : Board
     */
    @GetMapping("/api/boards/news/{boardSeq}")
    public ResponseEntity<Board> newsBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.NEWS) {
            throw new BoardTypeNotMatchException();
        }

        return ResponseEntity.ok(board);
    }

    /**
     * 입력받은 boardSeq을 가지는 NewsBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : Board
     */
    @GetMapping("/api/boards/member/{boardSeq}")
    public ResponseEntity<Board> memberBoardDetail(@PathVariable("boardSeq") int boardSeq,
                                                   @CurrentUser User currentUser) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.MEMBER) {
            throw new BoardTypeNotMatchException();
        }

        return ResponseEntity.ok(board);
    }


    /**
     * 유효성 검사 후 게시글 수정 정보를 사용하여 게시글 수정
     *
     * @param boardType      : 게시글 종류
     * @param boardSeq       : 수정할 게시글의 boardSeq
     * @param currentUser    : 현재 사용자
     * @param boardModifyDto : 게시글 수정 정보 담긴 DTO
     * @return : void
     * @throws IOException
     */
    @PutMapping("/api/boards/{type}/{boardSeq}")
    public ResponseEntity modify(@PathVariable("type") BoardType boardType,
                                 @PathVariable("boardSeq") int boardSeq,
                                 @CurrentUser User currentUser,
                                 @Valid BoardModifyDto boardModifyDto) throws IOException {

        Board originBoard = boardService.getBoardBySeq(boardSeq);
        if (originBoard.getBoardType() != boardType) {
            throw new BoardTypeNotMatchException();
        }
        UserUtils.checkAuthorization(currentUser, originBoard.getUserSeq());

        Board newBoard = Board.builder()
                .boardSeq(boardSeq)
                .title(boardModifyDto.getTitle())
                .content(boardModifyDto.getContent())
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardModifyDto.getFiles(), boardSeq);
        List<File> deleteFileList = boardModifyDto.getDeleteFiles().stream()
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
     * 검색 조건에 해당하는 FreeBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardCountDto
     */
    @GetMapping("/api/boards/free/count")
    public ResponseEntity<BoardCountDto> freeBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return ResponseEntity.ok(boardCountDto);
    }

    /**
     * 검색 조건에 해당하는 NotifyBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardCountDto
     */
    @GetMapping("/api/boards/notify/count")
    public ResponseEntity<BoardCountDto> notifyBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return ResponseEntity.ok(boardCountDto);
    }

    /**
     * 검색 조건에 해당하는 MemberBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardCountDto
     */
    @GetMapping("/api/boards/member/count")
    public ResponseEntity<BoardCountDto> memberBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return ResponseEntity.ok(boardCountDto);
    }

    /**
     * 검색 조건에 해당하는 NewsBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : BoardCountDto
     */
    @GetMapping("/api/boards/news/count")
    public ResponseEntity<BoardCountDto> newsBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return ResponseEntity.ok(boardCountDto);
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
