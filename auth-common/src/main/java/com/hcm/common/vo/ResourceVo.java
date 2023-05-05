package com.hcm.common.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 */
@Data
public class ResourceVo {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long resourceId;

    /**
     * 菜单名称
     */
    private String resourceName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private String orderNum;

    /**
     * 特指前端组件路径
     */
    private String component;

    /**
     * 前端为路由地址，后端为url
     */
    private String path;

    /**
     * 请求方法 get/post
     */
    private String requestType;

    /**
     * 控制器类
     */
    private String controllerClass;

    /**
     * 控制器名称
     */
    private String controllerName;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源类型（M目录 C菜单 F按钮）
     */
    private String resourceType;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子菜单
     */
    private List<ResourceVo> children;

    /**
     * 资源访问量，map 中 key 为日期和total，值为数量
     */
    private Map<String,Integer> pageCounter;
}
