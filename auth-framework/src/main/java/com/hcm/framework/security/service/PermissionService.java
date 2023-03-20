package com.hcm.framework.security.service;

import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.enums.ResultCodeEnum;
import com.hcm.common.exception.AuthException;
import com.hcm.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pc
 */
@Slf4j
@Service("ss")
public class PermissionService {
    @Resource
    private UserMapper userMapper;
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
            throw new AuthException(ResultCodeEnum.UNACCESS.getCode(),ResultCodeEnum.UNACCESS.getMessage());
        }
        UserDetail user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("PermissionService ---> hasPermission:{}",user);
        if(user.getPermissions().contains(ALL_PERMISSION)){
            return true;
        }
        if(!user.getPermissions().contains(permission)){
            throw new AuthException(ResultCodeEnum.UNACCESS.getCode(),ResultCodeEnum.UNACCESS.getMessage());
        }
        return true;
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户id
     * @return {@link Set}<{@link String}>
     */
    public Set<String> getUserPermissionById(Long userId){
        Set<String> userPermission = new HashSet<>();
        if(userId.equals(1L)){
            userPermission.add(ALL_PERMISSION);
        }else{
            userPermission = userMapper.getUserPermissionById(userId);
        }
        return userPermission;
    }
}
