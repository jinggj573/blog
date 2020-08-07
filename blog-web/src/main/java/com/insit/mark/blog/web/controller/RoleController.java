package com.insit.mark.blog.web.controller;

import com.insit.mark.blog.common.business.user.RoleService;
import com.insit.mark.blog.common.framework.log.SysLog;
import com.insit.mark.blog.common.framework.web.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @SysLog
    @PostMapping(value = "/index")
    @ResponseBody
    public CommonResult getRouter(){
        //return new CommonResult<>(CommonResult.SUCCESS_CODE,resourcesService.getMenu());
        return null;
    }
}
