package com.hcm.controller.deploy;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.ExcelUtils;
import com.hcm.common.vo.FunctionVo;
import com.hcm.common.vo.PageVo;
import com.hcm.system.service.FunctionService;
import com.hcm.validation.FunctionValidation;
import com.hcm.validation.PageValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 权限控制器
 *
 * @author pc
 * @date 2023/03/25
 */
@Api(tags = "功能权限管理")
@RestController
@RequestMapping("/functions")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    /**
     * 得到功能权限列表
     *
     * @param functionVo functionVo
     * @param page       pageVo
     * @return {@link ResultVO}<{@link PageVo}<{@link FunctionVo}>>
     */
    @GetMapping
    @PreAuthorize("@ss.hasPermission('permission:function:query')")
    @ApiOperation(value = "得到功能权限列表", notes = "得到功能权限列表的详情")
    public ResultVO<PageVo<FunctionVo>> getFunctionList(FunctionVo functionVo, @Validated PageVo page) throws BadRequestException {
        PageValidation.isPassPageSizeOrNum(page);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<SysFunction> functionList = functionService.getFunctionList(functionVo);
        PageInfo<SysFunction> pageList = new PageInfo<>(functionList);
        PageVo<FunctionVo> pageVo = new PageVo<>();
        // 转 Vo
        BeanUtils.copyProperties(pageList, pageVo);
        if (pageList.getList().size() > 0) {
            List<FunctionVo> functionVos = new ArrayList<>();
            pageList.getList().forEach(sysFunction -> {
                FunctionVo functions = new FunctionVo();
                BeanUtils.copyProperties(sysFunction, functions);
                functionVos.add(functions);
            });
            pageVo.setList(functionVos);
        }
        return ResultVO.success(pageVo);
    }

    /**
     * 添加功能权限
     *
     * @param functionVo functionVo
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermission('permission:function:query')")
    @ApiOperation(value = "新增功能权限", notes = "新增功能权限")
    public ResultVO<String> addFunction(@Validated @RequestBody FunctionVo functionVo) throws BadRequestException {
        FunctionValidation.editValidation(functionVo);
        boolean isNameExist = functionService.isInsertExist(functionVo);
        if (isNameExist) {
            throw new BadRequestException("中文名或英文名称重复");
        }
        functionService.insertFunction(functionVo);
        return ResultVO.success("添加成功");
    }

    /**
     * 根据 functionId 得到功能权限（菜单和按钮）的id列表
     *
     * @param functionId 函数id
     * @return {@link ResultVO}<{@link List}<{@link Long}>>
     */
    @GetMapping("/function_id/{functionId}")
    @PreAuthorize("@ss.hasPermission('permission:function:query')")
    @ApiOperation(value = "根据 functionId 得到 menuId list", notes = "根据 functionId 得到功能权限（菜单和按钮）的id列表")
    public ResultVO<List<Long>> getPermIdListByFunId(@PathVariable("functionId") Long functionId) throws BadRequestException {
        FunctionValidation.isPassFunctionId(functionId);
        List<Long> permissionId = functionService.getPermIdListByFunId(functionId);
        return ResultVO.success(permissionId);
    }

    /**
     * 得到参数列表,供功能权限查询项目的联想搜索
     *
     * @param functionVo functionVo
     * @return {@link ResultVO}<{@link List}<{@link String}>>
     */
    @GetMapping("/params")
    @PreAuthorize("@ss.hasPermission('permission:function:query')")
    @ApiOperation(value = "得到参数列表", notes = "得到参数列表,供功能权限查询项目的联想搜索")
    public ResultVO<List<String>> getParamsList(@Validated FunctionVo functionVo) {
        List<String> paramsList = functionService.getParamsList(functionVo);
        return ResultVO.success(paramsList);
    }

    /**
     * 编辑功能权限信息
     *
     * @param functionVo functionVo
     * @param functionId id
     * @return {@link ResultVO}<{@link String}>
     * @throws BadRequestException 错误请求异常
     */
    @PostMapping("/function_id/{functionId}")
    @PreAuthorize("@ss.hasPermission('permission:function:edit')")
    @ApiOperation(value = "编辑功能权限信息", notes = "编辑指定的 functionId 的信息和相关联的 menuId List")
    public ResultVO<String> editFunctionInfo(@Validated @RequestBody FunctionVo functionVo, @PathVariable("functionId") Long functionId) throws BadRequestException {
        // 如果一张表里面有多个数据范围的权限信息，需要验证id的有效性：即当前数据范围下的id是否存在
        FunctionValidation.isPassFunctionId(functionId);
        FunctionValidation.editValidation(functionVo);
        functionVo.setFunctionId(functionId);
        functionService.editFunctionInfo(functionVo);
        return ResultVO.success("修改成功");
    }

    /**
     * 权限详情导出到excel
     *
     * @param response   响应
     * @param functionVo functionVo
     * @param pageVo     pageVo
     */
    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('permission:function:export')")
    @ApiOperation(value = "权限详情导出", notes = "权限详情导出到excel")
    public void export(HttpServletResponse response, @Validated FunctionVo functionVo, @Validated PageVo pageVo) throws IOException {
        PageValidation.isPassPageSizeOrNum(pageVo);
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<SysFunction> functionList = functionService.getFunctionList(functionVo);
        List<FunctionVo> functionVos = new ArrayList<>();
        if (functionList.size() > 0) {
            functionList.forEach(sysFunction -> {
                FunctionVo functions = new FunctionVo();
                BeanUtils.copyProperties(sysFunction, functions);
                functionVos.add(functions);
            });
        }
        ExcelUtils.export(response, "功能权限", "功能权限列表", FunctionVo.class, functionVos);
    }

    /**
     * 导出模板
     *
     * @param response 响应
     * @throws IOException ioexception
     */
    @GetMapping("/export-template")
    @PreAuthorize("@ss.hasPermission('permission:function:export')")
    @ApiOperation(value = "导出模板",notes = "导出模板")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        List<FunctionVo> functionVos = Arrays.asList(FunctionVo.builder().functionId(1001L).functionNameCn("管理员权限").
                        functionDescriptionCn("系统的所有权限").functionKey("admin").status("1").build(),
                FunctionVo.builder().functionId(1002L).functionNameCn("普通用户权限").
                        functionDescriptionCn("普通用户的所有权限").functionKey("common").status("1").build());
        ExcelUtils.export(response,"功能权限模板", "功能权限列表", FunctionVo.class, functionVos);
    }

    /**
     * 导入功能权限列表
     *
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/import")
    @PreAuthorize("@ss.hasPermission('permission:function:import')")
    @ApiOperation(value = "功能权限列表导入",notes = "功能权限列表导入")
    public ResultVO<String> importFunctionList(@RequestBody MultipartFile file)throws IOException{
        List<FunctionVo> functionVos = ExcelUtils.excel2List(file,FunctionVo.class);
        functionService.insertFunctionList(functionVos);
        return ResultVO.success("导入成功");
    }
}
