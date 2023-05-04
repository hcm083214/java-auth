package com.hcm.controller.redisTest;

import com.hcm.common.core.redis.RedisStringCache;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字符串测试
 *
 * @author pc
 * @date 2023/05/01
 */
@Api("redis string test")
@Slf4j
@RestController
@RequestMapping("/test")
public class StringTest {
    @Autowired
    private RedisStringCache redisStringCache;

   public void test(){

   }
}
