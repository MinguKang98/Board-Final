package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.annotation.CurrentUser;
import com.study.boardfinaltemplate.domain.users.User;
import com.study.boardfinaltemplate.domain.users.UserRole;
import com.study.boardfinaltemplate.domain.users.UserStatus;
import com.study.boardfinaltemplate.dto.users.LoginDto;
import com.study.boardfinaltemplate.dto.users.PasswordChangeDto;
import com.study.boardfinaltemplate.dto.users.RegisterDto;
import com.study.boardfinaltemplate.service.UserService;
import com.study.boardfinaltemplate.utils.CookieUtils;
import com.study.boardfinaltemplate.utils.EncryptUtils;
import com.study.boardfinaltemplate.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * User 관련 요청 처리하는 controller
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private boolean isAuthenticated = false;
    private User currentUser = null;

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
     * call login page
     *
     * @param model
     * @return : 로그인이 되었다면 redirect:/, 로그인 되지 않았다면 login.html
     */
    @GetMapping("/login")
    public String loginPage(Model model) {

        if (isAuthenticated) {
            return "redirect:/";
        }

        model.addAttribute("form", new LoginDto());
        return "/users/login";
    }

    /**
     * LoginDto의 값에 일치하는 User가 있다면 token을 생성해 cookie에 추가 후 redirect:/, 없다면 redirect:/login
     *
     * @param loginDto : login에 필요한 필드들이 들어간 dto
     * @param response
     * @return : loginDto의 값에 일치하는 User가 있다면 redirect:/, 없다면 redirect:/login
     */
    @PostMapping("/loginUser")
    public String loginUser(@CookieValue(value = "URL-BEFORE-LOGIN", required = false) String redirectUrl,
                            LoginDto loginDto,
                            HttpServletResponse response) {

        Optional<User> loginUser = userService.login(loginDto.getId(), EncryptUtils.encryptPassword(loginDto.getPassword()));
        if (loginUser.isEmpty()) {
            log.info("There is no User with id={} password={}", loginDto.getId(), loginDto.getPassword());
            return "redirect:/login?error=1";
        }

        String jwtToken = JwtUtils.makeJwtToken(loginUser.get());
        Cookie authCookie = new Cookie("AUTH-TOKEN", jwtToken);
        authCookie.setPath("/");
        authCookie.setMaxAge(30 * 60);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        response.addCookie(authCookie);

        if (redirectUrl == null) {
            redirectUrl = "/";
        }
        Cookie urlCookie = new Cookie("URL-BEFORE-LOGIN", null);
        urlCookie.setMaxAge(0);
        response.addCookie(urlCookie);
        return String.format("redirect:%s", redirectUrl);
    }

    /**
     * cookie 값을 삭제하여 로그아웃
     *
     * @param response
     * @return : redirect:/
     */
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    /**
     * call register page
     *
     * @param model
     * @return : 로그인이 되었다면 redirect:/, 로그인 되지 않았다면 register.html
     */
    @GetMapping("/register")
    public String registerPage(Model model) {

        if (isAuthenticated) {
            return "redirect:/";
        }

        model.addAttribute("form", new RegisterDto());
        return "/users/register";
    }

    /**
     * map에 들어있는 id의 value를 확인하여 존재하는 id인지 확인 후, 존재하지 않는다면 valid: true,
     * 존재한면 valid: false를 return
     *
     * @param params : id 값이 들어있는 map
     * @return : id가 존재하지 않는다면 valid: true, 존재한면 valid: false
     */
    @PostMapping("/identifyCheck")
    @ResponseBody
    public Map<String, Object> identifyCheck(@RequestParam Map<String, Object> params) {

        boolean identifyCheck = userService.identifyCheck(params.get("id").toString());
        Map<String, Object> result = new HashMap<>();
        if (identifyCheck) {
            result.put("valid", true);
        } else {
            result.put("valid", false);
        }
        return result;
    }

    /**
     * map에 들어있는 email의 value를 확인하여 존재하는 email인지 확인 후, 존재하지 않는다면 valid: true,
     * 존재한면 valid: false를 return
     *
     * @param params : email 값이 들어있는 map
     * @return : email이 존재하지 않는다면 valid: true, 존재한면 valid: false
     */
    @PostMapping("/emailCheck")
    @ResponseBody
    public Map<String, Object> emailCheck(@RequestParam Map<String, Object> params) {

        boolean emailCheck = userService.emailCheck(params.get("email").toString());
        Map<String, Object> result = new HashMap<>();
        if (emailCheck) {
            result.put("valid", true);
        } else {
            result.put("valid", false);
        }
        return result;
    }

    /**
     * 유효성 검사를 통과한 registerDto를 사용해 User를 등록한 후 call login page. 검사 실패시 call register page
     *
     * @param registerDto   : 회원가입에 필요한 필드 들어있는 DTO
     * @param bindingResult
     * @return : 유효성 검사에 성공 시 redirect:/login, 실패 시 redirect:/register
     */
    @PostMapping("/registerUser")
    public String register(@Valid RegisterDto registerDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("회원 가입 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return "redirect:/register";
        }

        if (!userService.identifyCheck(registerDto.getId())) {
            log.info("이미 등록된 id : {}", registerDto.getId());
            return "redirect:/register";
        }

        if (!userService.emailCheck(registerDto.getEmail())) {
            log.info("이미 등록된 email : {}", registerDto.getEmail());
            return "redirect:/register";
        }

        User user = User.builder()
                .name(registerDto.getName())
                .id(registerDto.getId())
                .email(registerDto.getEmail())
                .password(EncryptUtils.encryptPassword(registerDto.getPassword()))
                .role(UserRole.ROLE_MEMBER)
                .status(UserStatus.ALLOWED)
                .build();

        userService.saveUser(user);
        log.info("register user - id : {}", user.getId());

        return "redirect:/login";
    }

    /**
     * call user detail page
     *
     * @param userSeq : User의 userSeq
     * @param model
     * @return : userDetail.html
     */
    @GetMapping("/users/{userSeq}")
    public String userDetailPage(@PathVariable("userSeq") int userSeq, Model model) {

        User user = userService.getUserBySeq(userSeq);
        model.addAttribute("user", user);

        return "/users/userDetail";
    }

    /**
     * call password change page
     *
     * @param response
     * @param model
     * @return : 로그인 되었있다면 passwordChange.html, 되어있지 않다면 redirect:/login
     */
    @GetMapping("/user/passwordChange")
    public String passwordChangePage(HttpServletResponse response, Model model) {

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl("/user/passwordChange", response);
            return "redirect:/login";
        }

        model.addAttribute("form", new PasswordChangeDto());

        return "/users/passwordChange";
    }

    /**
     * 유효성 감사를 통과한 passwordChangeDto를 사용해 비밀번호 변경 후, redirect:/users/{userSeq}, 통과하지 못하면 redirect:/user/passwordChange
     *
     * @param model
     * @param passwordChangeDto : passwordChange에 필요한 필드 포함한 DTO
     * @param bindingResult
     * @return : 유효성 검사 통과 시 redirect:/users/{userSeq}, 통과하지 못하면 redirect:/user/passwordChange
     */
    @PostMapping("/passwordChange")
    public String passwordChange(Model model, @Valid PasswordChangeDto passwordChangeDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.info("비밀번호 변경 형식 에러 : {}", fieldError.getDefaultMessage());
            }
            return "redirect:/user/passwordChange";
        }

        if (passwordChangeDto.getOriginPassword().equals(passwordChangeDto.getNewPassword())) {
            log.info("비밀번호 변경 형식 에러 : 새 비밀번호가 기존 비밀번호와 일치합니다.");
            return "redirect:/user/passwordChange";
        }

        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getNewPasswordCheck())) {
            log.info("비밀번호 변경 형식 에러 : 새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
            return "redirect:/user/passwordChange";
        }

        if (!currentUser.getPassword().equals(
                EncryptUtils.encryptPassword(passwordChangeDto.getOriginPassword()
                ))) {
            log.info("비밀번호 변경 형식 에러 : 기존 비밀번호가 일치하지 않습니다.");
            return "redirect:/user/passwordChange";
        }

        User newUser = User.builder()
                .userSeq(currentUser.getUserSeq())
                .password(
                        EncryptUtils.encryptPassword(passwordChangeDto.getNewPassword())
                )
                .build();


        userService.updateUserPassword(newUser);
        log.info("비밀번호가 변경되었습니다. user={}", currentUser.getId());

        return String.format("redirect:/users/%d", currentUser.getUserSeq());
    }

    /**
     * call user delete page
     *
     * @param response
     * @param model
     * @return : 로그인 되어있으면 users/userDelete.html, 안 되어있으면 redirect:/login
     */
    @GetMapping("/user/delete")
    public String userDeletePage(HttpServletResponse response) {

        if (!isAuthenticated) {
            CookieUtils.setNextLoginPageUrl("/user/delete", response);
            return "redirect:/login";
        }

        return "/users/userDelete";
    }

    /**
     * 로그인 되어있는 User 삭제 후 redirect:/
     *
     * @param model
     * @param response
     * @return : redirect:/
     */
    @PostMapping("/userDelete")
    public String userDelete(HttpServletResponse response) {

        userService.deleteUser(currentUser.getUserSeq());
        log.info("유저가 삭제되었습니다 - userSeq:{}", currentUser.getUserSeq());

        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    // TODO 차단

}
