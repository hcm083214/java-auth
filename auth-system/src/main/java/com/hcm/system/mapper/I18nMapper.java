package com.hcm.system.mapper;

import com.hcm.common.core.entity.SysI18n;
import com.hcm.common.vo.I18nVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * i18n映射器
 *
 * @author pc
 * @date 2023/04/23
 */
@Mapper
public interface I18nMapper {

    /**
     * 得到i18n列表
     *
     * @param i18nVo i18n签证官
     * @return {@link List}<{@link SysI18n}>
     */
    List<SysI18n> getI18nList(@Param("i18n") I18nVo i18nVo);

    /**
     * 添加i18n
     *
     * @param i18nVo i18n签证官
     */
    void addI18n(@Param("i18n") I18nVo i18nVo);

    /**
     * 编辑i18n
     *
     * @param i18nVo i18n签证官
     */
    void editI18n(@Param("i18n") I18nVo i18nVo);
}
