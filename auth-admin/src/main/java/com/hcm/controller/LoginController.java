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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "登陆注册服务")
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
    @ApiOperation(value = "用户登录",notes = "用户登录")
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
    @ApiOperation(value = "获取用户信息",notes = "获取用户信息")
    public ResultVO<UserVo> getUserInfo() throws BadRequestException {
        UserDetail user;
        try {
            user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("LoginController ---> getUserInfo,获取用户信息异常:${}",e.getMessage());
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
     * @param loginVo loginVo
     * @return {@link ResultVO}<{@link UserVo}>
     * @throws BadRequestException 错误请求异常
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "用户注册")
    public ResultVO<UserVo> register(@RequestBody LoginVo loginVo) throws BadRequestException {
        LoginValidation.loginParamsValid(loginVo);
        userLoginService.checkCaptcha(loginVo.getCode(), loginVo.getUuid());
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
