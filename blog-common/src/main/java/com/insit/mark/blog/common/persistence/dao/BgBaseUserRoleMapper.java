package com.insit.mark.blog.common.persistence.dao;

import com.insit.mark.blog.common.persistence.model.BgBaseUserRole;

public interface BgBaseUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BgBaseUserRole record);

    int insertSelective(BgBaseUserRole record);

    BgBaseUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BgBaseUserRole record);

    int updateByPrimaryKey(BgBaseUserRole record);
}