package com.insit.mark.blog.common.framework.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult  handle(Exception exception, HttpServletRequest request) {
        log.error("get the url:{}",request.getRequestURL());
        log.error("get the method :{}",request.getMethod());
        if(exception instanceof SystemException){
            SystemException systemException=(SystemException)exception;
            return new CommonResult(CommonResult.FAILED_CODE,null,systemException.getMessage());
        }else{
            log.error("Unknown exception",exception);
            return new CommonResult(CommonResult.FAILED_CODE, null,exception.getMessage());
        }
    }

    public boolean isAjaxRequest(HttpServletRequest request){
        return (request.getHeader("X-Requested-With")) !=null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString());
    }
}
