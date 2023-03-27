package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author pc
 */
@Mapper
public interface MenuMapper {

    /**
     * 获取菜单列表
     *
     * @return {@link List}<{@link SysMenu}>
     */
    List<SysMenu> getMenuList();

    /**
     * 得到所有菜单列表
     *
     * @return {@link List}<{@link SysMenu}>
     */
    List<SysMenu> getMenuListAll();
}
