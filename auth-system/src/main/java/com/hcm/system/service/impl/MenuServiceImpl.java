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
import java.util.stream.Collectors;

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
            // 获取 parentId = 0的根节点
            sysMenuResult = sysMenuList.stream().filter(sysMenu->sysMenu.getParentId().equals(0L)).collect(Collectors.toList());
            // 根据 parentId 进行分组
            Map<Long, List<SysMenu>> map = sysMenuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
            recursionTree(sysMenuResult,map);
        }
        return sysMenuResult;
    }

    /**
     * 生成递归树
     *
     * @param treeList 树列表
     * @param map      目标
     */
    private void recursionTree(List<SysMenu> treeList,Map<Long, List<SysMenu>> map){
        treeList.forEach(tree->{
            List<SysMenu> childList = map.get(tree.getMenuId());
            tree.setChildren(childList);
            if(tree.getChildren() != null && tree.getChildren().size()>0){
                recursionTree(childList,map);
            }
        });
    }
}
