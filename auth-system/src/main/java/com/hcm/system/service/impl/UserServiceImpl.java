package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysRole;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.enums.TripartiteSourceEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.system.mapper.ResourceMapper;
import com.hcm.system.mapper.UserMapper;
import com.hcm.system.service.RoleService;
import com.hcm.system.service.UserService;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pc
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleService roleService;

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

    /**
     * 插入用户
     *
     * @param sysUser 系统用户
     */
    @Override
    public void insertUser(SysUser sysUser) {
        userMapper.insertUser(sysUser);
        sysUser.setUserId(sysUser.getUserId());
        List<SysRole> roleList = new ArrayList<>();
        roleList.add(roleService.getDefaultRole());
        sysUser.setRoleList(roleList);
        userMapper.insertUserRole(sysUser);
    }


    /**
     * gitee user 转用户
     *
     * @param authUser 身份验证用户
     * @return {@link SysUser}
     */
    @Override
    public SysUser giteeUser2User(AuthUser authUser) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(authUser.getUsername());
        sysUser.setEmail(authUser.getEmail());
        sysUser.setNickName(authUser.getNickname());
        sysUser.setUserType(TripartiteSourceEnum.GITEE.getType());
        return sysUser;
    }
}
