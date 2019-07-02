package com.sfy.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 金鹏祥 on 2019/5/15.
 */
@Component
@ConfigurationProperties(prefix = "security.oauth2.client")
@Data
public class Oauth2ClientProperties {

    private String clientId;
    private String clientSecret;
    private String userAuthorizationUri;
    private String accessTokenUri;
}
