package com.study.boardfinalback.apiController;

import com.study.boardfinalback.domain.user.User;
import com.study.boardfinalback.error.users.AuthenticationException;
import com.study.boardfinalback.service.UserService;
import com.study.boardfinalback.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new AuthenticationException();
        }

        String token = authHeader.substring(7);
        Claims claims = JwtUtils.getClaims(token);
        int loginUserSeq = Integer.parseInt(claims.get("userSeq").toString());
        User user = userService.getUserBySeq(loginUserSeq);

        return user;
    }

}
