package com.hcm.common.exception;

/**
 * @author pc
 */
public class AuthException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误提示
     */
    private Integer code;

    public AuthException(String message) {
        this.message = message;
    }

    public AuthException(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public AuthException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
