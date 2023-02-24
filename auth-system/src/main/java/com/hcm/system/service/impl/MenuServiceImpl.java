package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysMenu;
import com.hcm.system.mapper.MenuMapper;
import com.hcm.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author pc
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    /**
     * 获得菜单列表
     */
    @Override
    public List<SysMenu> getMenuList() {
        List<SysMenu> sysMenuList = menuMapper.getMenuList();
        List<SysMenu> sysMenuResult = new ArrayList<>();
        if (sysMenuList != null) {
            Map<Long, List<SysMenu>> map = new HashMap<>();
            sysMenuList.forEach(sysMenu -> {
                List<SysMenu> childrenList = map.get(sysMenu.getParentId());
                if (childrenList == null) {
                    childrenList = new ArrayList<>();
                }
                childrenList.add(sysMenu);
                map.put(sysMenu.getParentId(), childrenList);
            });
            Long num = 0L;
            sysMenuList.forEach(sysMenu -> {
                if (sysMenu.getParentId().equals(0L)) {
                    sysMenu.setChildren(map.get(sysMenu.getMenuId()));
                    sysMenuResult.add(sysMenu);
                }
            });
        }
        return sysMenuResult;
    }
}
