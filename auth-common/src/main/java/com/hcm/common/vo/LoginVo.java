package com.hcm.common.vo;

import lombok.Data;

/**
 * @author pc
 */
@Data
public class LoginVo {
    private static final long serialVersionUID = 1L;

    /**
     * 登陆名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否记住用户名和密码
     */
    private Boolean rememberMe;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码的uuid
     */
    private String uuid;

    private String token;
}
