package com.hcm.controller;

import com.hcm.common.constants.CacheConstants;
import com.hcm.common.constants.CommonConstants;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.core.redis.RedisCache;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.LoginVo;


import com.hcm.common.vo.UserVo;
import com.hcm.framework.security.JwtManager;
import com.hcm.framework.security.service.UserLoginService;
import com.hcm.system.service.UserService;
import com.hcm.validation.LoginValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pc
 * @description 用户
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserLoginService userLoginService;

    /**
     * @param loginVo LoginVo
     * @description
     */
    @PostMapping("/login")
    public ResultVO<LoginVo> login(@RequestBody() LoginVo loginVo) throws BadRequestException {
        LoginValidation.loginParamsValid(loginVo);
        UserDetail userDetail = userLoginService.login(loginVo);
        loginVo.setToken(jwtManager.generate(userDetail));
        return ResultVO.success(loginVo);
    }

    @PostMapping("/register")
    public ResultVO<UserVo> register(@RequestBody LoginVo loginVo, HttpServletRequest request) throws BadRequestException {
        LoginValidation.loginParamsValid(loginVo);
        String captchaCode = (String) request.getSession().getAttribute(CacheConstants.CACHE_CAPTCHA_CODE);
        if (!loginVo.getCode().equals(captchaCode)) {
            throw new BadRequestException("验证码错误");
        }
        SysUser sysUser = userService.getUserInfoByName(loginVo.getUserName());
        if (sysUser != null) {
            throw new BadRequestException("用户已经存在");
        }
        loginVo.setPassword(passwordEncoder.encode(loginVo.getPassword()));
        SysUser user = new SysUser();
        BeanUtils.copyProperties(loginVo,user);
        userService.insertUser(user);
        return ResultVO.success();
    }
}
