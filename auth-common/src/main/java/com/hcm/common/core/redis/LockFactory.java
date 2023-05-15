package com.hcm.common.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * LockFactory
 *
 * @author pc
 * @date 2023/05/08
 */
@Component
public class LockFactory {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public  RedisReentrantLock getLock(){
        String lockName = "lockname";
        return new RedisReentrantLock(stringRedisTemplate,lockName);
    }
}
