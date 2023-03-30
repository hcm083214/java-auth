package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;
import com.hcm.common.vo.ResourceVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author pc
 */
@Data
public class SysResource extends BaseEntity {
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
     * 请求方法
     */
    private String requestMethod;

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
    private List<SysResource> children;

    public static void pos2vos(List<SysResource> pos, List<ResourceVo> vos){
        pos.forEach(sysMenu -> {
            ResourceVo menu = new ResourceVo();
            BeanUtils.copyProperties(sysMenu, menu);
            List<ResourceVo> childrens = new ArrayList<>();
            if (sysMenu.getChildren() != null) {
                pos2vos(sysMenu.getChildren(),childrens);
            }
            menu.setChildren(childrens);
            vos.add(menu);
        });
    }
}
