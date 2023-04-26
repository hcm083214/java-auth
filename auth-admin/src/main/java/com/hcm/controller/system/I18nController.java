package com.hcm.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysI18n;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.I18nVo;
import com.hcm.common.vo.PageVo;
import com.hcm.framework.security.context.AuthenticationContextHolder;
import com.hcm.system.service.I18nService;
import com.hcm.validation.I18nValidation;
import com.hcm.validation.PageValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

/**
 * i18n控制器
 *
 * @author pc
 * @date 2023/04/23
 */
@Slf4j
@RestController
@RequestMapping("/i18n")
@Api(tags = "国际化管理")
public class I18nController {

    @Autowired
    private I18nService i18nService;

    /**
     * 分页获取国际化数据列表
     *
     * @param i18nVo i18n签证官
     * @param page   页面
     * @return {@link ResultVO}<{@link PageVo}<{@link I18nVo}>>
     */
    @GetMapping
    @PreAuthorize("@ss.hasPermission('system:i18n:query')")
    @ApiOperation(value = "国际化数据查询", notes = "分页获取国际化数据列表")
    public ResultVO<PageVo<I18nVo>> getI18nList(@Validated I18nVo i18nVo, @Validated PageVo page) {
        PageValidation.isPassPageSizeOrNum(page);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<SysI18n> sysI18nList = i18nService.getI18nList(i18nVo);
        PageInfo<SysI18n> pageList = new PageInfo<>(sysI18nList);
        List<I18nVo> i18nVoList = SysI18n.pos2vos(sysI18nList);

        PageVo<I18nVo> pageVo = new PageVo<>();
        BeanUtils.copyProperties(pageList, pageVo);
        pageVo.setList(i18nVoList);

        return ResultVO.success(pageVo);
    }

    /**
     * 根据语言环境获取全部的国际化数据
     *
     * @param i18nVo i18n签证官
     * @return {@link ResultVO}<{@link List}<{@link I18nVo}>>
     */
    @GetMapping("/all")
    @ApiOperation(value = "国际化语言包查询", notes = "根据语言环境获取全部的国际化数据")
    public ResultVO<List<I18nVo>> getI18nAllList(@Validated I18nVo i18nVo) {
        List<SysI18n> sysI18nList = i18nService.getI18nList(i18nVo);

        List<I18nVo> result = new ArrayList<>(sysI18nList.size());
        sysI18nList.forEach(sysI18n -> {
            I18nVo i18n = new I18nVo();
            BeanUtils.copyProperties(sysI18n, i18n);
            result.add(i18n);
        });
        return ResultVO.success(result);
    }

    /**
     * 新增国际化数据
     *
     * @param i18nVo i18n签证官
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermission('system:i18n:add')")
    @ApiOperation(value = "国际化数据新增", notes = "新增国际化数据")
    public ResultVO<String> addI18nList(@RequestBody  @Validated List<I18nVo> i18nVos) {
        if(i18nVos.size() == 0){
            throw new BadRequestException("传递的数据为空");
        }
        i18nVos.forEach(i18nVo -> {
            I18nValidation.addValidation(i18nVo);
            SysUser user = AuthenticationContextHolder.getCurrentUserInfo();
            i18nVo.setCreateBy(user.getUserName());
        });

        i18nService.addI18ns(i18nVos);
        return ResultVO.success("新增成功");
    }

    /**
     * 修改国际化数据
     *
     * @param i18nVo i18n签证官
     * @param i18nId i18n id
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/i18n_id/{i18nId}")
    @PreAuthorize("@ss.hasPermission('system:i18n:edit')")
    @ApiOperation(value = "国际化数据修改", notes = "修改国际化数据")
    public ResultVO<String> editI18nList(@RequestBody @Validated I18nVo i18nVo, @PathVariable("i18nId")Long i18nId) {
        i18nVo.setI18nId(i18nId);
        I18nValidation.editValidation(i18nVo);
        SysUser user = AuthenticationContextHolder.getCurrentUserInfo();
        i18nVo.setUpdateBy(user.getUserName());
        i18nService.editI18n(i18nVo);
        return ResultVO.success("修改成功");
    }
}




