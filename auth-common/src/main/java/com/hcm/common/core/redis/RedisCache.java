package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis缓存
 *
 * @author pc
 * @date 2023/02/22
 */
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public<T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 获取缓存的对象
     *
     * @param key 缓存的键值
     * @return {@link Object}
     */
    public<T> T getCacheObject(String key){
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key key
     */
    public boolean deleteObject(String key)
    {
        return redisTemplate.delete(key);
    }
}
