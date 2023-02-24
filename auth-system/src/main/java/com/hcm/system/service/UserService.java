package com.hcm.system.service;

import com.hcm.common.core.entity.SysUser;

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
     * 插入用户
     *
     * @param sysUser 用户信息
     */
    void insertUser(SysUser sysUser);
}
