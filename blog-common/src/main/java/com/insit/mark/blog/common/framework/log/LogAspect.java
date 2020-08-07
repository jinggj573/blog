package com.insit.mark.blog.common.framework.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.insit.mark.blog.common.framework.log.SysLog)")
    public void log(){}

    @Around("log()")
    public Object handleLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("==========Start LogAspect==========");
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String methodName = joinPoint.getSignature().getName();
        log.info("{} method start invoke,parameter is:{}",joinPoint.toLongString(),args);

        String url=request.getRequestURL().toString();
        String parmeters= ReflectionToStringBuilder.toString(joinPoint.getArgs());
        String host=request.getRemoteHost();
        String uri=request.getRequestURI();
        log.info("url:{} parmeters:{} host:{},methodName:{}",url,parmeters,host,methodName);
        log.info("uri:{}",uri);

        try {
            Object result = joinPoint.proceed();
            Long endTime = System.currentTimeMillis();
            log.info("{} method  invoke success, result:{}, time is:{}",joinPoint.toLongString(),(result!=null?ReflectionToStringBuilder.toString(result):" "),(endTime-startTime));
            return result;
        }catch (Throwable throwable){
            log.error("{}method invoke failed,parameter is:{}",joinPoint.toLongString(),ReflectionToStringBuilder.toString(joinPoint.getArgs()));
            throw throwable;

        }finally {
            //增加日志
        }


    }
}
