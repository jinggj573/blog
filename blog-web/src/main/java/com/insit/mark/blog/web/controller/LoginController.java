package com.insit.mark.blog.web.controller;

import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.constants.Constants;
import com.insit.mark.blog.common.framework.log.SysLog;
import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.common.persistence.model.BgBaseUser;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author Administrator
 */
@Api(tags = "LoginController 部分")
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    ResourcesService resourcesService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @ResponseBody
    @ApiOperation(value = "登录验证",notes = "方法备注？一般写什么啊")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public CommonResult hello(){
        return new CommonResult<>(CommonResult.SUCCESS_CODE,"hello");
    }


    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "/findUser",method = RequestMethod.POST)
    public ModelMap findUser(@RequestParam(value = "userName") String userName){
        ModelMap modelMap=new ModelMap();
        modelMap.addAttribute("userName", userName);
        return modelMap;
    }


    @GetMapping("/captchaCode/create")
    @ResponseBody
    @ApiOperation(value = "生成图形验证码", position = 10)
    public CommonResult<Map<String, String>> createCaptchaCode() {
        Map<String, String> result = new HashMap<>();
        SpecCaptcha specCaptcha = new SpecCaptcha(111, 36, 4);
        String verCode = specCaptcha.text().toLowerCase();
        String key = String.valueOf(RandomUtils.nextInt());
        stringRedisTemplate.opsForValue().set(Constants.CAPTCHA_CODE_KEY.concat(key), verCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.SECONDS);
        result.put("key", key);
        result.put("image", specCaptcha.toBase64());
        return new CommonResult<>(CommonResult.SUCCESS_CODE,result);
    }

    @SysLog
    @PostMapping(value = "/checkLogin")
    @ResponseBody
    public CommonResult checkLogin(@RequestParam String userName,@RequestParam String passWord,
                                   @RequestParam String captchaCode,@RequestParam String captchaKey){
        BgBaseUser user=new BgBaseUser();
        user.setUserName(userName);
        user.setPassWord(passWord);
        return userService.login(user,captchaCode,captchaKey);
    }

    /**
     *获取当前登录的用户路由信息
     */
    @SysLog
    @PostMapping(value = "/getRouter")
    @ResponseBody
    public CommonResult getRouter(){
        return new CommonResult<>(CommonResult.SUCCESS_CODE,resourcesService.getMenu());
    }



    @SysLog
    @GetMapping("/logout")
    @ResponseBody
    public CommonResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new CommonResult<>(CommonResult.SUCCESS_CODE,null,"成功登出");
    }

    /**
     * 未认证返回前台信息
     */
    @RequestMapping("/unAuth")
    @ResponseBody
    public CommonResult unAuth() {
        return new CommonResult<>(CommonResult.SUCCESS_CODE, null,"用户未登录！");
    }

    /**
     * 未认证返回前台信息
     */
    @RequestMapping("/unAuthorized")
    @ResponseBody
    public CommonResult unAuthorized() {
        return new CommonResult<>(CommonResult.SUCCESS_CODE, null,"用户无权限！");
    }


}
