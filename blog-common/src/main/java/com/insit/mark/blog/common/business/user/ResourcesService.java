package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.persistence.model.Resources;

import java.util.List;

public interface ResourcesService {

    List<Resources> findUserResources(String userName);
}
