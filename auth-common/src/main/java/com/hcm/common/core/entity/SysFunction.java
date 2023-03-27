package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 功能权限实体类
 *
 * @author pc
 * @date 2023/03/25
 */
@Data
public class SysFunction extends BaseEntity {
    /**
     * 功能权限id
     */
    private Long functionId;

    /**
     * 功能权限中文名称
     */
    private String functionNameCn;

    private String functionNameEn;

    /**
     * 功能权限字符串
     */
    private String functionKey;


    /**
     * 功能权限中文描述
     */
    private String functionDescriptionCn;

    /**
     * 功能权限英文描述
     */
    private String functionDescriptionEn;

    /**
     * 菜单列表json
     */
    private String menuListJson;

    /**
     * 状态(0:无效，1：有效，2：软删除)
     */
    private String status;

}
