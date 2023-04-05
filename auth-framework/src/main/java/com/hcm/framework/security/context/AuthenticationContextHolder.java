package com.hcm.framework.security.context;


import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 身份验证上下文
 *
 * @author pc
 * @date 2023/02/24
 */
@Slf4j
@Component
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置上下文
     *
     * @param authentication 身份验证
     */
    public static void setContext(Authentication authentication) {
        CONTEXT_HOLDER.set(authentication);
    }

    /**
     * 设置安全上下文
     *
     * @param authentication 身份验证
     */
    public static void setSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获得上下文
     *
     * @return {@link Authentication}
     */
    public static Authentication getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 获取当前用户
     *
     * @return {@link UserDetail}
     */
    public static UserDetail getCurrentUser() {
        UserDetail user;
        try {
            user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("AuthenticationContextHolder ---> getCurrentUser,获取用户信息异常:${}", e.getMessage());
            throw new BadRequestException("获取用户信息异常");
        }
        return user;
    }

    /**
     * 删除上下文，特别注意 ThreadLocal一定要在 finally 中清除：因为当前线程执行完相关代码后，很可能会被重新放入线程池中，
     * 如果ThreadLocal没有被清除，该线程执行其他代码时，会把上一次的状态带进去。
     */
    public static void removeContext() {
        CONTEXT_HOLDER.remove();
    }
}
