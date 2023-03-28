package com.hcm.system.service;

import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysMenu;
import com.hcm.common.vo.FunctionVo;

import java.util.List;

/**
 * 功能权限服务
 *
 * @author pc
 * @date 2023/03/25
 */
public interface FunctionService {

    /**
     * 查询功能权限列表
     *
     * @param functionVo functionVo
     * @return {@link List}<{@link SysFunction}>
     */
    List<SysFunction> getFunctionList(FunctionVo functionVo);

    /**
     * 通过权限id查询功能权限
     *
     * @param functionId 函数id
     * @return {@link List}<{@link SysMenu}>
     */
    List<Long> getPermIdListByFunId(Long functionId);

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     */
    void editFunctionInfo(FunctionVo functionVo);
}
