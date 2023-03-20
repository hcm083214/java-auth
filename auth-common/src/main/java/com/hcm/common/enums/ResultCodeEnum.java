package com.hcm.common.enums;

/**
 * @author pc
 */

public enum ResultCodeEnum {
    // 请求成功
    SUCCESS(200, "请求成功"),
    // 请求失败
    FAILED(400, "请求失败"),
    // 没有权限
    UNAUTHORIZED(401,"没有权限"),
    // token过期
    TOKENEXPIRATION(401,"token过期"),
    // 接口未授权
    UNACCESS(403,"接口未授权");

    private final int code;
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
