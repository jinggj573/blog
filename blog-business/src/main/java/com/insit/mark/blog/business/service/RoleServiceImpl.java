package com.insit.mark.blog.business.service;

import com.insit.mark.blog.business.dao.SrvRoleMapper;
import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.persistence.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    SrvRoleMapper roleMapper;

    public List<Role> findRole(String userName) {
        return roleMapper.findRole(userName);
    }
}
