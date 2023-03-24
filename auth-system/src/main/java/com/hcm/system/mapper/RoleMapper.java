package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysRole;
import com.hcm.common.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RoleMapper
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
     * @param roleVo 角色入参
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRoles(@Param("role") RoleVo roleVo);

    /**
     * 得到参数列表
     *
     * @param type   类型
     * @param roleVo roleVo
     * @return {@link List}<{@link String}>
     */
    List<String> getParamsList(@Param("role") RoleVo roleVo,@Param("type") String type);

    /**
     * 插入角色
     *
     * @param roleVos 角色vos
     */
    void insertRole(@Param("roles") List<RoleVo> roleVos);
}
