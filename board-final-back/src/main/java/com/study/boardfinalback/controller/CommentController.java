package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.dto.comment.CommentDeleteDto;
import com.study.boardfinalback.dto.comment.CommentModifyDto;
import com.study.boardfinalback.dto.comment.CommentWriteDto;
import com.study.boardfinalback.service.comments.CommentService;
import com.study.boardfinalback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

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
     * 댓글 작성 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 작성 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     *
     * @param type : 게시글 종류
     * @param commentWriteDto : 작성할 댓글의 DTO
     * @param bindingResult
     * @param searchCriteria : 검색 기준
     * @param redirectAttributes
     * @param model
     * @return : redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 작성 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/write")
    public String write(@RequestParam("type") String type,
                        @Valid CommentWriteDto commentWriteDto,
                        BindingResult bindingResult,
                        SearchCriteria searchCriteria,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ("free".equals(type) || "member".equals(type)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
            return String.format("redirect:/login", type);
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 작성 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", type, commentWriteDto.getBoardSeq());
        }

        Comment comment = Comment.builder()
                .content(commentWriteDto.getContent())
                .userSeq(commentWriteDto.getUserSeq())
                .boardSeq(commentWriteDto.getBoardSeq())
                .build();

        int commentSeq = commentService.addComment(comment);
        log.info("댓글이 생성되었습니다. commentSeq={}", commentSeq);

        return String.format("redirect:/board/%s/%d", type, commentWriteDto.getBoardSeq());
    }

    /**
     *  댓글 수정 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 수정 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     *
     * @param type : 게시글 종류
     * @param commentModifyDto : 댓글 수정 시 사용되는 DTO
     * @param bindingResult
     * @param searchCriteria : 검색 기준
     * @param redirectAttributes
     * @param model
     * @return : redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 수정 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/modify")
    public String modify(@RequestParam("type") String type,
                         @Valid CommentModifyDto commentModifyDto,
                         BindingResult bindingResult,
                         SearchCriteria searchCriteria,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ("free".equals(type) || "member".equals(type)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
            return String.format("redirect:/login", type);
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 수정 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", type, commentModifyDto.getBoardSeq());
        }

        Comment comment = Comment.builder()
                .commentSeq(commentModifyDto.getCommentSeq())
                .content(commentModifyDto.getContent())
                .build();

        commentService.updateComment(comment);
        log.info("댓글이 수정되었습니다. commentSeq={}",commentModifyDto.getCommentSeq());

        return String.format("redirect:/board/%s/%d", type, commentModifyDto.getBoardSeq());
    }

    /**
     * 댓글 삭제 후 redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 삭제 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     * @param type : 게시글 종류
     * @param commentDeleteDto : 댓글 삭제 시 사용되는 DTO
     * @param bindingResult
     * @param searchCriteria : 검색 기준
     * @param redirectAttributes
     * @param model
     * @return :  redirect:/board/{type}/{boardSeq}. 로그인 안 되어있으면 redirect:/login, 댓글 삭제 유효성 검사 실패 시 redirect:/board/{type}/{boardSeq}
     */
    @PostMapping("/comment/delete")
    public String delete(@RequestParam("type") String type,
                         @Valid CommentDeleteDto commentDeleteDto,
                         BindingResult bindingResult,
                         SearchCriteria searchCriteria,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        redirectAttributes.addAttribute("curPage", searchCriteria.getCurPage());
        redirectAttributes.addAttribute("dateCreatedFrom", searchCriteria.getDateCreatedFrom());
        redirectAttributes.addAttribute("dateCreatedTo", searchCriteria.getDateCreatedTo());
        redirectAttributes.addAttribute("text", searchCriteria.getText());
        if ("free".equals(type) || "member".equals(type)) {
            redirectAttributes.addAttribute("categorySeq", searchCriteria.getCategorySeq());
        }

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
            return String.format("redirect:/login", type);
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("댓글 삭제 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return String.format("redirect:/board/%s/%d", type, commentDeleteDto.getBoardSeq());
        }

        commentService.deleteComment(commentDeleteDto.getCommentSeq(), commentDeleteDto.getBoardSeq());
        log.info("댓글이 삭제되었습니다. commentSeq={}", commentDeleteDto.getCommentSeq());

        return String.format("redirect:/board/%s/%d", type, commentDeleteDto.getBoardSeq());
    }

}
