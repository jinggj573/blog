package com.insit.mark.blog.common.framework.shiro;

import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.constants.Constants;
import com.insit.mark.blog.common.persistence.model.BgBaseResource;
import com.insit.mark.blog.common.persistence.model.BgBaseRole;
import com.insit.mark.blog.common.persistence.model.BgBaseUser;
import com.insit.mark.blog.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    UserService service;

    @Autowired
    @Lazy
    ResourcesService resourcesService;

    @Autowired
    @Lazy
    RoleService roleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        BgBaseUser user= (BgBaseUser) SecurityUtils.getSubject().getPrincipal();
        List<BgBaseResource> resourcesList=resourcesService.findUserResources(user.getUserName());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<BgBaseRole> roleList=roleService.findRole(user.getUserName());
        roleList.forEach(role -> info.addRole(role.getRoleName()));
        resourcesList.forEach(resources-> info.addStringPermission(resources.getUrl()));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String userName =token.getUsername();
        String password= String.valueOf(token.getPassword());
        BgBaseUser user=  service.findUser(userName);
        if(ObjectUtils.isNull(user)){
            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (Constants.blog_USER_STATUS_0.equals(user.getUserStatus())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassWord(), ByteSource.Util.bytes(password),getName());
        return info;
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
