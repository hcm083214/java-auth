package com.hcm.framework.config;

import com.hcm.common.enums.TripartiteSourceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 三方凭证
 *
 * @author pc
 * @date 2023/04/12
 */
@Component
public class TripartiteConfiguration {

    @Autowired
    private TripartiteProperties properties;
    /**
     * 客户机id
     */
    private String clientId;

    /**
     * 客户密钥
     */
    private String clientSecret;

    /**
     * 重定向uri
     */
    private String redirectUri;

    public String getClientId(String source) {
        if(source.equals(TripartiteSourceEnum.GITEE.getDescription())){
            clientId = properties.getGiteeClientId();
        }
        return clientId;
    }

    public String getClientSecret(String source) {
        if(source.equals(TripartiteSourceEnum.GITEE.getDescription())){
            clientSecret = properties.getGiteeClientSecret();
        }
        return clientSecret;
    }

    public String getRedirectUri(String source) {
        if(source.equals(TripartiteSourceEnum.GITEE.getDescription())){
            redirectUri = properties.getGiteeRedirectUri();
        }
        return redirectUri;
    }
}
