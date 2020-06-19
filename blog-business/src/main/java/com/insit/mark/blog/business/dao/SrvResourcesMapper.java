package com.insit.mark.blog.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.insit.mark.blog.common.persistence.model.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SrvResourcesMapper extends BaseMapper<Resources> {

    List<Resources> findResource(@Param("userName") String userName);

}