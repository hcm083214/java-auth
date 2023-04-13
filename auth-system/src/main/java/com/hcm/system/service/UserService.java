package com.hcm.system.service;

import com.hcm.common.core.entity.SysUser;
import me.zhyd.oauth.model.AuthUser;

import java.util.List;

/**
 * @author pc
 */
public interface UserService {

    /**
     * 根据用户名或者登陆名查询用户
     *
     * @param name username
     * @return {@link SysUser}
     * @description 根据用户名或者登陆名查询用户
     */
    SysUser getNameAndPasswordByName(String name);

    /**
     * 得到用户信息
     *
     * @param name 名字
     * @return {@link SysUser}
     */
    SysUser getUserInfoByName(String name);

    /**
     * 通过id获取用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getUserRolesById(Long userId);

    /**
     * 插入用户
     *
     * @param sysUser 用户信息
     */
    void insertUser(SysUser sysUser);

    /**
     * gitee user 转用户
     *
     * @param authUser 身份验证用户
     * @return {@link SysUser}
     */
    SysUser giteeUser2User(AuthUser authUser);
}
