package com.hcm.framework.security.service;

import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.utils.StringUtils;
import com.hcm.framework.security.JwtManager;
import com.hcm.framework.security.handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销处理
 *
 * @author pc
 * @date 2023/04/05
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private JwtManager jwtManager;

    /**
     * 用户注销
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 身份验证
     * @throws IOException ioexception
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            UserDetail userDetail = jwtManager.parse(token);
            jwtManager.deleteUserTokenOnRedis(userDetail);
            ResponseHandler.handler(response, ResultCodeEnum.SUCCESS.getCode(),"退出成功");
        }
    }
}
