package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysUser;
import com.hcm.system.mapper.UserMapper;
import com.hcm.system.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pc
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * @param name userName or loginName
     * @description 根据用户名或者登陆名查询用户
     */
    @Override
    public SysUser getNameAndPasswordByName(String name) {
        return userMapper.getNameAndPasswordByName(name);
    }

    @Override
    public SysUser getUserInfoByName(String name) {
        return userMapper.getUserInfoByName(name);
    }

    /**
     * 通过id获取用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> getUserRolesById(Long userId) {
        return userMapper.getUserRolesById(userId);
    }

    @Override
    public void insertUser(SysUser sysUser) {
        userMapper.insertUser(sysUser);
    }
}
