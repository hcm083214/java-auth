package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class RedisStringCache {

    @Autowired
    @Qualifier("redisTemplate")
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
    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 如果没有设置
     *
     * @param key      关键
     * @param value    价值
     * @param timeout  超时
     * @param timeUnit 时间单位
     * @return {@link Boolean}
     */
    public  Boolean setIfAbsent(String key, Object value, Long timeout, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }
    /**
     * 获取缓存的对象
     *
     * @param key 缓存的键值
     * @return {@link Object}
     */
    public <T> T getCacheObject(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key key
     */
    public boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 增加
     *
     * @param key   关键
     * @param delta δ
     */
    public void increase(String key,Integer delta){
        redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 减少
     *
     * @param key   关键
     * @param delta δ
     */
    public void decrease(String key,Integer delta){
        redisTemplate.opsForValue().decrement(key,delta);
    }
    /**
     * 是否存在
     *
     * @param key 关键
     * @return boolean
     */
    public boolean isExists(String key){
        return redisTemplate.hasKey(key);
    }
}
