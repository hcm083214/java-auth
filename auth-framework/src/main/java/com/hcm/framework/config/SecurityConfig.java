package com.hcm.framework.config;


import com.hcm.framework.security.filter.AuthFilter;
import com.hcm.framework.security.handler.AuthErrorHandler;
import com.hcm.framework.security.handler.AccessErrorHandler;
import com.hcm.framework.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;

/**
 * @author pc
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private AuthErrorHandler authErrorHandler;

    @Autowired
    private AccessErrorHandler accessErrorHandler;

    @Autowired
    private UserDetailsServiceImpl userService;

    /**
     * @description 决定了Spring Security对哪些接口进行保护、什么组件生效、某些功能是否启用等等都需要在配置类中进行配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf和frameOptions，如果不关闭会影响前端请求接口
        http.csrf().disable();
        http.headers().frameOptions().disable();
        // 开启跨域以便前端调用接口
        http.cors();

        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http.authorizeRequests()
                // 注意这里，是允许前端跨域联调的一个必要配置
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 指定某些接口不需要通过验证即可访问,但是会走过滤器链。像登陆、注册接口肯定是不需要认证的
                .antMatchers("/login","/register","/captcha/image").permitAll()
                // 这里意思是其它所有接口需要认证才能访问
                .antMatchers("/**").authenticated()
                // 指定认证错误处理器
                .and().exceptionHandling()
                // 认证错误处理器
                .authenticationEntryPoint(authErrorHandler)
                // 授权错误处理器
                .accessDeniedHandler(accessErrorHandler);

        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 在UsernamePasswordAuthenticationFilter 前添加自定义的登录认证过滤器
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        // 使用自定义的权限授权过滤器代替默认的
//        http.addFilterBefore(new AccessFilter(), FilterSecurityInterceptor.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置 UserDetailService 和加密器
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * @description 自定义加密规则和校验规则
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用bcrypt加密算法，安全性比较高
        return new BCryptPasswordEncoder();
    }
}
