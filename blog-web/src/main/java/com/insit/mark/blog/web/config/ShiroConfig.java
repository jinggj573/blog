package com.insit.mark.blog.web.config;

import com.insit.mark.blog.web.shiro.ShiroRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {

        @Value("${spring.redis.host}")
        private String host;
        @Value("${spring.redis.port}")
        private int port;
        @Value("${spring.redis.timeout}")
        private int timeout;
        @Value("${spring.redis.password}")
        private String password;

        @Bean
        public ShiroFilterChainDefinition shiroFilterChainDefinition() {
            return new DefaultShiroFilterChainDefinition();
        }

        /**
         * shiro的过滤器
         */
        @Bean("shiroFilterFactoryBean")
        public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
                log.info("==========Start ShiroConfiguration shirFilter==========");
                ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
                shiroFilterFactoryBean.setSecurityManager(securityManager);
                shiroFilterFactoryBean.setLoginUrl("/");
                shiroFilterFactoryBean.setSuccessUrl("/login/home");
                shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");
                Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
                filterChainDefinitionMap.put("/login/", "anon");
                filterChainDefinitionMap.put("/login/checkLogin", "anon");
                filterChainDefinitionMap.put("/logout", "logout");
                filterChainDefinitionMap.put("/static/**", "anon");
                filterChainDefinitionMap.put("/blog/views/**", "authc");
                filterChainDefinitionMap.put("/**", "authc");
                shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
                return shiroFilterFactoryBean;
        }

        /**
         * 配置securityManager 安全管理器
         *
         * @return
         */
        @Bean
        public SessionsSecurityManager securityManager(){
            DefaultWebSecurityManager webSessionManager=new DefaultWebSecurityManager();
            webSessionManager.setRealm(userRealm());
            webSessionManager.setSessionManager(sessionManager());
            webSessionManager.setCacheManager(cacheManagers());
            return webSessionManager;
        }


        /**
         * 配置自定义认证器
         */
        @Bean
        @DependsOn("lifecycleBeanPostProcessor")
        public ShiroRealm userRealm(){
            ShiroRealm userRealm = new ShiroRealm();


           /* userRealm.setCachingEnabled(true);
            //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
            userRealm.setAuthenticationCachingEnabled(true);
            //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
            userRealm.setAuthenticationCacheName("authenticationCache");
            //启用授权缓存，即缓存AuthorizationInfo信息，默认false
            userRealm.setAuthorizationCachingEnabled(true);
            //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
            userRealm.setAuthorizationCacheName("authorizationCache");*/



            userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
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
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);
        redisManager.setTimeout(0);
        // redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }


    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }


    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
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
     *开启shiro aop注解支持.
     *   使用代理方式;所以需要开启代码支持;
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     *  这个地方变为静态static方法。否则请求的时候会报 Could not get a resource from the pool,
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }




}
