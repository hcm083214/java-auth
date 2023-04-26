package com.hcm.validation;

import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.I18nVo;

/**
 * i18n验证
 *
 * @author pc
 * @date 2023/04/23
 */
public class I18nValidation {
    public static void addValidation(I18nVo i18nVo) throws BadRequestException {
        if (StringUtils.isEmpty(i18nVo.getLocale())) {
            throw new BadRequestException("locale 未传");
        }
        if (StringUtils.isEmpty(i18nVo.getI18nKey())) {
            throw new BadRequestException("key 未传");
        }
        if (StringUtils.isEmpty(i18nVo.getI18nValue())) {
            throw new BadRequestException("value 未传");
        }
        if (StringUtils.isEmpty(i18nVo.getI18nModule())) {
            throw new BadRequestException("module 未传");
        }
    }

    public static void editValidation(I18nVo i18nVo) throws BadRequestException {
        if (StringUtils.isNull(i18nVo.getI18nId())) {
            throw new BadRequestException("i18nId 未传");
        }
        addValidation(i18nVo);
    }
}
