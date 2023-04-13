package com.hcm.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.vo.RoleVo;
import com.hcm.system.mapper.FunctionMapper;
import com.hcm.system.mapper.RoleMapper;
import com.hcm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private FunctionMapper functionMapper;

    /**
     * 获取默认角色
     *
     * @return {@link SysRole}
     */
    @Override
    public SysRole getDefaultRole(){
        return roleMapper.getRole(2L);
    }

    /**
     * 得到角色及相关信息，包括功能权限和数据权限
     *
     * @return {@link List}<{@link SysRole}>
     */
    @Override
    public List<SysRole> getRoles(RoleVo roleVo) {
        List<SysRole> roleList = roleMapper.getRoles(roleVo);
        setRoleFunctionList(roleList);
        return roleList;
    }

    /**
     * 设置角色功能权限列表
     *
     * @param roleList 角色列表
     */
    private void setRoleFunctionList(List<SysRole> roleList) {
        Set<Long> functions = new HashSet<>();
        Map<Long, Set<Long>> roleMap = new HashMap<>(roleList.size());
        roleList.forEach(sysRole -> {
            List<Long> ids = JSONArray.parseArray(sysRole.getFunctionJson(), Long.class);
            Set<Long> set = new HashSet<>(ids);
            functions.addAll(set);
            roleMap.put(sysRole.getRoleId(), set);
        });
        if (functions.size() > 0) {
            List<SysFunction> functionList = functionMapper.getFunctionListByIds(new ArrayList<>(functions));
            Map<Long, SysFunction> functionMap = new HashMap<>(functionList.size());
            functionList.forEach(function -> {
                functionMap.put(function.getFunctionId(), function);
            });
            roleList.forEach(sysRole -> {
                List<SysFunction> sysFunctionList = new ArrayList<>();
                roleMap.get(sysRole.getRoleId()).forEach(functionId -> {
                    sysFunctionList.add(functionMap.get(functionId));
                });
                sysRole.setFunctionList(sysFunctionList);
            });
        }
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
            result = roleMapper.getParamsList(roleVo, roleVo.getSearchParams().toLowerCase());
        }
        return result;
    }

    /**
     * 插入角色及功能权限
     *
     * @param roleVo roleVo
     */
    @Override
    public void insertRole(RoleVo roleVo) {
        roleMapper.insertRole(roleVo);
        List<Long> functionIds = JSONArray.parseArray(roleVo.getFunctionJson(), Long.class);
        Long roleId = roleVo.getRoleId();
        if (functionIds.size() > 0) {
            roleMapper.insertRoleFunctionList(roleId,functionIds);
        }
    }

    /**
     * 编辑角色
     *
     * @param roleVo roleVo
     */
    @Override
    public void editRole(RoleVo roleVo) {
        roleMapper.editRole(roleVo);
        List<Long> oriFunctionIds = roleMapper.getFunctionListById(roleVo.getRoleId());
        List<Long> editFunctionIds = JSONArray.parseArray(roleVo.getFunctionJson(),Long.class);
        List<Long> deleteFunctionIds = new ArrayList<>();
        List<Long> insertFunctionIds = new ArrayList<>();
        if(editFunctionIds.size() == 0){
            deleteFunctionIds = oriFunctionIds;
        }else {
            for (Long functionId : editFunctionIds) {
                if(!oriFunctionIds.contains(functionId)){
                    insertFunctionIds.add(functionId);
                }
            }
        }
        if(oriFunctionIds.size() == 0){
            insertFunctionIds = editFunctionIds;
        }else {
            for (Long functionId : oriFunctionIds) {
                if(!editFunctionIds.contains(functionId)){
                    deleteFunctionIds.add(functionId);
                }
            }
        }
        if(deleteFunctionIds.size()>0){
            roleMapper.deleteRoleFunctionList(roleVo.getRoleId(),deleteFunctionIds);
        }
        if(insertFunctionIds.size()>0){
            roleMapper.insertRoleFunctionList(roleVo.getRoleId(), insertFunctionIds);
        }
    }

    /**
     * 插入角色列表
     *
     * @param roleVos 角色vos
     */
    @Override
    public void insertRoleList(List<RoleVo> roleVos) {
        roleMapper.insertRoleList(roleVos);
    }

    /**
     * 通过id获取用户角色信息
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysRole}>
     */
    @Override
    public List<SysRole> getUserRoleInfoById(Long userId) {
        return roleMapper.getUserRoleInfoById(userId);
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
