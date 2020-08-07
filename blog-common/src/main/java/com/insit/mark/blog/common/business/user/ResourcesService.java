package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.persistence.model.BgBaseResource;

import java.util.List;

public interface ResourcesService {

    List<BgBaseResource> findUserResources(String userName);

    List<BgBaseResource> getMenu();
}
