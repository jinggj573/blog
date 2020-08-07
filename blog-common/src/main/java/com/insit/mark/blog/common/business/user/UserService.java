package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.common.persistence.model.BgBaseUser;

public interface UserService {

    BgBaseUser findUser(String userName);

    /****
     * 登录的处理方法
     * @return
     */
    CommonResult login(BgBaseUser user, String captchaCode, String captchaKey);


}
