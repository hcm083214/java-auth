package com.hcm.validation;

import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.BaseUtils;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.LoginVo;
import org.springframework.util.CollectionUtils;

/**
 * 登录接口入参校验
 *
 * @author pc
 * @description 登录接口入参校验
 * @date 2023/03/21
 */
public class LoginValidation {
    public static void loginParamsValid(LoginVo loginVo) {
        if (BaseUtils.isEmptyString(loginVo.getUserName())) {
            throw new BadRequestException("loginName 未传");
        }
        if (BaseUtils.isEmptyString(loginVo.getPassword())) {
            throw new BadRequestException("password 未传");
        }
        if (BaseUtils.isEmptyString(loginVo.getCode())) {
            throw new BadRequestException("code 未传");
        }
        if(StringUtils.isEmpty(loginVo.getUuid())){
            throw new BadRequestException("uuid 未传");
        }
    }
}
