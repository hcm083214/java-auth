package com.hcm.common.exception;

/**
 * @author pc
 */
public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误提示
     */
    private Integer code;

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        this.message = message;
    }

    public BadRequestException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
