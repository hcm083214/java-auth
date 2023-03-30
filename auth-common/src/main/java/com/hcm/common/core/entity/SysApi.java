package com.hcm.common.core.entity;

import lombok.Data;

/**
 * sys api
 *
 * @author pc
 * @date 2023/03/29
 */
@Data
public class SysApi {

    /**
     * api id
     */
    private Long apiId;

    /**
     * api名称,对应 method 的名称
     */
    private String apiName;

    /**
     * api 的 url 地址
     */
    private String apiUrl;

    /**
     * api描述,对应 @ApiOperation 的 notes 或者 value
     */
    private String apiDescription;

    /**
     * 请求方法 ,get or post
     */
    private String requestMethod;

    /**
     * controller 的类名
     */
    private String controllerType;

    /**
     * controller 名称,对应 @Api 的 tags
     */
    private String controllerName;

    /**
     * 权限，对应 @PreAuthorize("@ss.hasPermission('permission:function:import')") 的 permission:function:import
     */
    private String permission;
}
