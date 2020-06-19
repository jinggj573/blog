package com.insit.mark.blog.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.insit.mark.blog.common.persistence.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SrvRoleMapper extends BaseMapper<Role> {

    List<Role> findRole(@Param("userName") String userName);

}