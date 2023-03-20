package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * @author pc
 */
@Mapper
public interface UserMapper {
    /**
     * 通过名称查找用户信息包括密码
     *
     * @param username 用户名
     * @return {@link SysUser}
     */
    SysUser getNameAndPasswordByName(@Param("username") String username);

    /**
     * 得到用户信息
     *
     * @param username 用户名
     * @return {@link SysUser}
     */
    SysUser getUserInfoByName(@Param("username") String username);

    /**
     * 通过id获取用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getUserRolesById(@Param("userId") Long userId);

    /**
     * 插入用户
     *
     * @param sysUser 用户信息
     */
    void insertUser(@Param("sysUser")SysUser sysUser);

    /**
     * 获取用户权限id
     *
     * @param userId 用户id
     * @return {@link Set}<{@link String}>
     */
    Set<String> getUserPermissionById(@Param("userId") Long userId);
}
