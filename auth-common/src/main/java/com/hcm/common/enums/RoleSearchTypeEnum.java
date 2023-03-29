package com.hcm.common.enums;

/**
 * 角色联想搜索类型枚举
 *
 * @author pc
 * @date 2023/03/23
 */
public enum RoleSearchTypeEnum {
    // 角色中文名称
    ROLE_NAME_CN("角色中文名称"),
    // 角色英文名称
    ROLE_NAME_EN("角色英文名称"),
    // 权限字符
    FUNCTION_KEY("权限字符");

    private final String description;
    RoleSearchTypeEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
