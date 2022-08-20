package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.boards.Board;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.service.boards.BoardService;
import com.study.boardfinalback.utils.JwtUtils;
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
     * call home page
     *
     * @return : home.html
     */
    @GetMapping("/")
    public String home(Model model) {

        SearchCriteria searchCriteria = SearchCriteria.builder().build();
        PagingCriteria pagingCriteria = PagingCriteria.builder()
                .curPage(1)
                .totalBoardCount(10)
                .build();

        List<Board> notifyBoardList = boardService.getNotifyBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<Board> freeBoardList = boardService.getFreeBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<Board> memberBoardList = boardService.getMemberBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);
        List<Board> newsBoardList = boardService.getNewsBoardListBySearchPagingCriteria(searchCriteria, pagingCriteria);

        model.addAttribute("notifyBoardList", notifyBoardList);
        model.addAttribute("freeBoardList", freeBoardList);
        model.addAttribute("memberBoardList", memberBoardList);
        model.addAttribute("newsBoardList", newsBoardList);

        return "home";
    }

}
