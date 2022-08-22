package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.annotation.CurrentUser;
import com.study.boardfinaltemplate.dto.boards.BoardWithUserAndCategoryDto;
import com.study.boardfinaltemplate.domain.boards.Board;
import com.study.boardfinaltemplate.domain.criteria.PagingCriteria;
import com.study.boardfinaltemplate.domain.criteria.SearchCriteria;
import com.study.boardfinaltemplate.domain.users.User;
import com.study.boardfinaltemplate.service.boards.BoardQueryService;
import com.study.boardfinaltemplate.service.boards.BoardService;
import com.study.boardfinaltemplate.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;
    private final BoardQueryService boardQueryService;

    /**
     * call home page
     *
     * @return : home.html
     */
    @GetMapping("/")
    public String home(@CurrentUser User currentUser, Model model) {

        SearchCriteria searchCriteria = SearchCriteria.builder().build();
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(1)
                .totalBoardCount(10)
                .build();

        List<BoardWithUserAndCategoryDto> notifyBoardList = boardQueryService
                .getNotifyBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<BoardWithUserAndCategoryDto> freeBoardList = boardQueryService
                .getFreeBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<BoardWithUserAndCategoryDto> memberBoardList = boardQueryService
                .getMemberBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<BoardWithUserAndCategoryDto> newsBoardList = boardQueryService
                .getNewsBoardWithUserAndCategoryDtoListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("notifyBoardList", notifyBoardList);
        model.addAttribute("freeBoardList", freeBoardList);
        model.addAttribute("memberBoardList", memberBoardList);
        model.addAttribute("newsBoardList", newsBoardList);
        model.addAttribute("currentUser", currentUser);

        return "home";
    }

}
