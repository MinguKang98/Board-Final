package com.study.boardfinalback.jwt;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * JWT 추출 클래스
 */
@Component
public class JwtExtractor {

    private final String EMPTY_STRING = "";

    /**
     * request를 받아 request에서 token 을 추출한다.
     *
     * @param request
     * @return : return JWT. 만약 Autorization 헤더의 값이 없다면 return EMPTY_STRING
     */
    public String extract(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return EMPTY_STRING;
        }

        return authHeader.substring("Bearer ".length());
    }

}
