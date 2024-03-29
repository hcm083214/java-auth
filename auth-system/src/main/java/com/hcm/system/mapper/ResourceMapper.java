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
     * 删除资源
     *
     * @param resourceId 资源id
     */
    void deleteResource(@Param("resourceId") Long resourceId);

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
     *
     * @param resourceVoList resourceVoList
     */
    void updateParentId(@Param("resourceList") List<ResourceVo> resourceVoList);

    /**
     * 得到资源列表
     *
     * @param functionIds functionIds
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getResourceListByFunctions(@Param("functionIds") List<Long> functionIds);

    /**
     * 通过id获取资源
     *
     * @param resourceId 资源id
     * @return {@link SysResource}
     */
    SysResource getResourceById(@Param("resourceId") Long resourceId);
}
