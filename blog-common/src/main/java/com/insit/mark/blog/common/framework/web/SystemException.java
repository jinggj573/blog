package com.insit.mark.blog.common.framework.web;

/**
 * @author Administrator
 */
public class SystemException extends RuntimeException {
    private String code;

    public SystemException(CommonResultEnum resultStatusCode) {
        super(resultStatusCode.getMsg());
        this.code = resultStatusCode.getCode();
    }


    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(String message) {
        super(message);
        this.code = CommonResultEnum.SYSTEM_ERROR.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
