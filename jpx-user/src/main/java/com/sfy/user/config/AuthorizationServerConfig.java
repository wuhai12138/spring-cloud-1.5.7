package com.sfy.user.config;

import com.sfy.boot.redis.RedisAuthenticationCodeServices;
import com.sfy.user.service.auth.RedisClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;


/**
 * Created by 金鹏祥 on 2019/5/18.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    RedisClientDetailsService redisClientDetailsService;
    @Resource
    RedisAuthenticationCodeServices redisAuthenticationCodeServices;
    @Bean
    public RedisTokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12); // token有效期自定义设置，默认12小时
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 30);//默认30天，这里修改
        tokenServices.setClientDetailsService(redisClientDetailsService);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(authenticationManager);
        endpoints.authorizationCodeServices(redisAuthenticationCodeServices);
        endpoints.tokenServices(defaultTokenServices());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(redisClientDetailsService);
        redisClientDetailsService.loadAllClientToCache();

//        clients.inMemory()
//                .withClient("clientId")
//                .scopes("xx")
//                .secret("secretId")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
////                .autoApprove(true)
////                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code", "custom")
//                .and()
//                .withClient("webapp")
//                .scopes("xx")
//                .authorizedGrantTypes("implicit")
//                .and()
//                .withClient("browser")
//                .authorizedGrantTypes("refresh_token", "password")
//                .scopes("ui");


        /*clients.inMemory()
                .withClient("clientId")
                .scopes("xx")
                .secret("secretId")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .and()
                .withClient("webapp")
                .scopes("xx")
                .authorizedGrantTypes("implicit")
                .and()
                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password")
                .scopes("ui");*/
    }
}
