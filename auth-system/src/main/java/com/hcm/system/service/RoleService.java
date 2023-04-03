package com.hcm.system.service;

import com.hcm.common.core.entity.SysRole;
import com.hcm.common.vo.RoleVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务
 *
 * @author pc
 * @date 2023/02/25
 */
public interface RoleService {
    /**
     * 通过id获取权限
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermissionsByRoleIds(List<Long> roleIds);

    /**
     * 得到角色列表
     *
     * @param roleVo roleVo
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRoles(RoleVo roleVo);

    /**
     * 得到参数列表
     *
     * @param roleVo roleVo
     * @return {@link List}<{@link String}>
     */
    List<String> getParamsList(RoleVo roleVo);
    /**
     * 插入角色列表
     *
     * @param roleVos 角色vos
     */
    void insertRoleList(List<RoleVo> roleVos);

    /**
     * 插入角色
     *
     * @param roleVo roleVo
     */
    void insertRole(RoleVo roleVo);

    /**
     * 编辑角色
     *
     * @param roleVo roleVo
     */
    void editRole(RoleVo roleVo);
}
