package com.insit.mark.blog.common.framework.jwt;

import lombok.Builder;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@Builder
public class JwtToken implements AuthenticationToken {

    /**
     * token
     */
    private String token;

    /**
     * id
     */
    private Integer id;

    /**
     * 帐号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String name;


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
