package com.insit.mark.blog.common.framework.shiro;

import com.insit.mark.blog.common.framework.jwt.JwtAuthFilter;
import com.insit.mark.blog.common.framework.redis.RedisDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shiro 配置类
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Autowired
    private RedisDto redisDto;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisDto.getHost());
        redisManager.setPort(redisDto.getPort());
        redisManager.setExpire(1800);
        redisManager.setTimeout(redisDto.getTimeout());
        return redisManager;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * shiro的过滤器
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        log.info("==========Start ShiroConfiguration shirFilter==========");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();


        Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
        filtersMap.put("authcToken", new JwtAuthFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        filterChainDefinitionMap.put("/login/checkLogin", "anon");
        filterChainDefinitionMap.put("/remotingUserService", "anon");
        filterChainDefinitionMap.put("/login/captchaCode/create", "anon");

        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "authcToken");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置securityManager 安全管理器
     * DefaultWebSecurityManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        // 禁用session
        ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO())
                .getSessionStorageEvaluator()).setSessionStorageEnabled(false);
        securityManager.setSubjectFactory(new AgileSubjectFactory());
        return securityManager;
    }


    /**
     * 配置自定义认证器
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public Realm userRealm() {
        /*ShiroRealm userRealm = new ShiroRealm();*/
        JwtRealm userRealm = new JwtRealm();

        /*userRealm.setCredentialsMatcher(hashedCredentialsMatcher());*/
        /**
         * 6号 问题
         * org.apache.shiro.authc.AuthenticationException: Authentication failed for token submission [JwtToken(token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTQwMjg5OTcsInVzZXJuYW1lIjoiYWRtaW4ifQ.92XtOpavdAa-CGHCKPHnvnvzHStjZpT_tQPgx41y5i8, id=1, account=admin, password=admin, name=admin)].
         * Possible unexpected error? (Typical or expected login exceptions should extend from AuthenticationException).
         *
         * Caused by: org.apache.shiro.codec.CodecException: The org.apache.shiro.authc.credential.HashedCredentialsMatcher
         * implementation only supports conversion to byte[] if the source is of type byte[], char[], String, org.apache.shiro.util.ByteSource
         * File or InputStream.  The instance provided as a method argument is of type [com.insit.mark.blog.common.framework.jwt.JwtToken].
         * If you would like to convert this argument type to a byte[], you can 1) convert the argument to one of the supported types yourself and
         * then use that as the method argument or 2)
         * subclass org.apache.shiro.authc.credential.HashedCredentialsMatcherand override the objectToBytes(Object o) method.
         */
        userRealm.setCredentialsMatcher(new CredentialsMatcher());
        return userRealm;
    }

    /**
     * 配置加密方式
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }



    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return redisSessionDAO;
    }

    /**
     * Session ID 生成器
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }


    /**
     * Session Manager 自定义sessionManager
     */
    @Bean
    public SessionManager sessionManager() {
        MySessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }


    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManagers() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     * 这个地方变为静态static方法。否则请求的时候会报 Could not get a resource from the pool,
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public JwtAuthFilter jwtFilter() {
        return new JwtAuthFilter();
    }


}
