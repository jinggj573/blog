package com.insit.mark.blog.common.framework.jwt;

import cn.hutool.json.JSONUtil;
import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.common.framework.web.CommonResultEnum;
import com.insit.mark.blog.common.framework.web.SystemException;
import com.insit.mark.blog.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        HttpServletResponse response = WebUtils.toHttp(servletResponse);
        if (!isLoginAttempt(servletRequest, servletResponse)) {
            writerResponse(response, CommonResultEnum.NOT_SING_IN.getCode(), "无身份认证权限标示");
            return false;
        }
        try {
            return executeLogin(servletRequest, servletResponse);
        } catch (SystemException e) {
            writerResponse(response,e.getCode(), e.getMessage());
            return false;
        }
        //return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        JwtToken token = JwtToken.builder().token(this.getAuthorization(request)).build();
        try {
            subject.login(token);
        } catch (DisabledAccountException e) {
            /*throw ApiException.builder().code(ApiResultCodeEnum.NOT_SING_IN.value).msg(e.getMessage()).build();*/
            throw new SystemException(e.getMessage());
        } catch (Exception e) {
            /*throw ApiException.systemError(e.getMessage());*/
            throw new SystemException(e.getMessage());
        }
        return true;
    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return StringUtils.isNotBlank(this.getAuthorization(request));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }

    /**
     * 获取token
     */
    private String getAuthorization(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return req.getHeader("Authorization");
    }

    /**
     * 输出
     */
    private void writerResponse(HttpServletResponse response, String code, String msg) {
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        try {
            response.getWriter().write(JSONUtil.toJsonStr(new CommonResult<>(Integer.parseInt(code),null,msg)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
