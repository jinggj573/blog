package com.insit.mark.blog.web.controller;

import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.web.config.PasswordHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;


@Api(tags = "LoginController 部分")
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @ApiOperation(value = "登录验证",notes = "方法备注？一般写什么啊")
    @RequestMapping(value = "/checkLogin", method = RequestMethod.GET)
    public String checkLogin(){
        return "hello";
    }


    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "/findUser",method = RequestMethod.POST)
    public ModelMap findUser(@RequestParam(value = "userName") String userName){
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("userName", userName);
        return modelMap;
    }

    @RequestMapping(value = "/checkLogin")
    public CommonResult checkLogin(String userName, String passWord){
        UsernamePasswordToken token=new UsernamePasswordToken(userName, passWord);
        Subject subject= SecurityUtils.getSubject();

        try {
            subject.login(token);
            log.info("是否登录==>{}", subject.isAuthenticated());
        }catch (IncorrectCredentialsException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult(CommonResult.FAILED_CODE,null,"验证未通过，错误的凭证");
        }catch (UnknownAccountException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult(CommonResult.FAILED_CODE,null,"验证未通过，未知账户！");
        }catch (LockedAccountException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult(CommonResult.FAILED_CODE,null,"验证未通过，账户锁定！");
        }catch (ExcessiveAttemptsException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult(CommonResult.FAILED_CODE,null,"验证未通过，错误次数太多！");
        }catch (AuthenticationException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult(CommonResult.FAILED_CODE,null,"验证未通过，用户名、密码不正确！");
        }
        if(subject.isAuthenticated()){
            log.info("认证成功==>");
            return new CommonResult(CommonResult.SUCCESS_CODE,null,"认证成功==>");
        }else{
            return new CommonResult(CommonResult.SUCCESS_CODE,null,"认证成功==>");
        }
    }

}
