package com.insit.mark.blog.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.insit.mark.blog.common.persistence.model.BgBaseRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SrvRoleMapper extends BaseMapper<BgBaseRole> {

    List<BgBaseRole> findRole(@Param("userName") String userName);
}
