package com.hcm.common.vo;

import lombok.Data;

/**
 * @author pc
 */
@Data
public class CaptchaVo {
    private static final long serialVersionUID = 1L;
    /**
     * base64编码的图片字符串
     */
    private String base64Url;

    private String uuid;

    /**
     * captchaEnabled 是否需要使用验证码登录
     */
    private Boolean captchaEnabled;
}
