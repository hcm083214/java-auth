package com.hcm.system.service;

import com.hcm.common.core.entity.SysResource;
import com.hcm.common.vo.ResourceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author pc
 */

public interface ResourceService {
    /**
     * 获得菜单列表
     * * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getMenuList();

    /**
     * 得到所有菜单列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getMenuListAll();

    /**
     * 编辑菜单
     *
     * @param resourceVo resourceVo
     */
    void editMenu(ResourceVo resourceVo);

    /**
     * 添加菜单
     *
     * @param resourceVo resourceVo
     */
    void addMenu(ResourceVo resourceVo);

    /**
     * 编辑资源父id
     *
     * @param resourceVoList resourceVo
     */
    void editResourceParentId(List<ResourceVo> resourceVoList);

    /**
     * 同步api信息
     *
     * @param request 请求
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> syncApiInfo(HttpServletRequest request);

    /**
     * 获得api列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    List<SysResource> getApiList();

    /**
     * 添加api列表
     *
     * @param sysResourceList 系统资源列表
     */
    void addApiList(List<SysResource> sysResourceList);
}
