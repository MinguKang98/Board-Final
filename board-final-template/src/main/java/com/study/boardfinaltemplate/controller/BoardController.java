package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.annotation.CurrentUser;
import com.study.boardfinaltemplate.domain.*;
import com.study.boardfinaltemplate.domain.boards.Board;
import com.study.boardfinaltemplate.domain.boards.BoardType;
import com.study.boardfinaltemplate.domain.criteria.PagingCriteria;
import com.study.boardfinaltemplate.domain.criteria.SearchCriteria;
import com.study.boardfinaltemplate.domain.users.User;
import com.study.boardfinaltemplate.domain.users.UserRole;
import com.study.boardfinaltemplate.dto.boards.BoardModifyDto;
import com.study.boardfinaltemplate.dto.boards.BoardWithUserAndCategoryDto;
import com.study.boardfinaltemplate.dto.boards.BoardWriteDto;
import com.study.boardfinaltemplate.dto.comments.CommentWithUserDto;
import com.study.boardfinaltemplate.service.*;
import com.study.boardfinaltemplate.service.boards.BoardQueryService;
import com.study.boardfinaltemplate.service.boards.BoardService;
import com.study.boardfinaltemplate.service.comments.CommentQueryService;
import com.study.boardfinaltemplate.utils.CookieUtils;
import com.study.boardfinaltemplate.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final CategoryService categoryService;
    private final CommentQueryService commentQueryService;
    private final FileService fileService;
    private final FileUtils fileUtils;

    private boolean isAuthenticated = false;
    private User currentUser = null;

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
     * 현재 로그인 한 User를 currentUser에 받아 model에 넘긴다.
     * currentUser가 null이 아니면 authenticsted를 true로 초기화한다.
     *
     * @param currentUser
     * @param model
     */
    @ModelAttribute
    public void authenticated(@CurrentUser User currentUser, Model model) {
        model.addAttribute("currentUser", currentUser);
        if (currentUser != null) {
            isAuthenticated = true;
            this.currentUser = currentUser;
        } else {
            isAuthenticated = false;
        }
    }

    /**
     * call notify board page
     *
     * @param searchCriteria : 검색 기준
     * @param model
     * @return : /boards/notifyBoard.html
     */
    @GetMapping("/board/notify")
    public String notifyBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardList = boardQueryService
                .getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);

        return "/boards/notifyBoard";
    }

    /**
     * call free board page
     *
     * @param searchCriteria : 검색 기준
     * @param model
     * @return : /boards/freeBoard.html
     */
    @GetMapping("/board/free")
    public String freeBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardList = boardQueryService
                .getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);
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
     * @param searchCriteria : 검색 기준
     * @param model
     * @return : /boards/memberBoard.html
     */
    @GetMapping("/board/member")
    public String memberBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardList = boardQueryService
                .getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);
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
     * @param searchCriteria : 검색 기준
     * @param model
     * @return : /boards/newsBoard.html
     */
    @GetMapping("/board/news")
    public String newsBoard(SearchCriteria searchCriteria, Model model) {

        int totalBoardCount = boardService.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(searchCriteria.getCurPage())
                .totalBoardCount(totalBoardCount)
                .build();

        List<BoardWithUserAndCategoryDto> boardList = boardQueryService
                .getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("pagingCriteria", pagingCriteria);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("boardList", boardList);

        return "/boards/newsBoard";
    }

    /**
     * call board detail page
     *
     * @param boardType      : 게시글의 종류
     * @param boardSeq       : 게시글의 boardSeq
     * @param searchCriteria
     * @param response
     * @param model
     * @return : /boards/boardDetail.html
     */
    @GetMapping("/board/{type}/{boardSeq}")
    public String boardDetail(@PathVariable("type") BoardType boardType,
                              @PathVariable("boardSeq") int boardSeq,
                              SearchCriteria searchCriteria,
                              HttpServletResponse response,
                              Model model) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            return String.format("redirect:/board/%s", boardType.getType());
        }

        if ((boardType == BoardType.MEMBER) && !isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(String.format("/board/%s/%d", boardType.getType(), boardSeq), response);
            return "redirect:/login";
        }

        boardService.updateVisitCount(boardSeq);

        User user = userService.getUserBySeq(board.getUserSeq());
        List<CommentWithUserDto> commentList = commentQueryService
                .getCommentWithUserDtoListByBoardSeq(boardSeq);
        List<File> fileList = fileService.getFileListByBoardSeq(boardSeq);
        if (board.getCategorySeq() != 1) {
            Category category = categoryService.getCategoryBySeq(board.getCategorySeq());
            model.addAttribute("category", category);

        }

        model.addAttribute("type", boardType.getType());
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
     * @param boardType      : 작성할 게시글 type
     * @param searchCriteria
     * @param response
     * @param model
     * @return : 로그인 안되어 있으면 redirect:/board/{type}, 지정된 type이 아닌 경우는 redirect:/,
     * notify, news에 ROLE_MEMBER 유저가 접근 시 redirect:/, 나머지 경우는 /boards/boardWrite.html
     */
    @GetMapping("/board/{type}/write")
    public String boardWrite(@PathVariable("type") BoardType boardType,
                             SearchCriteria searchCriteria,
                             HttpServletResponse response,
                             Model model) {

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(String.format("/board/%s/write", boardType.getType()), response);
            return String.format("redirect:/login", boardType.getType());
        }

        if ((boardType == BoardType.NOTIFY) || (boardType == BoardType.NEWS)) {
            if ("ROLE_MEMBER".equals(currentUser.getRole())) {
                return "redirect:/";
            }
        }

        if ((boardType == BoardType.FREE) || (boardType == BoardType.MEMBER)) {
            List<Category> categoryList = categoryService.getCategoryList();
            categoryList.remove(0);
            model.addAttribute("categoryList", categoryList);
        }

        model.addAttribute("type", boardType.getType());
        model.addAttribute("form", new BoardWriteDto());
        model.addAttribute("searchCriteria", searchCriteria);

        return "/boards/boardWrite";
    }

    /**
     * 유효성 검사를 통과한 boardWriteDto와 fileDto를 사용해 Board 생성 후 생성된 board page로 이동한다.
     * 유효성 검사를 통과하지 못한 경우는 redirect:/board/{type}/write
     *
     * @param boardType     : 게시글 종류
     * @param boardWriteDto : 업로드할 게시글의 필드들이 담긴 DTO
     * @param bindingResult
     * @return : 유효성 검사를 통과하지 못한 경우 redirect:/board/{type}/write,
     * 통과한 경우 redirect:/board/{type}/{boardSeq}. 이때 boardSeq은 현재 생성된 Board의 boardSeq
     * @throws IOException
     */
    @PostMapping("/boardWrite/{type}")
    public String write(@PathVariable("type") BoardType boardType,
                        @Valid BoardWriteDto boardWriteDto,
                        BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("게시글 작성 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/write", boardType.getType());
        }

        Board board = Board.builder().title(boardWriteDto.getTitle())
                .content(boardWriteDto.getContent())
                .fileExist(boardWriteDto.isFileExist())
                .boardType(boardType)
                .userSeq(boardWriteDto.getUserSeq())
                .categorySeq(boardWriteDto.getCategorySeq())
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardWriteDto.getFiles());

        int boardSeq = boardService.addBoard(board, addFileList);
        log.info("새로운 게시글이 등록되었습니다. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
    }

    /**
     * call board modify page
     *
     * @param boardType      : 게시글 종류
     * @param boardSeq       : 게시글 boardSeq
     * @param searchCriteria : 검색 기준
     * @param response
     * @param model
     * @return
     */
    @GetMapping("/board/{type}/{boardSeq}/modify")
    public String boardModify(@PathVariable("type") BoardType boardType,
                              @PathVariable("boardSeq") int boardSeq,
                              SearchCriteria searchCriteria,
                              HttpServletResponse response,
                              Model model) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            return String.format("redirect:/board/%s", boardType.getType());
        }

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(String.format("/board/%s/%d/modify", boardType.getType(), boardSeq), response);
            return String.format("redirect:/login");
        }

        if ((UserRole.ROLE_MEMBER == currentUser.getRole()) && (currentUser.getUserSeq() != board.getUserSeq())) {
            return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
        }

        Category category = categoryService.getCategoryBySeq(board.getCategorySeq());
        List<File> fileList = fileService.getFileListByBoardSeq(boardSeq);

        model.addAttribute("type", boardType.getType());
        model.addAttribute("board", board);
        model.addAttribute("category", category);
        model.addAttribute("fileList", fileList);
        model.addAttribute("searchCriteria", searchCriteria);

        return "/boards/boardModify";
    }

    @PostMapping("/boardModify")
    public String modify(@RequestParam("type") BoardType boardType,
                         @RequestParam("boardSeq") int boardSeq,
                         @Valid BoardModifyDto boardModifyDto,
                         BindingResult bindingResult,
                         MultipartHttpServletRequest request,
                         SearchCriteria searchCriteria,
                         RedirectAttributes redirectAttributes) throws IOException {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        if ((boardType == BoardType.FREE) || (boardType == BoardType.MEMBER)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }
        redirectAttributes.addAttribute("text", searchCriteria.getText());

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("게시글 수정 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d/modify", boardType.getType(), boardSeq);
        }

        Board board = Board.builder()
                .boardSeq(boardSeq)
                .title(boardModifyDto.getTitle())
                .content(boardModifyDto.getContent())
                .build();

        List<File> addFileList = fileUtils.getAddFileList(boardModifyDto.getFiles(), boardSeq);
        List<File> deleteFileList = fileUtils.getDeleteFileList(fileService.getFileListByBoardSeq(boardSeq), request);

        boardService.updateBoard(board, addFileList, deleteFileList);
        log.info("게시글이 수정되었습니다. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
    }

    /**
     * @param boardType      : 게시글 종류
     * @param boardSeq       : 삭제할 게시글의 boardSeq
     * @param searchCriteria : 검색 기준
     * @param response
     * @param model
     * @return : 로그인 되어있지 않으면 redirect:/login, 지정된 type이 아니면 redirect:/,
     * 삭제 권한이 없는 접근이면 redirect:/board/{type}/{boardSeq}, 삭제 권한이 있는 접근이면 /boards/boardDelete.html
     */
    @GetMapping("/board/{type}/{boardSeq}/delete")
    public String boardDelete(@PathVariable("type") BoardType boardType,
                              @PathVariable("boardSeq") int boardSeq,
                              SearchCriteria searchCriteria,
                              HttpServletResponse response,
                              Model model) {

        Board board = boardService.getBoardBySeq(boardSeq);
        if (board.getBoardType() != boardType) {
            return String.format("redirect:/board/%s", boardType.getType());
        }

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(String.format("/board/%s/%d/delete", boardType.getType(), boardSeq), response);
            return String.format("redirect:/login");
        }

        if ((UserRole.ROLE_MEMBER == currentUser.getRole()) && (currentUser.getUserSeq() != board.getUserSeq())) {
            return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
        }

        model.addAttribute("type", boardType.getType());
        model.addAttribute("boardSeq", boardSeq);
        model.addAttribute("searchCriteria", searchCriteria);

        return "/boards/boardDelete";
    }

    /**
     * 게시글 삭제 후 게시판으로 이동
     *
     * @param type     : 게시글 종류
     * @param boardSeq : 삭제할 Board의 boardSeq
     * @return : redirect:/board/{type}
     */
    @PostMapping("/boardDelete")
    public String delete(@RequestParam("type") String type,
                         @RequestParam("boardSeq") int boardSeq) {

        boardService.deleteBoard(boardSeq);
        log.info("게시글이 삭제되었습니다. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s", type);
    }

}
