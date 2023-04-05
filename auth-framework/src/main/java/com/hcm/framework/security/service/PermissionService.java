package com.hcm.framework.security.service;

import com.hcm.common.core.entity.SysResource;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import com.hcm.common.utils.StringUtils;
import com.hcm.framework.security.context.AuthenticationContextHolder;
import com.hcm.system.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author pc
 */
@Slf4j
@Service("ss")
public class PermissionService {

    @Autowired
    private ResourceService resourceService;

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 是否有权限
     *
     * @param permission 权限
     * @return boolean
     */
    public boolean hasPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            throw new AuthException(ResultCodeEnum.UNACCESS.getCode(), ResultCodeEnum.UNACCESS.getMessage());
        }
        UserDetail user = AuthenticationContextHolder.getCurrentUser();
        log.info("PermissionService ---> hasPermission:{}", user);
        if (user.getPermissions().contains(ALL_PERMISSION)) {
            return true;
        }
        if (!user.getPermissions().contains(permission)) {
            throw new AuthException(ResultCodeEnum.UNACCESS.getCode(), ResultCodeEnum.UNACCESS.getMessage());
        }
        return true;
    }

    /**
     * 获取用户权限
     *
     * @param sysUser 系统用户
     * @return {@link List}<{@link String}>
     */
    public List<String> getUserPermissionById(SysUser sysUser) {
        Set<String> userPermission = new HashSet<>();
        if (sysUser.getUserId().equals(1L)) {
            userPermission.add(ALL_PERMISSION);
        } else {
            List<SysResource> resourceList = resourceService.getUserResource(sysUser);
            resourceList.forEach(sysResource -> {
                if (StringUtils.isNotNull(sysResource.getPerms())) {
                    userPermission.add(sysResource.getPerms());
                }
            });
        }
        return new ArrayList<>(userPermission);
    }

}
