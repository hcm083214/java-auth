package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ConfigMapper 获取系统的参数配置
 *
 * @author pc
 * @date 2023/02/22
 */
@Mapper
public interface ConfigMapper {
    /**
     * 得到系统参数配置
     *
     * @param configKey 参数键名
     * @return {@link SysConfig}
     */
    public SysConfig getSysConfigByKey(@Param("configKey") String configKey);
}
