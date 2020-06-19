package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.persistence.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findRole(String userName);
}
