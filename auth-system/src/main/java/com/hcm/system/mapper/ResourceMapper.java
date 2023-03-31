package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysResource;
import com.hcm.common.vo.ResourceVo;
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
     * 编辑菜单
     *
     * @param resourceVo resourceVo
     */
    void editMenu(@Param("resource") ResourceVo resourceVo);

    /**
     * 添加菜单
     *
     * @param resourceVo resourceVo
     */
    void addMenu(@Param("resource") ResourceVo resourceVo);

    /**
     * 添加api列表
     *
     * @param sysResourceList 系统资源列表
     */
    void addApiList(@Param("resourceList") List<SysResource> sysResourceList);

    /**
     * 获得api列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getApiList();

    /**
     * 更新父id
     */
    void updateParentId(@Param("resourceList") List<ResourceVo> resourceVoList);
}
