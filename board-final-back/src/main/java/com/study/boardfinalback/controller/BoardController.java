package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.*;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.dto.boards.BoardWriteDto;
import com.study.boardfinalback.dto.files.FileDto;
import com.study.boardfinalback.service.*;
import com.study.boardfinalback.utils.FileUtils;
import com.study.boardfinalback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final FileService fileService;

    private final FileUtils fileUtils;

    private Map<String, BoardType> boardTypeMap = Map.of(
            "notify", BoardType.NOTIFY,
            "free", BoardType.FREE,
            "member", BoardType.MEMBER,
            "news", BoardType.NEWS
    );

    /**
     * cookie의 token 값을 확인하여 인증여부, userSeq, id, role을 Model에 설정한다
     *
     * @param token : jwt token
     * @param model
     */
    @ModelAttribute
    public void authenticated(@CookieValue(value = "AUTH-TOKEN", required = false) String token,
                              Model model) {
        if (token != null) {
            model.addAttribute("authenticated", true);
            model.addAttribute("userSeq", JwtUtils.getClaims(token).get("userSeq"));
            model.addAttribute("userId", JwtUtils.getClaims(token).get("id"));
            model.addAttribute("role", JwtUtils.getClaims(token).get("role"));
        } else {
            model.addAttribute("authenticated", false);
        }
    }

    /**
     * call notify board page
     *
     * @param searchCriteria  : 검색 기준
     * @param model
     * @return : /boards/notifyBoard.html
     */
    @GetMapping("/board/notify")
    public String notifyBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        //TODO user join
        List<Board> boardList = boardService.getNotifyBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);

        return "/boards/notifyBoard";
    }

    /**
     * call free board page
     *
     * @param searchCriteria  : 검색 기준
     * @param model
     * @return : /boards/freeBoard.html
     */
    @GetMapping("/board/free")
    public String freeBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        //TODO user join
        List<Board> boardList = boardService.getFreeBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<Category> categoryList = categoryService.getCategoryList();
        categoryList.remove(0);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);
        model.addAttribute("categoryList", categoryList);

        return "/boards/freeBoard";
    }

    /**
     * call member board page
     *
     * @param searchCriteria  : 검색 기준
     * @param model
     * @return : /boards/memberBoard.html
     */
    @GetMapping("/board/member")
    public String memberBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        //TODO user join
        List<Board> boardList = boardService.getMemberBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<Category> categoryList = categoryService.getCategoryList();
        categoryList.remove(0);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);
        model.addAttribute("categoryList", categoryList);

        return "/boards/memberBoard";
    }

    /**
     * call news board page
     *
     * @param searchCriteria  : 검색 기준
     * @param model
     * @return : /boards/newsBoard.html
     */
    @GetMapping("/board/news")
    public String newsBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(
                        (searchCriteria.getCurPage() == null) ? 1 : searchCriteria.getCurPage()
                )
                .totalBoardCount(totalBoardCount)
                .build();

        //TODO user join
        List<Board> boardList = boardService.getNewsBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);

        return "/boards/newsBoard";
    }

    /**
     * call board detail page
     *
     * @param type : 게시글의 종류
     * @param boardSeq : 게시글의 boardSeq
     * @param model
     * @return : /boards/boardDetail.html
     */
    @GetMapping("/board/{type}/{boardSeq}")
    public String boardDetail(@PathVariable("type") String type,
                              @PathVariable("boardSeq") int boardSeq,
                              SearchCriteria searchCriteria,
                              Model model) {

        if (boardTypeMap.get(type) == null) {
            return "redirect:/";
        }

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (type.equals("member") && isAuthenticated == false) {
            return "redirect:/login";
        }

        boardService.updateVisitCount(boardSeq);

        Board board = boardService.getBoardBySeq(boardSeq);
        User user = userService.getUserBySeq(board.getUserSeq());
        List<Comment> commentList = commentService.getCommentListByBoardSeq(boardSeq);
        //TODO comment,user join 으로 return
        List<File> fileList = fileService.getFileListByBoardSeq(boardSeq);
        if (board.getCategorySeq() != 1) {
            Category category = categoryService.getCategoryBySeq(board.getCategorySeq());
            model.addAttribute("category", category);

        }

        model.addAttribute("type", type);
        model.addAttribute("user", user);
        model.addAttribute("board", board);
        model.addAttribute("commentList", commentList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("searchCriteria", searchCriteria);

        return "/boards/boardDetail";
    }

    /**
     * call board write page
     *
     * @param type : 작성할 게시글 type
     * @param model
     * @return : 로그인 안되어 있으면 redirect:/board/{type}, 지정된 type이 아닌 경우는 redirect:/,
     * notify, news에 ROLE_MEMBER 유저가 접근 시 redirect:/, 나머지 경우는 /boards/boardWrite.html
     */
    @GetMapping("/board/{type}/write")
    public String boardWrite(@PathVariable("type") String type,
                             SearchCriteria searchCriteria,
                             Model model) {

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
            return String.format("redirect:/login", type);
        }

        if (boardTypeMap.get(type) == null) {
            return "redirect:/";
        }

        String role = model.getAttribute("role").toString();
        if (type.equals("notify") || type.equals("news")) {
            if (role.equals("ROLE_MEMBER")) {
                return "redirect:/";
            }
        }

        if (type.equals("free") || type.equals("member")) {
            List<Category> categoryList = categoryService.getCategoryList();
            categoryList.remove(0);
            model.addAttribute("categoryList", categoryList);
        }

        model.addAttribute("type", type);
        model.addAttribute("form", new BoardWriteDto());
        model.addAttribute("searchCriteria", searchCriteria);

        return "/boards/boardWrite";
    }

    /**
     * 유효성 검사를 통과한 boardWriteDto와 fileDto를 사용해 Board 생성 후 생성된 board page로 이동한다.
     * 유효성 검사를 통과하지 못한 경우는 redirect:/board/{type}/write
     *
     * @param type : 게시글 종류
     * @param fileDto : 업로드할 파일들이 담긴 DTO
     * @param boardWriteDto : 업로드할 게시글의 필드들이 담긴 DTO
     * @param bindingResult
     * @return : 유효성 검사를 통과하지 못한 경우 redirect:/board/{type}/write,
     * 통과한 경우 redirect:/board/{type}/{boardSeq}. 이때 boardSeq은 현재 생성된 Board의 boardSeq
     * @throws IOException
     */
    @PostMapping("/boardWrite/{type}")
    public String write(@PathVariable("type") String type,
                        FileDto fileDto,
                        @Valid BoardWriteDto boardWriteDto,
                        BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("게시글 작성 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/write", type);
        }

        BoardType boardType = boardTypeMap.get(type);
        Board board = Board.builder().title(boardWriteDto.getTitle())
                .content(boardWriteDto.getContent())
                .fileExist(fileDto.isFileExist())
                .boardType(boardType)
                .userSeq(boardWriteDto.getUserSeq())
                .categorySeq(boardWriteDto.getCategorySeq())
                .build();

        List<File> addFileList = fileUtils.getAddFileList(fileDto.getFiles());

        int boardSeq = boardService.addBoard(board, addFileList);
        log.info("새로운 게시글이 등록되었습니다. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s/%d", type, boardSeq);
    }

    // TODO 게시글 수정

    // TODO 게시글 삭제

}
