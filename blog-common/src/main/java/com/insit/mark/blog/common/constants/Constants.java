package com.insit.mark.blog.common.constants;

/***
 * 常用变量
 */
public class Constants {

    public static final String blog_USER_STATUS_1="1";

    public static final String blog_USER_STATUS_0="0";

    /**
     * redis中验证码key
     */
    public static final String CAPTCHA_CODE_KEY = "admin_captcha_code:";

    /**
     * redis中验证码有效期3分钟（单位：秒）
     */
    public static final long CAPTCHA_EXPIRATION = 60 * 3;

}
