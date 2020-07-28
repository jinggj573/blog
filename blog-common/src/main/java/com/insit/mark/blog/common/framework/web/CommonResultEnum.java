package com.insit.mark.blog.common.framework.web;

public enum CommonResultEnum {

    OK("200", "OK"),
    SYSTEM_ERROR("0000","系统异常"),
    NOT_SING_IN("203", "用户未登录或身份异常");
    private String code;
    private String msg;

    CommonResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
