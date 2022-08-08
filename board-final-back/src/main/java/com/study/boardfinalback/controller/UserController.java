package com.study.boardfinalback.controller;

import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.dto.LoginDto;
import com.study.boardfinalback.jwt.JwtUtils;
import com.study.boardfinalback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
     * cookie의 token 값을 확인하여 인증여부, id, role을 Model에 설정한다
     *
     * @param token : jwt token
     * @param model
     */
    @ModelAttribute
    public void authenticated(@CookieValue(value = "AUTH-TOKEN", required = false) String token,
                              Model model) {
        if (token != null) {
            model.addAttribute("authenticated", true);
            model.addAttribute("id", JwtUtils.getClaims(token).get("id"));
            model.addAttribute("role", JwtUtils.getClaims(token).get("role"));
        }
        else{
            model.addAttribute("authenticated", false);
        }
    }

    /**
     * call home page
     *
     * @param model
     * @return : home.html
     */
    @GetMapping("/")
    public String home(Model model) {
        return "home";
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
        return "login";
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
        Optional<User> loginUser = userService.login(loginDto.getId(), loginDto.getPassword());
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
        return "register";
    }
}
