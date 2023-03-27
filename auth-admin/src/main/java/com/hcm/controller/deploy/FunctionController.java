package com.hcm.controller.deploy;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysFunction;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.FunctionVo;
import com.hcm.common.vo.MenuVo;
import com.hcm.common.vo.PageVo;
import com.hcm.system.service.FunctionService;
import com.hcm.validation.PageValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 *
 * @author pc
 * @date 2023/03/25
 */
@RestController
@RequestMapping("/functions")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    /**
     * 得到函数列表
     * 得到功能权限列表
     *
     * @param functionVo functionVo
     * @param page       pageVo
     * @return {@link ResultVO}<{@link PageVo}<{@link FunctionVo}>>
     */
    @GetMapping
    @PreAuthorize("@ss.hasPermission('permission:function:query')")
    public ResultVO<PageVo<FunctionVo>> getFunctionList(FunctionVo functionVo, @Validated PageVo page) {
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


    @GetMapping("/function_id/{functionId}")
    public ResultVO<List<Long>> getPermIdListByFunId(@PathVariable("functionId") Long functionId) {
        if(functionId == null){
            throw new BadRequestException("functionId不能为空");
        }
        List<Long> permissionId = functionService.getPermIdListByFunId(functionId);
        return ResultVO.success(permissionId);
    }
}
