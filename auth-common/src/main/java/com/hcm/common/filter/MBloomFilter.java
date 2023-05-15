package com.hcm.common.filter;

import com.hcm.common.core.redis.RedisBitMapCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 布隆过滤器
 *
 * @author pc
 * @date 2023/05/07
 */
@Component
@Slf4j
public class MBloomFilter {

    @Autowired
    private RedisBitMapCache redisBitMapCache;

    /**
     * 数据预加载到布隆过滤器
     *
     * @param data 数据
     */
    public void initData(List<String> data,String key) {
        data.forEach(k -> {
            //1 计算hashcode，由于可能有负数，直接取绝对值
            int hashValue = Math.abs(k.hashCode());
            //2 通过hashValue和2的32次方取余后，获得对应的下标坑位
            long index = (long) (hashValue % Math.pow(2, 32));
            //3 设置redis里面bitmap对应坑位，设置为1
            log.info("initData:{}",index);
            redisBitMapCache.setBit(key, index, true);
        });
    }

    /**
     * 检查值是否存在于布隆过滤器
     *
     * @param checkItem 检查项目
     * @param key       关键
     * @return boolean
     */
    public boolean checkWithBloomFilter(String checkItem,String key){
        int hashValue = Math.abs(checkItem.hashCode());
        long index = (long) (hashValue % Math.pow(2, 32));
        log.info("checkWithBloomFilter:{}",index);
        return redisBitMapCache.getBit(key, index);
    }
}
