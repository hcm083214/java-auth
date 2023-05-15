package com.hcm.common.filter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * guava布隆过滤器
 *
 * @author pc
 * @date 2023/05/07
 */
public class GuavaBloomFilter {

    public static Map<String,BloomFilter> bloomFilterMap = new HashMap<>();

    public static void bloomFilterConfig(Integer size, Double fpp, List<String> data,String key) {
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), size, fpp);
        data.forEach(d -> {
            bloomFilter.put(d);
        });
        bloomFilterMap.put(key,bloomFilter);
    }

    public static BloomFilter getBloomFilter(String key){
        return bloomFilterMap.get(key);
    }
}
