package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pc
 */
@Mapper
public interface ResourceMapper {

    /**
     * 获取菜单列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getMenuList();

    /**
     * 得到所有菜单列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> geResourceList();

    /**
     * 添加api列表
     *
     * @param sysResourceList 系统资源列表
     */
    void addApiList(@Param("resourceList") List<SysResource> sysResourceList);
}
