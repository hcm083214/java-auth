package com.hcm.framework.exception;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import com.hcm.common.exception.BadRequestException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理程序
 *
 * @author pc
 * @date 2023/03/23
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResultVO<?> handleBadRequestException(BadRequestException e) {
        return ResultVO.fail(e.getMessage(), ResultCodeEnum.FAILED.getCode());
    }

    /**
     * 处理身份验证异常
     *
     * @param e e
     * @return {@link ResultVO}<{@link ?}>
     */
    @ExceptionHandler(AuthException.class)
    public ResultVO<?> handleAuthException(AuthException e) {
        int code = e.getCode() == null ? ResultCodeEnum.FAILED.getCode() : e.getCode();
        return ResultVO.fail(e.getMessage(), code);
    }

    /**
     * 校验异常处理
     *
     * @param e e
     * @return {@link ResultVO}<{@link ?}>
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public ResultVO<?> handleValidation(Exception e) {
        String msg = null;
        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            msg = ex.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            msg = ex.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        }
        return ResultVO.fail(msg, ResultCodeEnum.FAILED.getCode());
    }
}
