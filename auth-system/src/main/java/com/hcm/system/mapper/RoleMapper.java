package com.hcm.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色映射器
 *
 * @author pc
 * @date 2023/02/25
 */
@Mapper
public interface RoleMapper {
    /**
     * 通过id获取权限
     *
     * @param roleId 角色id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermissionsByRoleIds(@Param("roleIds") List<Long> roleId);

    List<Long> getAllPermissions();
}
