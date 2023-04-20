package com.hcm.framework.security.service;

import com.hcm.common.constants.CacheConstants;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.core.redis.RedisCache;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.LoginVo;
import com.hcm.framework.security.context.AuthenticationContextHolder;
import com.hcm.system.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 用户登录服务
 *
 * @author pc
 * @date 2023/02/22
 */
@Slf4j
@Service
public class UserLoginService {
    @Autowired
    private ConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDetail login(LoginVo loginVo) throws BadRequestException, AuthException {
        checkCaptcha(loginVo.getCode(), loginVo.getUuid());
        Authentication authentication;
        try {
            // 生成一个包含账号密码的认证信息
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUserName(), loginVo.getPassword());
            // AuthenticationManager校验这个认证信息，返回一个已认证的Authentication,该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
            // 将返回的Authentication存到上下文中。ruoyi 是将其存入到自定义的上下文中 Authentication
            AuthenticationContextHolder.setSecurityContext(authentication);
            // 放入到自定义的上下文的作用是为了限制同一账号多次登陆
            AuthenticationContextHolder.setContext(authentication);
        } catch (Exception e) {
            log.error("UserLoginService ---> login:{}", e.getMessage());
            if (e instanceof BadCredentialsException) {
                throw new AuthException(ResultCodeEnum.FAILED.getCode(), "用户名或者账号错误");
            } else {
                throw new BadRequestException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.removeContext();
        }
        return (UserDetail) authentication.getPrincipal();
    }

    /**
     * 检查验证码是否正确
     *
     * @param code 验证码
     * @param uuid 验证码的uuid
     * @throws BadRequestException 错误请求异常
     */
    public void checkCaptcha(String code, String uuid) throws BadRequestException {
        boolean captchaEnabled = configService.getCaptchaEnabled();
        if (captchaEnabled) {
            String captchaCode = redisCache.getCacheObject(CacheConstants.CACHE_CAPTCHA_CODE + uuid);
            if (!code.equalsIgnoreCase(captchaCode)) {
                redisCache.deleteObject(CacheConstants.CACHE_CAPTCHA_CODE + uuid);
                throw new BadRequestException("验证码错误");
            }
        }
    }
}
