package com.insit.mark.blog.common.persistence.dao;

import com.insit.mark.blog.common.persistence.model.BgBaseUser;

public interface BgBaseUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(BgBaseUser record);

    int insertSelective(BgBaseUser record);

    BgBaseUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(BgBaseUser record);

    int updateByPrimaryKey(BgBaseUser record);
}