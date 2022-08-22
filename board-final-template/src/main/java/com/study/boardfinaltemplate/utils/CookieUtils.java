package com.study.boardfinaltemplate.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 쿠키 관련 기능 지원 클래스
 */
public class CookieUtils {

    /**
     * 로그인 후 이동할 페이지 url을 쿠키에 저장한다.
     *
     * @param url : 로그인 후 이동할 페이지 url
     * @param response
     */
    public static void setNextLoginPageUrl(String url, HttpServletResponse response) {

        Cookie cookie = new Cookie("URL-BEFORE-LOGIN", url);
        cookie.setMaxAge(3 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

}
