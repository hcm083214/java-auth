package com.hcm.controller;

import com.hcm.common.constants.CacheConstants;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.LoginVo;


import com.hcm.common.vo.UserVo;
import com.hcm.framework.security.JwtManager;
import com.hcm.framework.security.service.UserLoginService;
import com.hcm.system.service.RoleService;
import com.hcm.system.service.UserService;
import com.hcm.validation.LoginValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 登录
     *
     * @param loginVo LoginVo
     * @return {@link ResultVO}<{@link LoginVo}>
     * @throws BadRequestException 错误请求异常
     * @description
     */
    @PostMapping("/login")
    public ResultVO<LoginVo> login(@RequestBody() LoginVo loginVo) throws BadRequestException {
        LoginValidation.loginParamsValid(loginVo);
        UserDetail userDetail = userLoginService.login(loginVo);
        loginVo.setToken(jwtManager.generate(userDetail));
        return ResultVO.success(loginVo);
    }

    /**
     * 获取用户信息
     *
     * @return {@link ResultVO}<{@link UserVo}>
     * @throws BadRequestException 错误请求异常
     */
    @GetMapping("/user")
    public ResultVO<UserVo> getUserInfo() throws BadRequestException {
        UserDetail user;
        try {
            user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException("获取用户信息异常");
        }
        UserVo userVo = new UserVo();
        List<Long> roles = userService.getUserRolesById(user.getSysUser().getUserId());
        List<Long> permissions = null;
        if (roles.size() > 0) {
            permissions = roleService.getPermissionsByRoleIds(roles);
        }
        BeanUtils.copyProperties(user.getSysUser(), userVo);
        userVo.setRoles(roles);
        userVo.setPermissions(permissions);
        return ResultVO.success(userVo);
    }

    /**
     * 注册
     *
     * @param loginVo 登录
     * @param request 请求
     * @return {@link ResultVO}<{@link UserVo}>
     * @throws BadRequestException 错误请求异常
     */
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
        BeanUtils.copyProperties(loginVo, user);
        userService.insertUser(user);
        return ResultVO.success();
    }
}
