package com.insit.mark.blog.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.insit.mark.blog.common.persistence.model.BgBaseResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SrvResourcesMapper extends BaseMapper<BgBaseResource> {

    List<BgBaseResource> findResource(@Param("userName") String userName);

    List<BgBaseResource> getMenuList(@Param("userId") Integer userId);
}
