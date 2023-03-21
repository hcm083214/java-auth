package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysRole;
import com.hcm.common.vo.RoleVo;
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

    /**
     * 得到所有权限
     *
     * @return {@link List}<{@link Long}>
     */
    List<Long> getAllPermissions();

    /**
     * 得到角色
     *
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRoles();

    /**
     * 插入作用
     * 插入角色
     *
     * @param roleVos 角色vos
     */
    void insertRole(@Param("roles") List<RoleVo> roleVos);
}
