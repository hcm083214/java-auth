package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 散列缓存
 *
 * @author pc
 * @date 2023/05/01
 */
@Component
public class RedisHashCache {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Hash 添加map对象
     *
     * @param key   关键
     * @param value 价值
     */
    public void putAll(String key, Map<String, Object> value) {
        // 对应 hmset
        redisTemplate.opsForHash().putAll(key, value);
    }

    /**
     * Hash 添加map对象内的值
     *
     * @param key     关键
     * @param hashKey 散列键
     * @param value   价值
     */
    public void put(String key, String hashKey, Object value) {
        // 对应 hset
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 不存在新增
     *
     * @param key     关键
     * @param hashKey 散列键
     * @param value   价值
     */
    public void putIfAbsent(String key, String hashKey, Object value) {
        // hsetx
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 通过 key 和 filed 得到值
     *
     * @param key     key
     * @param hashKey filed
     * @return {@link Object}
     */
    public Object get(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 通过 hscan 分步得到 Cursor
     *
     * @param key     关键
     * @param pattern 模式
     * @param count   数
     * @return {@link Cursor}
     */
    public Cursor get(String key, String pattern, Integer count) {
        // hscan
        Cursor scan = redisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().match(pattern).count(count).build());
        return scan;
    }

    /**
     * 判断 key 中的 filed 是否存在
     *
     * @param key     key
     * @param hashKey filed
     * @return {@link Boolean}
     */
    public Boolean isExists(String key, String hashKey) {
        //        hexists
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 根据 key 得到所有的数据
     *
     * @param key 关键
     * @return {@link Map}<{@link Object}, {@link Object}>
     */
    public Map<String, Object> entries(String key) {
        // hgetall
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除键
     *
     * @param key      关键
     * @param hashKeys 散列键
     */
    public void deleKey(String key, Object... hashKeys) {
        // hdel
        redisTemplate.opsForHash().delete(key, hashKeys);
    }


}
