package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.domain.user.UserRole;
import com.study.boardfinalback.domain.user.UserStatus;
import com.study.boardfinalback.dto.LoginDto;
import com.study.boardfinalback.dto.RegisterDto;
import com.study.boardfinalback.dto.PasswordChangeDto;
import com.study.boardfinalback.utils.EncryptUtils;
import com.study.boardfinalback.utils.JwtUtils;
import com.study.boardfinalback.service.UserService;
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
     * call login page
     *
     * @param model
     * @return : 로그인이 되었다면 redirect:/, 로그인 되지 않았다면 login.html
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == true) {
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
    public String loginUser(LoginDto loginDto, HttpServletResponse response) {
        Optional<User> loginUser = userService.login(loginDto.getId(), EncryptUtils.encryptPassword(loginDto.getPassword()));
        if (loginUser.isEmpty()) {
            log.info("There is no User with id={} password={}", loginDto.getId(), loginDto.getPassword());
            return "redirect:/login?error=1";
        }

        String jwtToken = JwtUtils.makeJwtToken(loginUser.get());

        Cookie cookie = new Cookie("AUTH-TOKEN", jwtToken);
        cookie.setMaxAge(30 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "redirect:/";
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

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == true) {
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
        }
        else{
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
        }
        else{
            result.put("valid", false);
        }
        return result;
    }

    /**
     * 유효성 검사를 통과한 registerDto를 사용해 User를 등록한 후 call login page. 검사 실패시 call register page
     *
     * @param registerDto : 회원가입에 필요한 필드 들어있는 DTO
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
     * @param model
     * @return : 로그인 되었있다면 passwordChange.html, 되어있지 않다면 redirect:/login
     */
    @GetMapping("/user/passwordChange")
    public String passwordChangePage(Model model) {

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
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

        //TODO valid custom

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

        int userSeq = Integer.parseInt(model.getAttribute("userSeq").toString());
        User user = userService.getUserBySeq(userSeq);

        if (!user.getPassword().equals(
                EncryptUtils.encryptPassword(passwordChangeDto.getOriginPassword()
            ))) {
            log.info("비밀번호 변경 형식 에러 : 기존 비밀번호가 일치하지 않습니다.");
            return "redirect:/user/passwordChange";
        }

        User newUser = User.builder()
                .userSeq(userSeq)
                .password(
                        EncryptUtils.encryptPassword(passwordChangeDto.getNewPassword())
                )
                .build();


       userService.updateUserPassword(newUser);
        log.info("비밀번호가 변경되었습니다. user={}", user.getId());

        return String.format("redirect:/users/%d", userSeq);
    }

    /**
     * call user delete page
     *
     * @param model
     * @return : 로그인 되어있으면 users/userDelete.html, 안 되어있으면 redirect:/login
     */
    @GetMapping("/user/delete")
    public String userDeletePage(Model model) {

        boolean isAuthenticated = (boolean) model.getAttribute("authenticated");
        if (isAuthenticated == false) {
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
    public String userDelete(Model model, HttpServletResponse response) {

        int userSeq = Integer.parseInt(model.getAttribute("userSeq").toString());
        userService.deleteUser(userSeq);
        log.info("유저가 삭제되었습니다 - userSeq:{}", userSeq);

        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    // TODO 차단

}
