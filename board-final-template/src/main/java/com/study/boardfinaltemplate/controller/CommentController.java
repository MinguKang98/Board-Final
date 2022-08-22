package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.annotation.CurrentUser;
import com.study.boardfinaltemplate.domain.Comment;
import com.study.boardfinaltemplate.domain.boards.BoardType;
import com.study.boardfinaltemplate.domain.criteria.SearchCriteria;
import com.study.boardfinaltemplate.domain.users.User;
import com.study.boardfinaltemplate.dto.comments.CommentDeleteDto;
import com.study.boardfinaltemplate.dto.comments.CommentModifyDto;
import com.study.boardfinaltemplate.dto.comments.CommentWriteDto;
import com.study.boardfinaltemplate.service.comments.CommentService;
import com.study.boardfinaltemplate.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

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
     * 댓글 작성 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 작성 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     *
     * @param boardType          : 게시글 종류
     * @param commentWriteDto    : 작성할 댓글의 DTO
     * @param bindingResult
     * @param searchCriteria     : 검색 기준
     * @param response
     * @param redirectAttributes
     * @return : redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 작성 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/write")
    public String write(@RequestParam("type") BoardType boardType,
                        @Valid CommentWriteDto commentWriteDto,
                        BindingResult bindingResult,
                        SearchCriteria searchCriteria,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ((boardType == BoardType.FREE) || (boardType == BoardType.MEMBER)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(
                    String.format("/board/%s/%d", boardType.getType(), commentWriteDto.getBoardSeq()), response);
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 작성 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", boardType.getType(), commentWriteDto.getBoardSeq());
        }

        Comment comment = Comment.builder()
                .content(commentWriteDto.getContent())
                .userSeq(commentWriteDto.getUserSeq())
                .boardSeq(commentWriteDto.getBoardSeq())
                .build();

        int commentSeq = commentService.addComment(comment);
        log.info("댓글이 생성되었습니다. commentSeq={}", commentSeq);

        return String.format("redirect:/board/%s/%d", boardType.getType(), commentWriteDto.getBoardSeq());
    }

    /**
     * 댓글 수정 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 수정 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     *
     * @param boardType          : 게시글 종류
     * @param commentModifyDto   : 댓글 수정 시 사용되는 DTO
     * @param bindingResult
     * @param searchCriteria     : 검색 기준
     * @param response
     * @param redirectAttributes
     * @return : redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 수정 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/modify")
    public String modify(@RequestParam("type") BoardType boardType,
                         @Valid CommentModifyDto commentModifyDto,
                         BindingResult bindingResult,
                         SearchCriteria searchCriteria,
                         HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ((boardType == BoardType.FREE) || (boardType == BoardType.MEMBER)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(
                    String.format("/board/%s/%d", boardType.getType(), commentModifyDto.getBoardSeq()), response);
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 수정 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", boardType.getType(), commentModifyDto.getBoardSeq());
        }

        Comment comment = Comment.builder()
                .commentSeq(commentModifyDto.getCommentSeq())
                .content(commentModifyDto.getContent())
                .build();

        commentService.updateComment(comment);
        log.info("댓글이 수정되었습니다. commentSeq={}", commentModifyDto.getCommentSeq());

        return String.format("redirect:/board/%s/%d", boardType.getType(), commentModifyDto.getBoardSeq());
    }

    /**
     * 댓글 삭제 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 삭제 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     *
     * @param boardType          : 게시글 종류
     * @param commentDeleteDto   : 댓글 삭제 시 사용되는 DTO
     * @param bindingResult
     * @param searchCriteria     : 검색 기준
     * @param response
     * @param redirectAttributes
     * @return :  redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 삭제 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/delete")
    public String delete(@RequestParam("type") BoardType boardType,
                         @Valid CommentDeleteDto commentDeleteDto,
                         BindingResult bindingResult,
                         SearchCriteria searchCriteria,
                         HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ((boardType == BoardType.FREE) || (boardType == BoardType.MEMBER)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl(
                    String.format("/board/%s/%d", boardType.getType(), commentDeleteDto.getBoardSeq()), response);
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 삭제 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", boardType.getType(), commentDeleteDto.getBoardSeq());
        }

        commentService.deleteComment(commentDeleteDto.getCommentSeq());
        log.info("댓글이 삭제되었습니다. commentSeq={}", commentDeleteDto.getCommentSeq());

        return String.format("redirect:/board/%s/%d", boardType.getType(), commentDeleteDto.getBoardSeq());
    }

}
