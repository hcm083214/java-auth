package com.hcm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.ExcelUtils;
import com.hcm.common.vo.PageVo;
import com.hcm.common.vo.RoleVo;
import com.hcm.system.service.RoleService;
import com.hcm.validation.RoleValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResultVO<PageVo<RoleVo>> getRoleList(RoleVo roleVo) {
        RoleValidation.rolesSearchParamsValid(roleVo);
        PageHelper.startPage(roleVo.getPageNum(), roleVo.getPageSize());
        List<SysRole> roles = roleService.getRoles();
        PageInfo<SysRole> pageList = new PageInfo<>(roles);
        PageVo<RoleVo> pageVo = new PageVo<>();
        BeanUtils.copyProperties(pageList, pageVo);
        if (pageList.getList().size() > 0) {
            List<RoleVo> roleVos = new ArrayList<>();
            pageList.getList().forEach(sysRole -> {
                RoleVo role = new RoleVo();
                BeanUtils.copyProperties(sysRole, role);
                roleVos.add(role);
            });
            pageVo.setList(roleVos);
        }
        return ResultVO.success(pageVo);
    }

    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('system:role:export')")
    public void export(HttpServletResponse response, RoleVo roleVo) throws IOException {
        RoleValidation.rolesSearchParamsValid(roleVo);
        PageHelper.startPage(roleVo.getPageNum(), roleVo.getPageSize());
        List<SysRole> roles = roleService.getRoles();
        PageInfo<SysRole> pageList = new PageInfo<>(roles);
        List<RoleVo> roleVos = new ArrayList<>();
        if (pageList.getList().size() > 0) {
            pageList.getList().forEach(sysRole -> {
                RoleVo role = new RoleVo();
                BeanUtils.copyProperties(sysRole, role);
                roleVos.add(role);
            });
        }
        ExcelUtils.export(response, "角色列表", "角色", RoleVo.class, roleVos);
    }

    @GetMapping("/import-template")
    @PreAuthorize("@ss.hasPermission('system:role:export')")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        List<RoleVo> roleVos = Arrays.asList(
                RoleVo.builder().roleId(1L).roleName("管理员").roleKey("admin").roleSort(1).build(),
                RoleVo.builder().roleId(2L).roleName("普通用户").roleKey("custom").roleSort(2).build());
        ExcelUtils.export(response, "导入模板", "角色", RoleVo.class, roleVos);
    }

    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermission('system:role:import')")
    public ResultVO<?> importExcel(@RequestBody MultipartFile file)throws IOException{
        List<RoleVo> list = ExcelUtils.excel2List(file, RoleVo.class);
        log.info(String.valueOf(list));
        roleService.insertRole(list);
        return ResultVO.success("上传成功");
    }
}
