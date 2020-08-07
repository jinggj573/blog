package com.insit.mark.blog.business.service;

import com.insit.mark.blog.business.dao.SrvResourcesMapper;
import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.framework.jwt.JwtToken;
import com.insit.mark.blog.common.framework.web.SystemException;
import com.insit.mark.blog.common.persistence.model.BgBaseResource;
import com.insit.mark.blog.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    SrvResourcesMapper srvResourcesMapper;

    @Override
    public List<BgBaseResource> findUserResources(String userName) {
        return srvResourcesMapper.findResource(userName);
    }

    @Override
    public List<BgBaseResource> getMenu() {
        JwtToken token = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        if(ObjectUtils.isEmpty(token)){
            throw new SystemException("用户未登录或身份异常");
        }else{
            log.info("getMenu method get the userId id:{},userName is:{}",token.getId(),token.getAccount());
            return srvResourcesMapper.getMenuList(token.getId());
        }
    }


}
