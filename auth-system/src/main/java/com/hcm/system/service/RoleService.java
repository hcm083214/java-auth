package com.hcm.system.service;

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
}
