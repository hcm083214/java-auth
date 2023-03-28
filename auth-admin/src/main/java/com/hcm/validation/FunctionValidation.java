package com.hcm.validation;

import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.FunctionVo;


/**
 * 功能权限入参验证
 *
 * @author pc
 * @date 2023/03/25
 */
public class FunctionValidation {
    /**
     * 是否传递functionId
     *
     * @param functionId 函数id
     * @throws BadRequestException 错误请求异常
     */
    public static void isPassFunctionId(Long functionId) throws BadRequestException {
        if (functionId == null) {
            throw new BadRequestException("functionId不能为空");
        }
    }

    /**
     * 编辑验证
     *
     * @param functionVo functionVo
     * @throws BadRequestException 错误请求异常
     */
    public static void editValidation(FunctionVo functionVo) throws BadRequestException {
        if(StringUtils.isEmpty(functionVo.getFunctionKey())){
            throw new BadRequestException("functionKey不能为空");
        }
        if(StringUtils.isEmpty(functionVo.getFunctionDescriptionCn())){
            throw new BadRequestException("functionDescriptionCn不能为空");
        }
        if(StringUtils.isEmpty(functionVo.getFunctionDescriptionEn())){
            throw new BadRequestException("functionDescriptionEn不能为空");
        }
        if(StringUtils.isEmpty(functionVo.getFunctionNameCn())){
            throw new BadRequestException("functionNameCn不能为空");
        }
        if(StringUtils.isEmpty(functionVo.getFunctionNameEn())){
            throw new BadRequestException("FunctionNameEn不能为空");
        }
    }
}
