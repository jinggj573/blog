package com.insit.mark.blog.common.persistence.dao;

import com.insit.mark.blog.common.persistence.model.BgBaseRoleResource;

public interface BgBaseRoleResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BgBaseRoleResource record);

    int insertSelective(BgBaseRoleResource record);

    BgBaseRoleResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BgBaseRoleResource record);

    int updateByPrimaryKey(BgBaseRoleResource record);
}