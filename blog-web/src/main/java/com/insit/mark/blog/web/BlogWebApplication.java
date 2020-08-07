package com.insit.mark.blog.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 */
@Slf4j
@MapperScan({"com.insit.mark.blog.*.dao","com.insit.mark.blog.common.persistence.dao"})
@ComponentScan({"com.insit.mark.blog.*.*"})
@SpringBootApplication
public class BlogWebApplication  {

    public static void main(String[] args) {
        SpringApplication.run(BlogWebApplication.class, args);
    }

}
