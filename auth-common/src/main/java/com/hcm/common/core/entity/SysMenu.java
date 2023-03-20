package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;
import com.hcm.common.vo.MenuVo;
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
public class SysMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private String orderNum;

    /**
     * 菜单URL
     */
    private String component;

    private String path;

    /**
     * 打开方式（menuItem页签 menuBlank新窗口）
     */
    private String target;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 是否刷新（0刷新 1不刷新）
     */
    private String isRefresh;

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
    private List<SysMenu> children;

    public static void pos2vos(List<SysMenu> pos,List<MenuVo> vos){
        pos.forEach(sysMenu -> {
            MenuVo menu = new MenuVo();
            BeanUtils.copyProperties(sysMenu, menu);
            List<MenuVo> childrens = new ArrayList<>();
            if (sysMenu.getChildren() != null) {
                pos2vos(sysMenu.getChildren(),childrens);
            }
            menu.setChildren(childrens);
            vos.add(menu);
        });
    }
}
