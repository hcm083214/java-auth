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

    List<Long> getPermIdListByFunId(@Param("functionId")Long functionId);
}
