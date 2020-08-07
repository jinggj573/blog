package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.persistence.model.BgBaseRole;

import java.util.List;

public interface RoleService {

    List<BgBaseRole> findRole(String userName);
}
