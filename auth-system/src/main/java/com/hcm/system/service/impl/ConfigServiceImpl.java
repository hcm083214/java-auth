package com.hcm.system.service.impl;

import com.hcm.common.constants.CacheConstants;
import com.hcm.common.constants.CommonConstants;
import com.hcm.common.core.entity.SysConfig;
import com.hcm.common.core.redis.RedisCache;
import com.hcm.common.utils.ConvertUtils;
import com.hcm.common.utils.StringUtils;
import com.hcm.system.mapper.ConfigMapper;
import com.hcm.system.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配置服务impl
 *
 * @author pc
 * @date 2023/02/24
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 得到系统配置
     *
     * @param configKey 配置的键
     * @return {@link String}
     */
    @Override
    public String getSysConfigValByKey(String configKey) {
        String configValue = ConvertUtils.toStr(redisCache.getCacheObject(configKey));
        if (StringUtils.isEmpty(configValue)) {
            SysConfig config = configMapper.getSysConfigByKey(configKey);
            if (config != null && StringUtils.isNotEmpty(config.getConfigValue())) {
                configValue = config.getConfigValue();
                redisCache.setCacheObject(getCacheKey(configKey), config.getConfigValue());
            }
        }
        return configValue;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean getCaptchaEnabled() {
        String captchaEnabled = getSysConfigValByKey(CommonConstants.CAPTCHA_SYS_CONFIG_KEY);
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return ConvertUtils.toBoolean(captchaEnabled);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.CACHE_SYS_CONFIG_KEY + configKey;
    }
}
