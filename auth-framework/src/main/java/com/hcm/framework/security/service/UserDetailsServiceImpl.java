package com.hcm.framework.security.service;

import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.utils.StringUtils;
import com.hcm.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pc
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.getNameAndPasswordByName(username);
        // 若没查询到一定要抛出该异常，这样才能被Spring Security的错误处理器处理
        if (StringUtils.isNull(user)) {
            throw new UsernameNotFoundException("没有找到该用户");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        // 走到这代表查询到了实体对象，返回我们自定义的UserDetail对象（这里权限暂时放个空集合，后面我会讲解）
        return new UserDetail(user, authorities);
    }
}
