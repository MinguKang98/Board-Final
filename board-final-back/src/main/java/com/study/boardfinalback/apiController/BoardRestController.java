package com.study.boardfinalback.apiController;

import com.study.boardfinalback.apiDto.boards.*;
import com.study.boardfinalback.domain.*;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.error.boards.BoardTypeException;
import com.study.boardfinalback.service.BoardQueryService;
import com.study.boardfinalback.service.BoardService;
import com.study.boardfinalback.service.FileService;
import com.study.boardfinalback.utils.FileUtils;
import com.study.boardfinalback.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    private Map<String, BoardType> boardTypeMap = Map.of(
            "notify", BoardType.NOTIFY,
            "free", BoardType.FREE,
            "member", BoardType.MEMBER,
            "news", BoardType.NEWS
    );

    /**
     * 검색 조건에 해당하는 FreeBoard의 총 갯수와 FreeBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : 200 with BoardListDto
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<BoardListDto> freeBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return new ResponseEntity(boardListDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 NotifyBoard의 총 갯수와 NotifyBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : 200 with BoardListDto
     */
    @GetMapping("/api/boards/notify")
    public ResponseEntity<BoardListDto> notifyBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return new ResponseEntity(boardListDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 MemberBoard의 총 갯수와 MemberBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : 200 with BoardListDto
     */
    @GetMapping("/api/boards/member")
    public ResponseEntity<BoardListDto> memberBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return new ResponseEntity(boardListDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 NewsBoard의 총 갯수와 NewsBoard List 담긴 BoardListDto return
     *
     * @param searchCriteria : 검색 조건
     * @return : 200 with BoardListDto
     */
    @GetMapping("/api/boards/news")
    public ResponseEntity<BoardListDto> newsBoard(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardWithUserAndCategoryDtoList = boardQueryService
                .getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        BoardListDto boardListDto = BoardListDto.builder()
                .totalBoardCount(totalBoardCount)
                .boardList(boardWithUserAndCategoryDtoList)
                .build();
        return new ResponseEntity(boardListDto, HttpStatus.OK);
    }

    /**
     * 유효성 검사 후 게시글 등록 정보를 사용하여 게시글 등록
     *
     * @param type : 게시글 종류
     * @param currentUser : 현재 로그인한 유저
     * @param boardWriteDto : 게시글 등록 정보 담긴 DTO
     * @return : 201
     * @throws IOException
     */
    @PostMapping("/api/boards/{type}")
    public ResponseEntity write(@PathVariable("type") String type,
                                @CurrentUser User currentUser,
                                @Valid BoardWriteDto boardWriteDto) throws IOException {

        BoardType boardType = boardTypeMap.get(type);
        if (boardType == null) {
            throw new BoardTypeException();
        }

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

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     *  입력받은 boardSeq을 가지는 FreeBoard return
     * 
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : 200 with Board
     */
    @GetMapping("/api/boards/free/{boardSeq}")
    public ResponseEntity<Board> freeBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.FREE) {
            throw new BoardTypeException();
        }

        return new ResponseEntity(board, HttpStatus.OK);
    }

    /**
     *  입력받은 boardSeq을 가지는 NotifyBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : 200 with Board
     */
    @GetMapping("/api/boards/notify/{boardSeq}")
    public ResponseEntity<Board> notifyBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.NOTIFY) {
            throw new BoardTypeException();
        }

        return new ResponseEntity(board, HttpStatus.OK);
    }

    /**
     *  입력받은 boardSeq을 가지는 NewsBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : 200 with Board
     */
    @GetMapping("/api/boards/news/{boardSeq}")
    public ResponseEntity<Board> newsBoardDetail(@PathVariable("boardSeq") int boardSeq) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.NEWS) {
            throw new BoardTypeException();
        }

        return new ResponseEntity(board, HttpStatus.OK);
    }

    /**
     *  입력받은 boardSeq을 가지는 NewsBoard return
     *
     * @param boardSeq : return 할 게시글의 boardSeq
     * @return : 200 with Board
     */
    @GetMapping("/api/boards/member/{boardSeq}")
    public ResponseEntity<Board> memberBoardDetail(@PathVariable("boardSeq") int boardSeq,
                                                   @CurrentUser User currentUser) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != BoardType.MEMBER) {
            throw new BoardTypeException();
        }

        return new ResponseEntity(board, HttpStatus.OK);
    }


    /**
     * 유효성 검사 후 게시글 수정 정보를 사용하여 게시글 수정
     *
     * @param type : 게시글 종류
     * @param boardSeq : 수정할 게시글의 boardSeq
     * @param currentUser : 현재 사용자
     * @param boardModifyDto : 게시글 수정 정보 담긴 DTO
     * @return : 204
     * @throws IOException
     */
    @PutMapping("/api/boards/{type}/{boardSeq}")
    public ResponseEntity modify(@PathVariable("type") String type,
                                 @PathVariable("boardSeq") int boardSeq,
                                 @CurrentUser User currentUser,
                                 @Valid BoardModifyDto boardModifyDto) throws IOException {

        BoardType boardType = boardTypeMap.get(type);
        if (boardType == null) {
            throw new BoardTypeException();
        }

        Board originBoard = boardService.getBoardBySeq(boardSeq);
        if (originBoard.getBoardType() != boardType) {
            throw new BoardTypeException();
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

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 입력받은 boardSeq 가지는 Board 삭제
     *
     * @param type : 게시글 종류
     * @param boardSeq : 삭제할 게시글의 boardSeq
     * @param currentUser : 현재 사용자
     * @return : 204
     */
    @DeleteMapping("/api/boards/{type}/{boardSeq}")
    public ResponseEntity delete(@PathVariable("type") String type,
                                 @PathVariable("boardSeq") int boardSeq,
                                 @CurrentUser User currentUser) {

        BoardType boardType = boardTypeMap.get(type);
        if (boardType == null) {
            throw new BoardTypeException();
        }

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            throw new BoardTypeException();
        }
        UserUtils.checkAuthorization(currentUser, board.getUserSeq());

        boardService.deleteBoard(boardSeq);
        log.info("게시글이 삭제되었습니다. boardSeq={}", boardSeq);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 검색 조건에 해당하는 FreeBoard 총 갯수 return
     * 
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건에 해당하는 FreeBoard 총 갯수
     */
    @GetMapping("/api/boards/free/count")
    public ResponseEntity freeBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return new ResponseEntity(boardCountDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 NotifyBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건에 해당하는 NotifyBoard 총 갯수
     */
    @GetMapping("/api/boards/notify/count")
    public ResponseEntity notifyBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return new ResponseEntity(boardCountDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 MemberBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건에 해당하는 MemberBoard 총 갯수
     */
    @GetMapping("/api/boards/member/count")
    public ResponseEntity memberBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return new ResponseEntity(boardCountDto, HttpStatus.OK);
    }

    /**
     * 검색 조건에 해당하는 NewsBoard 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건에 해당하는 NewsBoard 총 갯수
     */
    @GetMapping("/api/boards/news/count")
    public ResponseEntity newsBoardCount(SearchCriteria searchCriteria) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        BoardCountDto boardCountDto = BoardCountDto.builder()
                .totalBoardCount(totalBoardCount)
                .build();

        return new ResponseEntity(boardCountDto, HttpStatus.OK);
    }

    /**
     * 입력받은 boardSeq을 가지는 Board의 visitCount 1 중가
     *
     * @param boardSeq : visitCount를 증가 시킬 Board의 boardSeq
     * @return : 204
     */
    @PutMapping("/api/boards/{boardSeq}/visit-count")
    public ResponseEntity updateVisitCount(@PathVariable("boardSeq") int boardSeq) {

        boardService.updateVisitCount(boardSeq);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
