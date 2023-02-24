package com.hcm.controller;

import com.hcm.common.core.entity.SysMenu;
import com.hcm.common.vo.MenuVo;
import com.hcm.system.mapper.MenuMapper;
import com.hcm.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pc
 */
@Slf4j
@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表
     *
     * @return {@link List}<{@link MenuVo}>
     */
    @GetMapping("")
    public List<MenuVo> getMenuList() {
        List<SysMenu> sysMenuList = menuService.getMenuList();
        List<MenuVo> menuVoList = new ArrayList<>();
        sysMenuList.forEach(sysMenu -> {
            MenuVo menu = new MenuVo();
            BeanUtils.copyProperties(sysMenu, menu);
            List<MenuVo> childrens = new ArrayList<>();
            if (sysMenu.getChildren() != null) {
                sysMenu.getChildren().forEach(children -> {
                    MenuVo child = new MenuVo();
                    BeanUtils.copyProperties(children, child);
                    childrens.add(child);
                });
            }
            menu.setChildren(childrens);
            menuVoList.add(menu);
        });
        return menuVoList;
    }

}
