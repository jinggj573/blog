package com.insit.mark.blog.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.insit.mark.blog.*.dao","com.insit.mark.blog.common.persistence.dao"})
@ComponentScan({"com.insit.mark.blog.business.*","com.insit.mark.blog.common.*"})
public class BlogBusinessApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BlogBusinessApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogBusinessApplication.class, args);
    }

}
