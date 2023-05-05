package com.hcm.system.service;

import com.hcm.common.core.entity.SysResource;

import java.util.List;
import java.util.Map;

/**
 * 点击率服务
 *
 * @author pc
 * @date 2023/05/05
 */
public interface ViewCounterService {


    /**
     * 统计资源uvpv
     *
     * @param sysResource 系统资源  controllerClass 和  methodName
     */
    void countResourceUVAndPV(SysResource sysResource);

    /**
     * 得到当前时间前面多少
     *
     * @param during 在
     * @return {@link Map}<{@link String},{@link Long}>
     */
    Map<String, Long> getUVCount(Integer during);

    /**
     * 设置资源一段时间内的访问数量
     *
     * @param sysResource 系统资源
     * @param during      时间间隔
     * @return {@link Map}<{@link String}, {@link Long}>
     */
    SysResource setResourcePVCount(SysResource sysResource, Integer during);
}
