package com.hcm.system.service;

import com.hcm.common.core.entity.SysApi;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysResource;
import com.hcm.common.vo.FunctionVo;

import javax.servlet.http.HttpServletRequest;
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
     * 判断待新增functionName是否已经存在
     *
     * @param functionVo 函数签证官
     * @return boolean
     */
    boolean isInsertExist(FunctionVo functionVo);

    /**
     * 插入功能权限
     *
     * @param functionVo 函数签证官
     */
    void insertFunction(FunctionVo functionVo);

    /**
     * 批量插入功能权限
     *
     * @param functionVos functionVos
     */
    void insertFunctionList(List<FunctionVo> functionVos);

    /**
     * 通过权限id查询功能权限
     *
     * @param functionId 函数id
     * @return {@link List}<{@link SysResource}>
     */
    List<Long> getPermIdListByFunId(Long functionId);

    /**
     * 得到联想查询的参数列表
     *
     * @param functionVo functionVo
     * @return {@link List}<{@link String}>
     */
    List<String> getParamsList(FunctionVo functionVo);

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     */
    void editFunctionInfo(FunctionVo functionVo);



}
