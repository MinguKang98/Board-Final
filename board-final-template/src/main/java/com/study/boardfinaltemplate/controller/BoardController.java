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
     * String type??? BoardType ?????? ??????
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
     * ?????? ????????? ??? User??? currentUser??? ?????? model??? ?????????.
     * currentUser??? null??? ????????? authenticsted??? true??? ???????????????.
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
     * @param searchCriteria : ?????? ??????
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
     * @param searchCriteria : ?????? ??????
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
     * @param searchCriteria : ?????? ??????
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
     * @param searchCriteria : ?????? ??????
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
     * @param boardType      : ???????????? ??????
     * @param boardSeq       : ???????????? boardSeq
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
     * @param boardType      : ????????? ????????? type
     * @param searchCriteria
     * @param response
     * @param model
     * @return : ????????? ????????? ????????? redirect:/board/{type}, ????????? type??? ?????? ????????? redirect:/,
     * notify, news??? ROLE_MEMBER ????????? ?????? ??? redirect:/, ????????? ????????? /boards/boardWrite.html
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
     * ????????? ????????? ????????? boardWriteDto??? fileDto??? ????????? Board ?????? ??? ????????? board page??? ????????????.
     * ????????? ????????? ???????????? ?????? ????????? redirect:/board/{type}/write
     *
     * @param boardType     : ????????? ??????
     * @param boardWriteDto : ???????????? ???????????? ???????????? ?????? DTO
     * @param bindingResult
     * @return : ????????? ????????? ???????????? ?????? ?????? redirect:/board/{type}/write,
     * ????????? ?????? redirect:/board/{type}/{boardSeq}. ?????? boardSeq??? ?????? ????????? Board??? boardSeq
     * @throws IOException
     */
    @PostMapping("/boardWrite/{type}")
    public String write(@PathVariable("type") BoardType boardType,
                        @Valid BoardWriteDto boardWriteDto,
                        BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("????????? ?????? ?????? ?????? : {}", fieldError.getDefaultMessage());
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
        log.info("????????? ???????????? ?????????????????????. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
    }

    /**
     * call board modify page
     *
     * @param boardType      : ????????? ??????
     * @param boardSeq       : ????????? boardSeq
     * @param searchCriteria : ?????? ??????
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
                log.info("????????? ?????? ?????? ?????? : {}", fieldError.getDefaultMessage());
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
        log.info("???????????? ?????????????????????. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s/%d", boardType.getType(), boardSeq);
    }

    /**
     * @param boardType      : ????????? ??????
     * @param boardSeq       : ????????? ???????????? boardSeq
     * @param searchCriteria : ?????? ??????
     * @param response
     * @param model
     * @return : ????????? ???????????? ????????? redirect:/login, ????????? type??? ????????? redirect:/,
     * ?????? ????????? ?????? ???????????? redirect:/board/{type}/{boardSeq}, ?????? ????????? ?????? ???????????? /boards/boardDelete.html
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
     * ????????? ?????? ??? ??????????????? ??????
     *
     * @param type     : ????????? ??????
     * @param boardSeq : ????????? Board??? boardSeq
     * @return : redirect:/board/{type}
     */
    @PostMapping("/boardDelete")
    public String delete(@RequestParam("type") String type,
                         @RequestParam("boardSeq") int boardSeq) {

        boardService.deleteBoard(boardSeq);
        log.info("???????????? ?????????????????????. boardSeq={}", boardSeq);

        return String.format("redirect:/board/%s", type);
    }

}
