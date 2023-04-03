package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.vo.FunctionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能权限mapper
 *
 * @author pc
 * @date 2023/03/25
 */
@Mapper
public interface FunctionMapper {

    /**
     * 得到功能权限列表
     *
     * @param functionVo functionVo
     * @return {@link List}<{@link SysFunction}>
     */
    List<SysFunction> getFunctionList(@Param("function") FunctionVo functionVo);

    /**
     * 根据id列表查询功能权限列表
     *
     * @param ids id
     * @return {@link List}<{@link SysFunction}>
     */
    List<SysFunction> getFunctionListByIds(@Param("functionIds") List<Long> ids);

    /**
     * 确认是否存在同名
     *
     * @param functionVo 函数签证官
     * @return {@link List}<{@link String}>
     */
    List<String> getFunctionByNameOrKey(@Param("function") FunctionVo functionVo);

    /**
     * 根据functionId 查找功能权限id列表（菜单和按钮）
     *
     * @param functionId functionId
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermIdListByFunId(@Param("functionId") Long functionId);

    /**
     * 得到参数列表
     *
     * @param searchType 类型
     * @param functionVo roleVo
     * @return {@link List}<{@link String}>
     */
    List<String> getParamsList(@Param("function") FunctionVo functionVo, @Param("searchType") String searchType);

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     */
    void editFunctionInfo(@Param("function") FunctionVo functionVo);

    /**
     * 插入功能权限信息
     *
     * @param functionVo functionVo
     */
    void insertFunction(@Param("function") FunctionVo functionVo);

    /**
     * 批量插入功能权限信息
     *
     * @param functionList functionList
     */
    void insertFunctionList(@Param("functionList") List<FunctionVo> functionList);

    /**
     * 插入功能权限相关连的菜单和按钮权限
     *
     * @param perms      perms
     * @param functionId functionId
     */
    void insertFunctionPermInfo(@Param("functionId") Long functionId, @Param("perms") List<Long> perms);

    /**
     * 删除函数烫信息
     * 删除功能权限相关连的菜单和按钮权限
     *
     * @param perms      perms
     * @param functionId functionId
     */
    void deleteFunctionPermInfo(@Param("functionId") Long functionId, @Param("perms") List<Long> perms);
}
