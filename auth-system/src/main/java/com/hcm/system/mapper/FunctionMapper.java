package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.vo.FunctionVo;
import com.hcm.common.vo.MenuVo;
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
     * 根据functionId 查找功能权限id列表（菜单和按钮）
     *
     * @param functionId functionId
     * @return {@link List}<{@link Long}>
     */
    List<Long> getPermIdListByFunId(@Param("functionId") Long functionId);

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     */
    void editFunctionInfo(@Param("functionVo") FunctionVo functionVo);

    /**
     * 插入函数烫信息
     * 插入功能权限相关连的菜单和按钮权限
     *
     * @param perms      perms
     * @param functionId functionId
     */
    void insertFunctionPermInfo(@Param("functionId")Long functionId,@Param("perms") List<Long> perms);

    /**
     * 删除函数烫信息
     * 删除功能权限相关连的菜单和按钮权限
     *
     * @param perms      perms
     * @param functionId functionId
     */
    void deleteFunctionPermInfo(@Param("functionId")Long functionId,@Param("perms") List<Long> perms);
}
