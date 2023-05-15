package com.hcm.controller.redisTest;

import com.google.common.hash.BloomFilter;
import com.hcm.common.filter.MBloomFilter;
import com.hcm.common.filter.GuavaBloomFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * BitMapTest
 *
 * @author pc
 * @date 2023/05/07
 */
@Api("redis bitmap test")
@Slf4j
@RestController
@RequestMapping("/test")
public class BitMapTest {

    @Autowired
    private MBloomFilter bloomFilter;

    private final String KEY = "testBloomFilter";

    @PostMapping("/init-bloom-filter")
    @ApiOperation(value = "初始化布隆过滤器的数据", notes = "初始化布隆过滤器的数据")
    public void setBlackList() {
        String[] blackList = new String[]{"name1:001", "name2:002", "name3:003"};
        bloomFilter.initData(Arrays.asList(blackList), KEY);
    }

    @PostMapping("/info")
    @ApiOperation(value = "测试布隆过滤器", notes = "测试布隆过滤器")
    public void testBloomFilter() {
        String currentUser1 = "name1:001";
        boolean isBlackUser1 = bloomFilter.checkWithBloomFilter(currentUser1, KEY);
        String currentUser2 = "name4:004";
        boolean isBlackUser2 = bloomFilter.checkWithBloomFilter(currentUser2, KEY);
        log.info("currentUser1 存在：{}，currentUser2 存在：{}", isBlackUser1, isBlackUser2);
    }

    @PostMapping("/guava")
    @ApiOperation(value = "测试Guava布隆过滤器", notes = "测试Guava布隆过滤器")
    public void testGuavaBloomFilter(){
        String[] blackList = new String[]{"name1:001", "name2:002", "name3:003"};
        GuavaBloomFilter.bloomFilterConfig(100,0.03D, Arrays.asList(blackList),KEY);
        BloomFilter bloomFilter = GuavaBloomFilter.getBloomFilter(KEY);
        String currentUser1 = "name1:001";
        boolean isBlackUser1 = bloomFilter.mightContain(currentUser1);
        String currentUser2 = "name4:004";
        boolean isBlackUser2 = bloomFilter.mightContain(currentUser2);
        log.info("currentUser1 存在：{}，currentUser2 存在：{}", isBlackUser1, isBlackUser2);
    }
}
