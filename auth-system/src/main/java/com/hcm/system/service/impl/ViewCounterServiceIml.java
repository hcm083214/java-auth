package com.hcm.system.service.impl;

import com.hcm.common.constants.CacheConstants;
import com.hcm.common.constants.CommonConstants;
import com.hcm.common.core.entity.SysResource;
import com.hcm.common.core.redis.RedisHPCache;
import com.hcm.common.core.redis.RedisHashCache;
import com.hcm.common.core.redis.RedisStringCache;
import com.hcm.common.utils.DateUtils;
import com.hcm.common.utils.ServletUtils;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.utils.ip.IpUtils;
import com.hcm.system.service.ResourceService;
import com.hcm.system.service.ViewCounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点击率
 *
 * @author pc
 * @date 2023/05/05
 */
@Service
@Slf4j
public class ViewCounterServiceIml implements ViewCounterService {

    @Autowired
    private RedisHPCache redisHPCache;

    @Autowired
    private RedisStringCache redisStringCache;

    @Autowired
    private ResourceService resourceService;

    /**
     * 统计资源uvpv
     *
     * @param sysResource 系统资源  controllerClass 和  methodName
     */
    @Override
    public void countResourceUVAndPV(SysResource sysResource) {
        String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.YYYYMMdd);
        String timestamp = simpleDateFormat.format(new Date());

        String pvKey = formatKeyName(resourceService.getApiName(sysResource), timestamp);
        String uvKey = formatKeyName("unique_visitor", timestamp);
        redisHPCache.add(uvKey, ipAddr);

        if (redisStringCache.isExists(pvKey)) {
            redisStringCache.setCacheObject(pvKey, 1);
        } else {
            redisStringCache.increase(pvKey, 1);
        }

    }

    /**
     * 获取当前日期和往前 during 天的 UV 值
     *
     * @param during 天数
     * @return {@link Map}<{@link String}, {@link Long}>
     */
    @Override
    public Map<String, Long> getUVCount(Integer during) {
        List<String> lastDays = DateUtils.getLastDays(new Date(), during);
        HashMap<String, Long> map = new HashMap<>(lastDays.size());
        lastDays.forEach(day -> {
            Long uniqueVisitor = redisHPCache.count(formatKeyName("unique_visitor", day));
            map.put(day, uniqueVisitor);
        });
        return map;
    }

    /**
     * 设置资源一段时间内的访问数量
     *
     * @param sysResource 系统资源
     * @param during      时间间隔
     * @return {@link Map}<{@link String}, {@link Long}>
     */
    @Override
    public SysResource setResourcePVCount(SysResource sysResource, Integer during) {
        List<String> lastDays = DateUtils.getLastDays(new Date(), during);
        HashMap<String, Integer> map = new HashMap<>(lastDays.size());
        int total = 0;
        for (String day : lastDays) {
            Integer pageCounter = redisStringCache.getCacheObject(formatKeyName(resourceService.getApiName(sysResource), day));
            if(StringUtils.isNotNull(pageCounter)){
                map.put(day, pageCounter);
                total += pageCounter;
            }
        }
        map.put("total",total);
        sysResource.setPageCounter(map);
        return sysResource;
    }

    /**
     * 格式键名
     *
     * @param keyName 键名
     * @return {@link String}
     */
    private String formatKeyName(String keyName, String timestamp) {

        return CacheConstants.CACHE_VIEW_COUNTER_PREFIX + keyName + ":" + timestamp;
    }


}
