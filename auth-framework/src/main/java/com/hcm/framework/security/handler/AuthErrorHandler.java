package com.hcm.framework.security.handler;

import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pc
 * @description 认证错误处理器
 */
@Slf4j
@Component
public class AuthErrorHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws AuthException, ServletException, IOException {
        log.error("AuthErrorHandler ---> commence,验证失败:{}", authException.getMessage());
        ResponseHandler.handler(response,ResultCodeEnum.UNAUTHORIZED);
    }
}
