package com.insit.mark.blog.common.persistence.dao;

import com.insit.mark.blog.common.persistence.model.BgBaseRole;

public interface BgBaseRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(BgBaseRole record);

    int insertSelective(BgBaseRole record);

    BgBaseRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(BgBaseRole record);

    int updateByPrimaryKey(BgBaseRole record);
}