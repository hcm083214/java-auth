package com.hcm.common.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author pc
 */
@Data
public class UserVo {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;


    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 登录名称
     */
    private String nickName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;


    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 登陆凭证
     */
    private String token;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 最后登录IP
     */
    private String loginIp;


    /**
     * 用户拥有的角色集合
     */
    private List<Long> roles;

    /**
     * 用户拥有的权限集合
     */
    private List<Long> permissions;

    /**
     * 岗位组
     */
    private Long[] postIds;

}
