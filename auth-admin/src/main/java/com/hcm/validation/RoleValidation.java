package com.hcm.validation;

import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.RoleVo;

/**
 * 角色入参验证
 *
 * @author pc
 * @date 2023/03/21
 */
public class RoleValidation {
    public static void rolesSearchParamsValid(RoleVo roleVo){
        if (roleVo.getPageNum() == null || roleVo.getPageSize() == null) {
            throw new BadRequestException("pageNum或者pageSize未传");
        }
    }
}
