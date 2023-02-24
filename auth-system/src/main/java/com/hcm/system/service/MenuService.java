package com.hcm.system.service;

import com.hcm.common.core.entity.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pc
 */

public interface MenuService {
    /**
     * 获得菜单列表
     * * @return {@link List}<{@link SysMenu}>
     */
    List<SysMenu> getMenuList();
}
