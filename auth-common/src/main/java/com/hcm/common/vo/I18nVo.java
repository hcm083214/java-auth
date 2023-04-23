package com.hcm.common.vo;

import com.hcm.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * i18n Vo
 *
 * @author pc
 * @date 2023/04/23
 */
@Data
public class I18nVo extends BaseEntity {
    private static final long serialVersionUID = 1L;
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
