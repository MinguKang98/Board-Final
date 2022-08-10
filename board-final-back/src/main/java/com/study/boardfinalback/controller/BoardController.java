package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.Board;
import com.study.boardfinalback.domain.Category;
import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.domain.File;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.service.*;
import com.study.boardfinalback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final FileService fileService;

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

    // TODO 자유 게시판

    // TODO 회원 게시판

    // TODO 뉴스 게시판

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

    // TODO 게시글 등록

    // TODO 게시글 수정

    // TODO 게시글 삭제





}
