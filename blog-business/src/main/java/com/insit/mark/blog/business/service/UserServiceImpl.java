package com.insit.mark.blog.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.insit.mark.blog.business.dao.SrvUserMapper;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.persistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    SrvUserMapper userMapper;

    @Override
    public User findUser(String userName) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",userName));
    }
}
