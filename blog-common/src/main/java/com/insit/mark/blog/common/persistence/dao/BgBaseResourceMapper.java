package com.insit.mark.blog.common.persistence.dao;

import com.insit.mark.blog.common.persistence.model.BgBaseResource;

public interface BgBaseResourceMapper {
    int deleteByPrimaryKey(Integer resourceId);

    int insert(BgBaseResource record);

    int insertSelective(BgBaseResource record);

    BgBaseResource selectByPrimaryKey(Integer resourceId);

    int updateByPrimaryKeySelective(BgBaseResource record);

    int updateByPrimaryKey(BgBaseResource record);
}