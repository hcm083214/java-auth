package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysI18n;
import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.I18nVo;
import com.hcm.system.mapper.I18nMapper;
import com.hcm.system.service.I18nService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param i18nVo i18n签证官
     */
    @Override
    public void addI18n(I18nVo i18nVo) {
        List<SysI18n> sysI18nList = i18nMapper.getI18nList(i18nVo);
        if(sysI18nList.size()>0){
            throw new BadRequestException("插入的数据重复");
        }
        i18nMapper.addI18n(i18nVo);
    }

    /**
     * 编辑i18n
     *
     * @param i18nVo i18n签证官
     */
    @Override
    public void editI18n(I18nVo i18nVo) {
        List<SysI18n> sysI18nList = i18nMapper.getI18nList(i18nVo);
        if(sysI18nList.size() != 1){
            throw new BadRequestException("传入的id错误");
        }
        i18nMapper.editI18n(i18nVo);
    }
}
