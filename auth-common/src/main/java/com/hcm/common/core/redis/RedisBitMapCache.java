package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * bitmap redis 操作
 *
 * @author pc
 * @date 2023/05/07
 */
@Component
public class RedisBitMapCache {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置位数据
     *
     * @param key    键
     * @param offset 位
     * @param value  价值
     */
    public void setBit(String key,Long offset,Boolean value){
        redisTemplate.opsForValue().setBit(key,offset,value);
    }

    public boolean getBit(String key, Long offset) {
       return redisTemplate.opsForValue().getBit(key,offset);
    }
}
