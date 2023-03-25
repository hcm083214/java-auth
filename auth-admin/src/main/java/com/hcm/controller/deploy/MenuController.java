package com.hcm.controller.deploy;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.core.entity.SysMenu;
import com.hcm.common.vo.FunctionVo;
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
    public ResultVO<List<MenuVo>> getMenuList() {
        List<SysMenu> sysMenuList = menuService.getMenuList();
        List<MenuVo> menuVoList = new ArrayList<>();
        SysMenu.pos2vos(sysMenuList,menuVoList);
        return ResultVO.success(menuVoList);
    }
}
