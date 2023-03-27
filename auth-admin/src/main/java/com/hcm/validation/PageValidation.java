package com.hcm.validation;

import com.hcm.common.exception.BadRequestException;
import com.hcm.common.vo.PageVo;
import com.hcm.common.vo.RoleVo;

/**
 * page入参验证
 *
 * @author pc
 * @date 2023/03/25
 */
public class PageValidation {
    public static void isPassPageSizeOrNum(PageVo pageVo){
        if (pageVo.getPageNum() == null || pageVo.getPageSize() == null) {
            throw new BadRequestException("pageNum或者pageSize未传");
        }
    }
}
