package com.hcm.common.constants;

/**
 * 通用常量信息
 *
 * @author pc
 */
public class CommonConstants {

    /**
     * 验证码 类型 string
     */
    public static final String CAPTCHA_TYPE_STRING = "string";

    /**
     * 验证码 类型 math
     */
    public static final String CAPTCHA_TYPE_MATH = "math";


    /**
     * 异常处理 controller request url
     */
    public static final String SECURITY_AUTH_ERROR_PATH = "/error/throw";

    /**
     * 异常处理 controller request Attribute
     */
    public static final String SECURITY_AUTH_ERROR_ATTRIBUTE = "SecurityAuthError";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 时间格式：年月天
     */
    public static final String TIME_FORMAT_MONTH_DAY = "YYYYMMdd";

    /**
     * 时间格式：年月天小时分钟
     */
    public static final String TIME_FORMAT_DAY_MINUTES = "YYYYMMddHHmm";

    /**
     * 时间格式：年月天小时分钟秒
     */
    public static final String TIME_FORMAT_DAY_HOURS = "YYYYMMddHH";
}