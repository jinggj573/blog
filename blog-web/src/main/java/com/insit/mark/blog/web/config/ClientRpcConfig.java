package com.insit.mark.blog.web.config;

import com.insit.mark.blog.common.business.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author Administrator
 */
@Configuration
public class ClientRpcConfig {

    @Bean
    public HttpInvokerProxyFactoryBean remotingUserService(){
        HttpInvokerProxyFactoryBean factoryBean=new HttpInvokerProxyFactoryBean();
        factoryBean.setServiceInterface(UserService.class);
        factoryBean.setServiceUrl("http://127.0.0.1:8080/blog-business/remotingUserService");
        return factoryBean;
    }
}
