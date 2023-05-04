package com.hcm.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 第三方登录属性
 *
 * @author pc
 * @date 2023/04/12
 */
@Component
@ConfigurationProperties("tripartite")
@Data
public class TripartiteProperties {

    /**
     * gitee客户机id
     */
    private String giteeClientId;

    /**
     * gitee客户密钥
     */
    private String giteeClientSecret;

    /**
     * gitee定向uri
     */
    private String giteeRedirectUri;

}
