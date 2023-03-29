package com.hcm.common.enums;

/**
 * 功能权限联想搜索类型枚举
 *
 * @author pc
 * @date 2023/03/28
 */
public enum FunctionSearchTypeEnum {
    // 权限中文名称
    FUNCTION_NAME_CN("权限中文名称"),
    // 权限英文名称
    FUNCTION_NAME_EN("权限英文名称"),
    // 权限字符
    FUNCTION_KEY("权限字符");
    private final String description;
    FunctionSearchTypeEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
