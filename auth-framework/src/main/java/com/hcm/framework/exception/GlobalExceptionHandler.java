package com.hcm.framework.exception;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import com.hcm.common.exception.BadRequestException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author pc
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResultVO handleBadRequestException(BadRequestException e) {
        return ResultVO.fail(ResultCodeEnum.FAILED.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResultVO handleAuthException(AuthException e) {
        return ResultVO.fail(ResultCodeEnum.UNAUTHORIZED.getCode(), e.getMessage());
    }
}
