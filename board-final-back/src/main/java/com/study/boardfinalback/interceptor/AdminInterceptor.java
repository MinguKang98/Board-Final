package com.study.boardfinalback.interceptor;

import com.study.boardfinalback.domain.users.UserRole;
import com.study.boardfinalback.error.users.AuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 관리자 여부 확인하는 Interceptor
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * 관리자 여부 확인
     *
     * @param request
     * @param response
     * @param handler
     * @return : userRole 이 UserRole.ROLE_ADMIN 이라면 return true
     * @throws Exception : throws AuthorizationException if userRole is not ROLE_ADMIN
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserRole userRole = UserRole.getUserRole(String.valueOf(request.getAttribute("userRole")));
        if (userRole != UserRole.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        return true;
    }

}
