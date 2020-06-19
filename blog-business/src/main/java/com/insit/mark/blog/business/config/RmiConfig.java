package com.insit.mark.blog.business.config;

import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.business.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/12 14:48
 * @Version 1.0
 **/
@Configuration
public class RmiConfig {

    @Bean("/remotingUserService")
    public HttpInvokerServiceExporter remotingService(UserService userService){
        HttpInvokerServiceExporter exporter =new HttpInvokerServiceExporter();
        exporter.setService(userService);
        exporter.setServiceInterface(UserService.class);
        return exporter;
    }


    @Bean("/remotingRoleService")
    public HttpInvokerServiceExporter remotingRoleService(RoleService roleService){
        HttpInvokerServiceExporter exporter =new HttpInvokerServiceExporter();
        exporter.setService(roleService);
        exporter.setServiceInterface(RoleService.class);
        return exporter;
    }

}
