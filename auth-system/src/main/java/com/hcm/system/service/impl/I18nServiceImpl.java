package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysI18n;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.I18nVo;
import com.hcm.system.mapper.I18nMapper;
import com.hcm.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * I18nServiceImpl
 *
 * @author pc
 * @date 2023/04/23
 */
@Slf4j
@Service
public class I18nServiceImpl implements I18nService {

    @Autowired
    private I18nMapper i18nMapper;

    /**
     * 得到i18n列表
     *
     * @param i18nVo i18n签证官
     * @return {@link List}<{@link SysI18n}>
     */
    @Override
    public List<SysI18n> getI18nList(I18nVo i18nVo) {
        return i18nMapper.getI18nList(i18nVo);
    }

    /**
     * 添加i18n
     *
     * @param i18nVos i18n vos
     */
    @Override
    public void addI18ns(List<I18nVo> i18nVos) {
        if (!isExistInDB(i18nVos)) {
            i18nMapper.addI18ns(i18nVos);
        } else {
            throw new BadRequestException("插入的数据重复");
        }

    }

    /**
     * 编辑i18n
     *
     * @param i18nVo i18n签证官
     */
    @Override
    public void editI18n(I18nVo i18nVo) {
        i18nMapper.editI18n(i18nVo);
    }

    /**
     * 是存在于数据库
     * 数据是否存在于数据库
     *
     * @param i18nVos i18n vos
     * @return {@link Boolean}
     */
    private Boolean isExistInDB(List<I18nVo> i18nVos) {
        Integer counter = i18nMapper.localeAndKeyCounter(i18nVos);
        return counter > 0;
    }

    private Boolean isExistInDB(I18nVo i18nVo) {
        List<I18nVo> i18nVos = new ArrayList<>(1);
        i18nVos.add(i18nVo);
        Integer counter = i18nMapper.localeAndKeyCounter(i18nVos);
        return counter > 0;
    }
}
