package com.hcm.controller.deploy;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.utils.ExcelUtils;
import com.hcm.common.vo.PageVo;
import com.hcm.common.vo.RoleVo;
import com.hcm.system.service.RoleService;
import com.hcm.validation.PageValidation;
import com.hcm.validation.RoleValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * 获取角色列表
     *
     * @param roleVo roleVo
     * @param page   pageVo
     * @return {@link ResultVO}<{@link PageVo}<{@link RoleVo}>>
     */
    @GetMapping
    @PreAuthorize("@ss.hasPermission('permission:role:query')")
    public ResultVO<PageVo<RoleVo>> getRoleList(@Validated RoleVo roleVo,@Validated PageVo page) {
        PageValidation.isPassPageSizeOrNum(page);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<SysRole> roles = roleService.getRoles(roleVo);
        PageInfo<SysRole> pageList = new PageInfo<>(roles);
        PageVo<RoleVo> pageVo = new PageVo<>();
        // 转 Vo
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

    /**
     * 得到参数列表,供角色查询项目的联想搜索
     *
     * @param roleVo 签证官角色
     * @return {@link List}<{@link String}>
     */
    @GetMapping("/params")
    @PreAuthorize("@ss.hasPermission('permission:role:query')")
    public ResultVO<List<String>> getParamsList(@Validated RoleVo roleVo){
        List<String> result = roleService.getParamsList(roleVo);
        return ResultVO.success(result);
    }

    /**
     * 角色列表导出到 excel
     *
     * @param response 响应
     * @param roleVo   roleVo
     * @throws IOException ioexception
     */
    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('permission:role:export')")
    public void export(HttpServletResponse response,@Validated RoleVo roleVo,@Validated PageVo pageVo) throws IOException {
        // 入参校验
        PageValidation.isPassPageSizeOrNum(pageVo);
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<SysRole> roles = roleService.getRoles(roleVo);
        // 转 VO
        PageInfo<SysRole> pageList = new PageInfo<>(roles);
        List<RoleVo> roleVos = new ArrayList<>();
        if (pageList.getList().size() > 0) {
            pageList.getList().forEach(sysRole -> {
                RoleVo role = new RoleVo();
                BeanUtils.copyProperties(sysRole, role);
                roleVos.add(role);
            });
        }
        // excel导出
        ExcelUtils.export(response, "角色列表", "角色", RoleVo.class, roleVos);
    }

    /**
     * 导出角色列表模板
     *
     * @param response 响应
     * @throws IOException ioexception
     */
    @GetMapping("/import-template")
    @PreAuthorize("@ss.hasPermission('permission:role:export')")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        List<RoleVo> roleVos = Arrays.asList(
                RoleVo.builder().roleId(1L).roleNameCn("管理员").roleNameEn("admin").functionKey("admin").roleSort(1).build(),
                RoleVo.builder().roleId(2L).roleNameCn("普通用户").roleNameEn("custom").functionKey("custom").roleSort(2).build());
        ExcelUtils.export(response, "导入模板", "角色", RoleVo.class, roleVos);
    }

    /**
     * 导入excel
     *
     * @param file 文件
     * @return {@link ResultVO}<{@link ?}>
     * @throws IOException ioexception
     */
    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermission('permission:role:import')")
    public ResultVO<?> importExcel(@RequestBody MultipartFile file)throws IOException{
        List<RoleVo> list = ExcelUtils.excel2List(file, RoleVo.class);
        roleService.insertRole(list);
        return ResultVO.success("上传成功");
    }
}