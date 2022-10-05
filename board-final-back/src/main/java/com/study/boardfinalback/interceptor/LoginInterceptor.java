package com.study.boardfinalback.interceptor;

import com.study.boardfinalback.annotation.LoginRequired;
import com.study.boardfinalback.error.users.AuthenticationException;
import com.study.boardfinalback.jwt.JwtExtractor;
import com.study.boardfinalback.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 로그인 여부 확인하는 Interceptor
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;

    /**
     * @LoginRequired이 붙은 메서드의 로그인 여부 확인
     *
     * @param request
     * @param response
     * @param handler
     * @return @LoginRequired가 없거나 로그인이 되어있으면 true, 아니라면 thrwo Exception
     * @throws Exception : throws AuthenticationException if token is empty or JwtException if token is invalid
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (Objects.isNull(loginRequired)) {
            return true;
        }

        String token = jwtExtractor.extract(request);
        if (token.isEmpty()) {
            throw new AuthenticationException();
        }

        if (!jwtProvider.validateToken(token)) {
            throw new JwtException("올바르지 않은 형식의 JWT 입니다.");
        }

        int userSeq = Integer.parseInt(jwtProvider.getClaims(token).get("userSeq").toString());
        request.setAttribute("userSeq", userSeq);

        return true;
    }

}
