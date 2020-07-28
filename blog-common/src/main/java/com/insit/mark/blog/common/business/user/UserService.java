package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.common.persistence.model.User;

import java.util.List;

public interface UserService {

    User findUser(String userName);

    /****
     * 登录的处理方法
     * @return
     */
    CommonResult login(User user,String captchaCode,String captchaKey);


}
