package com.hcm.controller.redisTest;

import com.hcm.common.core.redis.RedisHashCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 散列测试
 *
 * @author pc
 * @date 2023/05/01
 */
@Api("redis Hash test")
@Slf4j
@RestController
@RequestMapping("/test")
public class HashTest {

    @Resource(name = "threadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private RedisHashCache redisHashCache;

    private final String key = "testBigKey";

    @PostMapping("/big-key")
    @ApiOperation(value = "添加 Hash 类型的大key", notes = "添加 Hash 类型的大key")
    public void setBigKey() {
        threadPoolExecutor.execute(() -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", "zhangsan");
            redisHashCache.putAll(key, hashMap);
            for (int i = 0; i < 2000; i++) {
                redisHashCache.put(key, "k" + i, "v" + i * 10);
            }
        });
    }

    @GetMapping("/big-key")
    @ApiOperation(value = "获取key对应的HashMap", notes = "获取key对应的HashMap")
    public Map<Object, Object> getBigkey() throws InterruptedException, ExecutionException {
        FutureTask<Map<Object, Object>> futureTask = new FutureTask<Map<Object, Object>>(() -> redisHashCache.entries(key));
        threadPoolExecutor.execute(futureTask);
        return futureTask.get();
    }

    @GetMapping("/delete")
    @ApiOperation(value = "获取key对应的HashMap", notes = "获取key对应的HashMap")
    public void delBigKey() {
        threadPoolExecutor.execute(() -> {
            int i = 0;
            Cursor cursor = redisHashCache.get(key, "k*", 100);
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) cursor.next();
                log.info("{}-key:{}-position:{}", i++, entry.getKey(), cursor.getPosition());
                redisHashCache.deleKey(key, entry.getKey());
            }
            cursor.close();
        });

    }


}
