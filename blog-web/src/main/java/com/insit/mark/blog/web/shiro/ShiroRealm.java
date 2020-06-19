package com.insit.mark.blog.web.shiro;

import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.constants.Constants;
import com.insit.mark.blog.common.persistence.model.Resources;
import com.insit.mark.blog.common.persistence.model.Role;
import com.insit.mark.blog.common.persistence.model.User;
import com.insit.mark.blog.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        User user= (User) SecurityUtils.getSubject().getPrincipal();
        List<Resources> resourcesList=resourcesService.findUserResources(user.getUsername());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<Role> roleList=roleService.findRole(user.getUsername());
        roleList.forEach(role -> info.addRole(role.getRoledesc()));
        resourcesList.forEach(resources-> info.addStringPermission(resources.getResurl()));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String userName =token.getUsername();
        String password= String.valueOf(token.getPassword());
        User user=  service.findUser(userName);
        if(ObjectUtils.isNull(user)){

            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (Constants.blog_USER_STATUS_0.equals(user.getEnable())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(password),getName());
        return info;
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }
}
