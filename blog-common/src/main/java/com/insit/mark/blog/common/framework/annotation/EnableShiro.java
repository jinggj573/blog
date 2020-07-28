package com.insit.mark.blog.common.framework.annotation;

import com.insit.mark.blog.common.framework.redis.RedisDto;
import com.insit.mark.blog.common.framework.shiro.ShiroConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ShiroConfig.class, RedisDto.class})
public @interface EnableShiro {
}
