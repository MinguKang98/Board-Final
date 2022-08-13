package com.study.boardfinalback.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * request의 error 처리하는 클래스
 */
@Controller
public class CustomErrorController implements ErrorController {

    private String VIEW_PATH = "/errors/";

    /**
     * request의 HttpStatus에 따라 return /errors/{HttpStatus}.html. 해당하는 status가 없으면 return /errors/default.html
     *
     * @param request
     * @return : return /errors/{HttpStatus}.html, 해당하는 HttpStatus 없으면 return /errors/default.html
     */
    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()){
                return VIEW_PATH + "404";
            }
            if(statusCode == HttpStatus.FORBIDDEN.value()){
                return VIEW_PATH + "500";
            }
        }
        return VIEW_PATH + "default";
    }

}
