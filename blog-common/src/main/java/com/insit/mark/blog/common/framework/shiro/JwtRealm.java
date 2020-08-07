package com.insit.mark.blog.common.framework.shiro;

import com.insit.mark.blog.common.business.user.ResourcesService;
import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.business.user.UserService;
import com.insit.mark.blog.common.constants.Constants;
import com.insit.mark.blog.common.framework.jwt.JwtToken;
import com.insit.mark.blog.common.framework.jwt.JwtUtil;
import com.insit.mark.blog.common.framework.web.CommonResultEnum;
import com.insit.mark.blog.common.persistence.model.BgBaseResource;
import com.insit.mark.blog.common.persistence.model.BgBaseRole;
import com.insit.mark.blog.common.persistence.model.BgBaseUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    ResourcesService resourcesService;

    @Autowired
    @Lazy
    RoleService roleService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        JwtToken jwtToken = (JwtToken) principalCollection.getPrimaryPrincipal();
        List<BgBaseResource> resourcesList=resourcesService.findUserResources(jwtToken.getAccount());
        List<BgBaseRole> roleList=roleService.findRole(jwtToken.getAccount());
        roleList.forEach(role -> info.addRole(role.getRoleName()));
        resourcesList.forEach(resources-> info.addStringPermission(resources.getUrl()));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String userName = jwtToken.getAccount() != null ? jwtToken.getAccount() : JwtUtil.getAttribute(jwtToken.getToken(), "account");
        BgBaseUser user=  userService.findUser(userName);
        if (user == null) {
            throw new DisabledAccountException(CommonResultEnum.NOT_SING_IN.getMsg());
        }
        if (Constants.blog_USER_STATUS_0.equals(user.getUserStatus())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        jwtToken.setId(user.getUserId());
        if (jwtToken.getAccount() == null) {
            jwtToken.setAccount(user.getUserName());
        }
        if (jwtToken.getName() == null) {
            jwtToken.setName(user.getUserName());
        }
        if (jwtToken.getToken() == null) {
            jwtToken.setToken(JwtUtil.sign(user.getUserId(),user.getUserName(), user.getPassWord()));
        }

        SimpleAuthenticationInfo  info=new SimpleAuthenticationInfo (jwtToken, user.getPassWord() ,getName());

        return  info;
    }
}
