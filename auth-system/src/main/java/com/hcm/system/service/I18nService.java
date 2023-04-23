package com.hcm.system.service;

import com.hcm.common.core.entity.SysI18n;
import com.hcm.common.vo.I18nVo;

import java.util.List;

/**
 * i18n服务
 *
 * @author pc
 * @date 2023/04/23
 */
public interface I18nService {

    /**
     * 得到i18n列表
     *
     * @param i18nVo i18n签证官
     * @return {@link List}<{@link SysI18n}>
     */
    List<SysI18n> getI18nList(I18nVo i18nVo);

    /**
     * 添加i18n
     *
     * @param i18nVo i18n签证官
     */
    void addI18n(I18nVo i18nVo);

    /**
     * 编辑i18n
     *
     * @param i18nVo i18n签证官
     */
    void editI18n(I18nVo i18nVo);
}
