package com.hcm.common.vo;

import com.hcm.common.annotations.EnumValue;
import com.hcm.common.enums.TripartiteSourceEnum;
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
     * 验证码，或者第三方传过来的 code
     */
    private String code;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 令牌
     */
    private String token;

    /**
     * 授权页面url
     */
    private String authorizeUrl;

    /**
     * 第三方源
     */
    @EnumValue(enumClass = TripartiteSourceEnum.class, ignoreCase = true, message = "传入的参数不对")
    private String source;
}
