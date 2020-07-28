package com.insit.mark.blog.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.insit.mark.blog.business.dao.SrvUserMapper;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.constants.Constants;
import com.insit.mark.blog.common.framework.jwt.JwtToken;
import com.insit.mark.blog.common.framework.web.CommonResult;
import com.insit.mark.blog.common.persistence.model.User;
import com.insit.mark.blog.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    SrvUserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User findUser(String userName) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",userName));
    }

    /**
     * 获取前台用户名密码，查询数据库汇总是否存在 比对密码，返回登录结果
     */
    @Override
    public CommonResult login(User user,String captchaCode,String captchaKey) {
        String redisCaptchaCode=stringRedisTemplate.opsForValue().get(Constants.CAPTCHA_CODE_KEY.concat(captchaKey));
        log.info("redis get the captchaCode is:{}",redisCaptchaCode);
        if(!captchaCode.toLowerCase().equals(redisCaptchaCode.toLowerCase())){
            return new CommonResult<>(CommonResult.FAILED_CODE,"验证码不正确或已过期");
        }
        Subject subject = SecurityUtils.getSubject();
        JwtToken token = JwtToken.builder().account(user.getUsername()).password(user.getPassword()).build();
        try {
            subject.login(token);
            return new CommonResult<>(CommonResult.SUCCESS_CODE,token.getToken(),"登录成功");
        }catch (IncorrectCredentialsException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult<>(CommonResult.FAILED_CODE,null,"验证未通过，错误的凭证");
        }catch (UnknownAccountException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult<>(CommonResult.FAILED_CODE,null,"验证未通过，未知账户！");
        }catch (LockedAccountException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult<>(CommonResult.FAILED_CODE,null,"验证未通过，账户锁定！");
        }catch (ExcessiveAttemptsException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult<>(CommonResult.FAILED_CODE,null,"验证未通过，错误次数太多！");
        }catch (AuthenticationException e){
            log.error("LoginController checkLogin method has exception:{},{}",e.getMessage(),e);
            return new CommonResult<>(CommonResult.FAILED_CODE,null,"验证未通过，用户名、密码不正确！");
        }
    }


}
