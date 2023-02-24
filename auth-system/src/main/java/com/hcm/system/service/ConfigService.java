package com.hcm.system.service;


import org.springframework.stereotype.Service;

/**
 * 配置服务
 *
 * @author pc
 * @date 2023/02/22
 */
public interface ConfigService {

    /**
     * 得到系统配置
     *
     * @param configKey 配置的键
     * @return {@link String}
     */
    String getSysConfigValByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    boolean getCaptchaEnabled();
}
