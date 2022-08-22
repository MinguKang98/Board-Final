package com.study.boardfinaltemplate.argumentResolver;

import com.study.boardfinaltemplate.annotation.CurrentUser;
import com.study.boardfinaltemplate.domain.users.User;
import com.study.boardfinaltemplate.service.UserService;
import com.study.boardfinaltemplate.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @CurrentUser 를 사용한 User에 현재 로그인 한 User 주입하는 Argument Resolver
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
        boolean hasUserType = User.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String token = null;
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("AUTH-TOKEN".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            return null;
        }
        Claims claims = JwtUtils.getClaims(token);
        int loginUserSeq = Integer.parseInt(claims.get("userSeq").toString());
        User user = userService.getUserBySeq(loginUserSeq);

        return user;
    }

}
