package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;

/**
 * sys i18n
 *
 * @author pc
 * @date 2023/04/23
 */
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
    private String module;

    /**
     * 键值
     */
    private String key;

    /**
     * 值
     */
    private String value;
}
