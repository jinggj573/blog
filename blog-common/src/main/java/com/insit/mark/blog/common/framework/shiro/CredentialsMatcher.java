package com.insit.mark.blog.common.framework.shiro;

import com.insit.mark.blog.common.framework.jwt.JwtToken;
import com.insit.mark.blog.common.framework.jwt.JwtUtil;
import com.insit.mark.blog.common.utils.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * <p>
 * 自定义密码匹配器
 * </p>
 *
 * @author lanjerry
 * @since 2019-09-09
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        Object accountCredentials = getCredentials(info);
        if (StringUtils.isNotBlank(jwtToken.getPassword())) {
            Object tokenCredentials = Md5Util.encode(jwtToken.getPassword(), jwtToken.getId().toString());
            if (!accountCredentials.equals(tokenCredentials)) {
                throw new DisabledAccountException("密码不正确");
            }
        } else {
            if (!JwtUtil.verify(jwtToken.getToken(), accountCredentials.toString())) {
                throw new DisabledAccountException("身份认证已失效，请重新登录");
            }
        }
        return true;
    }
}