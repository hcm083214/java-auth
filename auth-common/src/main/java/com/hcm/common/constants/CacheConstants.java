package com.hcm.common.constants;

/**
 * 缓存常量
 *
 * @author pc
 * @date 2023/02/22
 */
public class CacheConstants {
    /**
     * 验证码 key
     */
    public static final String CACHE_CAPTCHA_CODE = "captchaCode:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CACHE_CAPTCHA_EXPIRATION = 2;

    /**
     * 参数管理 cache key
     */
    public static final String CACHE_SYS_CONFIG_KEY = "sys_config:";

    /**
     * token key
     */
    public static final String CACHE_LOGIN_TOKEN_KEY = "login_token:";
}
