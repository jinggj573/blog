package com.insit.mark.blog.common.framework.web;


import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class CommonResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static final String SUCCESS="success";
    public static final String FAILED="failed";

    /**
     * 1 失败  0 成功
     */
    public static final Integer SUCCESS_CODE=0;
    public static final Integer FAILED_CODE=1;

    public CommonResult(Integer code,T data, String msg) {
        this.code=code;
        this.data=data;
        this.msg=msg;
    }

    public CommonResult(Integer code,T data) {
        this.code=code;
        this.data=data;
    }
    public CommonResult(Integer code,String msg) {
        this.code=code;
        this.msg=msg;
    }


}
