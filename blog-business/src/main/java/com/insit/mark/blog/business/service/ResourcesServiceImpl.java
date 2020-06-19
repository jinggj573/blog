package com.insit.mark.blog.business.service;

import com.insit.mark.blog.business.dao.SrvResourcesMapper;
import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.persistence.model.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    SrvResourcesMapper srvResourcesMapper;

    public List<Resources> findUserResources(String userName) {
        return srvResourcesMapper.findResource(userName);
    }
}
