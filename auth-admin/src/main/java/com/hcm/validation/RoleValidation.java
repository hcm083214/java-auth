package com.hcm.validation;

import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.RoleVo;

/**
 * 角色入参验证
 *
 * @author pc
 * @date 2023/03/21
 */
public class RoleValidation {
    public static void addRoleValidation(RoleVo roleVo)throws BadRequestException{
        if(StringUtils.isNull(roleVo.getRoleNameCn())){
            throw new BadRequestException("roleNameCn 未传");
        }
        if(StringUtils.isNull(roleVo.getRoleNameEn())){
            throw new BadRequestException("roleNameEn 未传");
        }
        if(StringUtils.isNull(roleVo.getRoleDescriptionCn())){
            throw new BadRequestException("roleDescriptionCn 未传");
        }
        if(StringUtils.isNull(roleVo.getRoleDescriptionEn())){
            throw new BadRequestException("RoleDescriptionEn 未传");
        }
        if(StringUtils.isNull(roleVo.getFunctionJson())){
            throw new BadRequestException("functionJson 未传");
        }
    }

    public static void editRoleValidation(RoleVo roleVo){
        if(StringUtils.isNull(roleVo.getRoleId())){
            throw new BadRequestException("roleId 未传");
        }
        addRoleValidation(roleVo);
    }
}
