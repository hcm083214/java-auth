package com.hcm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.PageVo;
import com.hcm.common.vo.RoleVo;
import com.hcm.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pc
 */
@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("@ss.hasPermission('system:role:list')")
    public ResultVO<PageVo<RoleVo>> getRoleList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            throw new BadRequestException(ResultCodeEnum.FAILED.getMessage());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> roles = roleService.getRoles();
        PageInfo<SysRole> pageList = new PageInfo<>(roles);
        PageVo<RoleVo> pageVo = new PageVo<>();
        BeanUtils.copyProperties(pageList, pageVo);
        if (pageList.getList().size() > 0) {
            List<RoleVo> roleVos = new ArrayList<>();
            pageList.getList().forEach(role -> {
                RoleVo roleVo = new RoleVo();
                BeanUtils.copyProperties(role, roleVo);
                roleVos.add(roleVo);
            });
            pageVo.setList(roleVos);
        }
        return ResultVO.success(pageVo);
    }
}
