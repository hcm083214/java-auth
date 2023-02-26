package com.hcm.framework.security.filter;


import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.exception.AuthException;
import com.hcm.common.utils.StringUtils;
import com.hcm.framework.security.JwtManager;
import com.hcm.framework.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pc
 * @description spring security 认证过滤器
 */
@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthException {
        String token = request.getHeader("Authorization");
        UserDetail userDetail = null;
        if (StringUtils.isNotEmpty(token)) {
             userDetail = jwtManager.parse(token);
        }
        log.info("AuthFilter--->Authentication:{}", SecurityContextHolder.getContext().getAuthentication());
        //  SecurityContextHolder.getContext().getAuthentication() 每次请求都为 null ，只要缓存中
        if(StringUtils.isNotNull(userDetail) && StringUtils.isNull(SecurityContextHolder.getContext().getAuthentication())){
            jwtManager.verifyUserTokenOnRedis(userDetail);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("AuthFilter--->token:{},payload:{}", token, userDetail);
        }
        chain.doFilter(request, response);
    }
}
