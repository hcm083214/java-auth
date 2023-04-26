package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;
import com.hcm.common.vo.I18nVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * sys i18n
 *
 * @author pc
 * @date 2023/04/23
 */
@Data
public class SysI18n extends BaseEntity {
    /**
     * i18n id
     */
    private Long i18nId;

    /**
     * 语言环境
     */
    private String locale;

    /**
     * 模块
     */
    private String i18nModule;

    /**
     * 键值
     */
    private String i18nKey;

    /**
     * 值
     */
    private String i18nValue;

    public static List<I18nVo> pos2vos(List<SysI18n> sysI18nList) {
        List<I18nVo> i18nVoList = new ArrayList<>(sysI18nList.size());
        sysI18nList.forEach(sysI18n -> {
            I18nVo i18n = new I18nVo();
            BeanUtils.copyProperties(sysI18n, i18n);
            i18nVoList.add(i18n);
        });
        return i18nVoList;
    }
}
