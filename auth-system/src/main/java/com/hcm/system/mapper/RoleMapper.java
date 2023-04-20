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
     * 获取角色的权限
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermissionsByRoleIds(List<Long> roleIds);

    /**
     * 通过id获取权限列表
     *
     * @param roleId 角色id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getFunctionListById(@Param("roleId") Long roleId);

    /**
     * 得到所有权限
     *
     * @return {@link List}<{@link Long}>
     */
    List<Long> getAllPermissions();

    /**
     * 得到角色列表
     *
     * @param roleVo 角色入参
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRoles(@Param("role") RoleVo roleVo);

    /**
     * 通过id获取用户角色信息
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getUserRoleInfoById(@Param("userId") Long userId);

    /**
     * 得到角色
     *
     * @param roleId 角色id
     * @return {@link SysRole}
     */
    SysRole getRole(@Param("roleId")Long roleId);

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
    void insertRoleList(@Param("roles") List<RoleVo> roleVos);

    /**
     * 插入角色
     *
     * @param roleVo roleVo
     */
    void insertRole(@Param("role") RoleVo roleVo);

    /**
     * 插入角色功能权限列表
     *
     * @param roleId       角色id
     * @param functionIds 函数列表
     */
    void insertRoleFunctionList(@Param("roleId") Long roleId,@Param("functionIds") List<Long> functionIds);

    /**
     * 编辑角色
     *
     * @param roleVo roleVo
     */
    void editRole(@Param("role") RoleVo roleVo);


    /**
     * 删除角色功能列表
     *
     * @param roleId            角色id
     * @param deleteFunctionIds 删除功能id
     */
    void deleteRoleFunctionList(@Param("roleId") Long roleId,@Param("functionIds") List<Long> deleteFunctionIds);

    /**
     * 根据 functionKey 查询 角色
     *
     * @param roleVo 签证官角色
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> getRolesByFunKey(@Param("role") RoleVo roleVo);
}
