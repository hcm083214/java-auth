package com.hcm.validation;

import com.hcm.common.exception.BadRequestException;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.vo.ResourceVo;

/**
 * 资源入参校验
 *
 * @author pc
 * @date 2023/03/31
 */
public class ResourceValidation {
    /**
     * 编辑菜单入参校验
     *
     * @param resourceId 资源id
     * @param resourceVo 资源签证官
     */
    public static void editMenuValidation(Long resourceId, ResourceVo resourceVo) throws BadRequestException {
        if (!resourceVo.getResourceId().equals(resourceId)) {
            throw new BadRequestException("resourceId错误");
        }
        addMenuValidation(resourceVo);
    }

    /**
     * 添加菜单验证
     *
     * @param resourceVo resourceVo
     */
    public static void addMenuValidation(ResourceVo resourceVo) {
        if (StringUtils.isNull(resourceVo.getResourceType()) || resourceVo.getResourceType().equals("F")) {
            throw new BadRequestException("resourceType 必须为'M' 和'C'且不为空");
        }
        if (StringUtils.isNull(resourceVo.getResourceName())) {
            throw new BadRequestException("resourceName未传");
        }
        if (resourceVo.getResourceType().equals("C") && StringUtils.isNull(resourceVo.getComponent())) {
            throw new BadRequestException("component未传");
        }
        if (StringUtils.isNull(resourceVo.getPath())) {
            throw new BadRequestException("path未传");
        }
        if (StringUtils.isNull(resourceVo.getParentId())) {
            throw new BadRequestException("parentId未传");
        }
        if (StringUtils.isNull(resourceVo.getIcon())) {
            throw new BadRequestException("icon未传");
        }
    }
}
