package com.hcm.controller.deploy;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysMenu;
import com.hcm.common.vo.FunctionVo;
import com.hcm.common.vo.MenuVo;
import com.hcm.system.mapper.MenuMapper;
import com.hcm.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "菜单和按钮权限管理")
@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表,type = "M"和 "C"
     *
     * @return {@link List}<{@link MenuVo}>
     */
    @GetMapping("")
    @ApiOperation(value = "获取菜单列表",notes = "获取菜单列表")
    public ResultVO<List<MenuVo>> getMenuList() {
        List<SysMenu> sysMenuList = menuService.getMenuList();
        List<MenuVo> menuVoList = new ArrayList<>();
        SysMenu.pos2vos(sysMenuList,menuVoList);
        return ResultVO.success(menuVoList);
    }

    /**
     * 获得功能权限列表,type = "M"， "C" 和 "F"
     *
     * @return {@link ResultVO}<{@link List}<{@link MenuVo}>>
     */
    @GetMapping("/all")
    @ApiOperation(value = "获得功能权限列表",notes = "获得功能权限列表,包括页面，菜单，按钮权限")
    public ResultVO<List<MenuVo>> getPermissionList(){
        List<SysMenu> sysMenuList = menuService.getMenuListAll();
        List<MenuVo> menuVoList = new ArrayList<>();
        SysMenu.pos2vos(sysMenuList,menuVoList);
        return ResultVO.success(menuVoList);
    }

}
