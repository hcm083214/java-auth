package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Hyper LogLog cache
 *
 * @author pc
 * @date 2023/05/03
 */
@Component
public class RedisHPCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加
     *
     * @param key     关键
     * @param element 元素
     */
    public void add(String key, Object... element) {
        redisTemplate.opsForHyperLogLog().add(key, element);
    }

    /**
     * 数量
     *
     * @param keys 键
     * @return {@link Long}
     */
    public Long count(String... keys) {
        return redisTemplate.opsForHyperLogLog().size(keys);
    }


}
