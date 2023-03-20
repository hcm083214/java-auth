package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysRole;
import com.hcm.system.mapper.RoleMapper;
import com.hcm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务impl
 *
 * @author pc
 * @date 2023/02/25
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 得到角色
     *
     * @return {@link List}<{@link SysRole}>
     */
    @Override
    public List<SysRole> getRoles() {
        return roleMapper.getRoles();
    }

    /**
     * 通过id获取权限
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> getPermissionsByRoleIds(List<Long> roleIds) {
        if(roleIds.contains(1L)){
           return roleMapper.getAllPermissions();
        }else{
            return roleMapper.getPermissionsByRoleIds(roleIds);
        }
    }
}
