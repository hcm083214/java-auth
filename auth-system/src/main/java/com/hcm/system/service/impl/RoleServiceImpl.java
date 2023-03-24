package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysRole;
import com.hcm.common.enums.RoleSearchTypeEnum;
import com.hcm.common.vo.RoleVo;
import com.hcm.system.mapper.RoleMapper;
import com.hcm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public List<SysRole> getRoles(RoleVo roleVo) {
        return roleMapper.getRoles(roleVo);
    }

    /**
     * 得到参数列表
     *
     * @param roleVo roleVo
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getParamsList(RoleVo roleVo) {
        List<String> result = new ArrayList<>();
        if (roleVo.getSearchParams() != null) {
            result = roleMapper.getParamsList(roleVo,roleVo.getSearchParams().toLowerCase());
        }
        return result;
    }

    /**
     * 插入角色
     *
     * @param roleVos 角色vos
     */
    @Override
    public void insertRole(List<RoleVo> roleVos) {
        roleMapper.insertRole(roleVos);
    }

    /**
     * 通过id获取权限
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> getPermissionsByRoleIds(List<Long> roleIds) {
        if (roleIds.contains(1L)) {
            return roleMapper.getAllPermissions();
        } else {
            return roleMapper.getPermissionsByRoleIds(roleIds);
        }
    }
}
