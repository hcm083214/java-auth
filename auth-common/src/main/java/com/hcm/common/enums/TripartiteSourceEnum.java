package com.hcm.common.enums;

/**
 * 三方源枚举
 *
 * @author pc
 * @date 2023/04/12
 */
public enum TripartiteSourceEnum {
    // gitee
    GITEE("gitee", 1L);


    private final String description;

    /**
     * 用户类型
     */
    private Long type;

    public String getDescription() {
        return description;
    }

    public Long getType() {
        return type;
    }

    TripartiteSourceEnum(String description) {
        this.description = description;
    }

    TripartiteSourceEnum(String description, Long type) {
        this.description = description;
        this.type = type;
    }
}
