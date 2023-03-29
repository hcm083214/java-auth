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
     * 判断待新增functionName和functionKey是否已经存在
     *
     * @param functionVo 函数签证官
     * @return boolean
     */
    @Override
    public boolean isInsertExist(FunctionVo functionVo) {
        List<String> functionNameList = functionMapper.getFunctionByNameOrKey(functionVo);
        return functionNameList != null && functionNameList.size() > 0;
    }

    /**
     * 插入功能权限
     *
     * @param functionVo functionVo
     */
    @Override
    public void insertFunction(FunctionVo functionVo) {
        functionMapper.insertFunction(functionVo);
        Long functionId = functionVo.getFunctionId();
        if(functionVo.getPermissionIds() != null && functionVo.getPermissionIds().size()>0){
            functionMapper.insertFunctionPermInfo(functionId,functionVo.getPermissionIds());
        }
    }

    /**
     * 批量插入功能权限
     *
     * @param functionVos functionVos
     */
    @Override
    public void insertFunctionList(List<FunctionVo> functionVos) {
        if(functionVos.size()>0){
            functionMapper.insertFunctionList(functionVos);
        }
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
     * 得到联想查询的参数列表
     *
     * @param functionVo functionVo
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getParamsList(FunctionVo functionVo) {
        return functionMapper.getParamsList(functionVo,functionVo.getSearchParams());
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
            if (insertPerms.size() > 0) {
                functionMapper.insertFunctionPermInfo(functionVo.getFunctionId(), insertPerms);
            }
            if (deletePerms.size() > 0) {
                functionMapper.deleteFunctionPermInfo(functionVo.getFunctionId(), deletePerms);
            }
        }
    }
}
