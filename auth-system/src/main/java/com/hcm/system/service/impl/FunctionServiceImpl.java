package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysMenu;
import com.hcm.common.vo.FunctionVo;
import com.hcm.system.mapper.FunctionMapper;
import com.hcm.system.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能权限服务impl
 *
 * @author pc
 * @date 2023/03/25
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionMapper functionMapper;

    /**
     * 查询功能权限列表
     *
     * @return {@link List}<{@link SysFunction}>
     */
    @Override
    public List<SysFunction> getFunctionList(FunctionVo functionVo) {
        return functionMapper.getFunctionList(functionVo);
    }

    /**
     * 通过权限id查询功能权限
     *
     * @param functionId 函数id
     * @return {@link List}<{@link SysMenu}>
     */
    @Override
    public List<Long> getPermIdListByFunId(Long functionId) {
        return functionMapper.getPermIdListByFunId(functionId);
    }

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     */
    @Override
    public void editFunctionInfo(FunctionVo functionVo) {
        functionMapper.editFunctionInfo(functionVo);
        if (functionVo.getPermissionIds() != null && functionVo.getPermissionIds().size() > 0) {
            List<Long> perms = functionMapper.getPermIdListByFunId(functionVo.getFunctionId());
            List<Long> insertPerms = new ArrayList<>();
            List<Long> deletePerms = new ArrayList<>();
            perms.forEach(menuId -> {
                if (!functionVo.getPermissionIds().contains(menuId)) {
                    deletePerms.add(menuId);
                }
            });
            functionVo.getPermissionIds().forEach(menuId -> {
                if (!perms.contains(menuId)) {
                    insertPerms.add(menuId);
                }
            });
            if(insertPerms.size()>0){
                functionMapper.insertFunctionPermInfo(functionVo.getFunctionId(), insertPerms);
            }
            if(deletePerms.size()>0){
                functionMapper.deleteFunctionPermInfo(functionVo.getFunctionId(), deletePerms);
            }
        }
    }
}
