package com.hcm.controller;

import com.hcm.common.annotations.EnumValue;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.enums.RoleSearchTypeEnum;
import com.hcm.common.enums.TripartiteSourceEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.utils.uuid.UUID;
import com.hcm.common.vo.LoginVo;


import com.hcm.common.vo.UserVo;
import com.hcm.framework.security.JwtManager;
import com.hcm.framework.security.context.AuthenticationContextHolder;
import com.hcm.framework.security.service.PermissionService;
import com.hcm.framework.security.service.UserLoginService;
import com.hcm.system.service.RoleService;
import com.hcm.system.service.UserService;
import com.hcm.tripartite.TripartiteConfiguration;
import com.hcm.validation.LoginValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pc
 * @description 用户
 */
@Slf4j
@RestController
@Api(tags = "登陆注册管理")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private TripartiteConfiguration tripartiteConfiguration;

    @Autowired
    private RoleService roleService;

    /**
     * 登录
     *
     * @param loginVo LoginVo
     * @return {@link ResultVO}<{@link LoginVo}>
     * @throws BadRequestException 错误请求异常
     * @description
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
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
    @ApiOperation(value = "用户信息查询", notes = "获取用户信息")
    @PreAuthorize("@ss.hasPermission('user:info:query')")
    public ResultVO<UserVo> getUserInfo() throws BadRequestException {
        UserDetail user = AuthenticationContextHolder.getCurrentUser();
        UserVo userVo = new UserVo();
        List<String> perms = user.getPermissions();
        List<Long> roles = userService.getUserRolesById(user.getSysUser().getUserId());
        BeanUtils.copyProperties(user.getSysUser(), userVo);
        userVo.setRoleIds(roles);
        userVo.setPermissions(perms);
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
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public ResultVO<UserVo> register(@RequestBody LoginVo loginVo) throws BadRequestException {
        LoginValidation.loginParamsValid(loginVo);
        userLoginService.checkCaptcha(loginVo.getCode(), loginVo.getUuid());
        loginVo.setPassword(passwordEncoder.encode(loginVo.getPassword()));
        SysUser user = new SysUser();
        BeanUtils.copyProperties(loginVo, user);
        user.setUserType(0L);
        SysUser oriUser = userService.getUserInfoByName(user.getUserName());
        if (oriUser != null) {
            throw new BadRequestException("用户已经存在");
        }
        userService.insertUser(user);
        return ResultVO.success();
    }

    /**
     * 第三方登录:获取第三方授权页面
     *
     * @param source 第三方
     * @return {@link ResultVO}<{@link LoginVo}>
     * @throws BadRequestException 错误请求异常
     */
    @GetMapping("/login-third-party")
    @ApiOperation(value = "第三方登录授权页面URL查询", notes = "第三方登录，获取到授权页面url")
    public ResultVO<LoginVo> preLoginByThirdParty(@Validated @EnumValue(enumClass = TripartiteSourceEnum.class, ignoreCase = true, message = "传入的参数不正确")
                                                  @RequestParam("source") String source) throws BadRequestException {
        if (StringUtils.isEmpty(source)) {
            throw new BadRequestException("source 未传");
        }
        LoginVo loginVo = new LoginVo();
        AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
                .clientId(tripartiteConfiguration.getClientId(source))
                .clientSecret(tripartiteConfiguration.getClientSecret(source))
                .redirectUri(tripartiteConfiguration.getRedirectUri(source))
                .build());
        String uuid = UUID.fastUUID().toString();
        log.info("preLoginByThirdParty ---> UUID:{}", uuid);
        String authorizeUrl = authRequest.authorize(uuid);
        loginVo.setAuthorizeUrl(authorizeUrl);
        loginVo.setUuid(uuid);
        return ResultVO.success(loginVo);
    }

    /**
     * 第三方登录
     *
     * @param loginVo loginVo
     * @return {@link ResultVO}<{@link LoginVo}>
     */
    @PostMapping("/login-third-party")
    @ApiOperation(value = "第三方登录", notes = "第三方登录，将第三方给过来的授权信息保存到本地数据库")
    public ResultVO<LoginVo> loginByThirdParty(@RequestBody LoginVo loginVo) {
        LoginValidation.loginByThirdPartyParamsValid(loginVo);
        log.info("loginByThirdParty ---> UUID:{}", loginVo.getUuid());
        AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
                .clientId(tripartiteConfiguration.getClientId(loginVo.getSource()))
                .clientSecret(tripartiteConfiguration.getClientSecret(loginVo.getSource()))
                .redirectUri(tripartiteConfiguration.getRedirectUri(loginVo.getSource()))
                .build());
        AuthResponse<AuthUser> login = authRequest.login(AuthCallback.builder().state(loginVo.getUuid()).code(loginVo.getCode()).build());
        AuthUser authUser = login.getData();
        SysUser sysUser = userService.giteeUser2User(authUser);
        SysUser oriUser = userService.getUserInfoByName(sysUser.getUserName());
        if (oriUser == null) {
            userService.insertUser(sysUser);
        }else{
            sysUser.setUserId(oriUser.getUserId());
        }
        List<SysRole> roleList = roleService.getUserRoleInfoById(sysUser.getUserId());
        sysUser.setRoleList(roleList);
        UserDetail userDetail = new UserDetail(sysUser, permissionService.getUserPermissionByUser(sysUser));
        loginVo.setToken(jwtManager.generate(userDetail));
        return ResultVO.success(loginVo);
    }
}
