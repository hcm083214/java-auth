package com.hcm.controller.concurrentTest;


import com.hcm.common.core.redis.LockFactory;
import com.hcm.common.core.redis.RedisReentrantLock;
import com.hcm.common.core.redis.RedisStringCache;
import com.hcm.common.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 并发测试
 *
 * @author pc
 * @date 2023/05/07
 */
@Api("ConcurrentTest test")
@Slf4j
@RestController
@RequestMapping("/test")
public class ConcurrentTest {
    @Resource(name = "threadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    private final String KEY = "shops:001";

    private final String LOCKNAME = "lockname";
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private RedisStringCache redisStringCache;

    @Autowired
    private LockFactory lockFactory;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String port;

    @GetMapping("/concurrent")
    @ApiOperation(value = "分布式锁", notes = "分布式锁测试")
    public void test() {
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            RedisReentrantLock lock = lockFactory.getLock();

            threadPoolExecutor.execute(() -> {
                lock.lock();
                try {
                    lock.lock();
                    try {
                        Integer shopsNum = redisStringCache.getCacheObject(KEY);
                        Integer num = StringUtils.isNull(shopsNum) ? 0 : shopsNum;
                        if (num > 0) {
                            num--;
                            redisStringCache.setCacheObject(KEY, num);
                            log.info("port:{},index:{},sale 1, remain :{}", port, finalI, num);
                        } else {
                            log.info("port:{},index:{},all sell out", port, finalI);
                        }
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    private void lock(String lockname, String keyVal) {
        // 加锁并设置有效期，防止死锁
        // while (!redisStringCache.setIfAbsent(lockname, keyVal, 30L, TimeUnit.MILLISECONDS)) {
        while (!isGetLock(lockname, keyVal)) {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean isGetLock(String lockname, String keyVal) {
        String script =
                "if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],ARGV[1]) == 1 then " +
                        "redis.call('hincrby',KEYS[1],ARGV[1],1) " +
                        "redis.call('expire',KEYS[1],ARGV[2]) " +
                        "return 1 " +
                        "else " +
                        "return 0 " +
                        "end";
        return stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockname), keyVal, String.valueOf(30));
    }

    private void unlock(String lockname, String keyVal) {
        // 判断加锁与解锁是不是同一个，保证安全性
//        if (redisStringCache.getCacheObject(lockname).equals(keyVal)) {
//            redisStringCache.deleteObject(lockname);
//        }
        String script =
                "if redis.call('HEXISTS',KEYS[1],ARGV[1]) == 0 then " +
                        "return nil " +
                        "elseif redis.call('HINCRBY',KEYS[1],ARGV[1],-1) == 0 then " +
                        "return redis.call('del',KEYS[1]) " +
                        "else " +
                        "return 0 " +
                        "end";
        Long flag = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(lockname), keyVal, String.valueOf(30));
        if (flag == null) {
            throw new RuntimeException("This lock doesn't EXIST");
        }
    }

}
