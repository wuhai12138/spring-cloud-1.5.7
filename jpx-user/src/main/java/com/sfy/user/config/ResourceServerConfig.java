package com.sfy.user.config;


import com.lorne.tx.compensate.repository.CompensateDataSource;
import com.sfy.boot.constant.BootConstant;
import com.sfy.boot.endpoint.AuthExceptionEntryPoint;
import com.sfy.boot.endpoint.CustomAccessDeniedHandler;
import com.sfy.boot.redis.RedisAuthenticationCodeServices;
import com.sfy.boot.tools.MyBearerTokenExtractor;
import com.sfy.boot.tools.Mysource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by SuperS on 2017/9/25.
 *
 * @author SuperS
 */
@Configuration
@EnableResourceServer
@ComponentScan(basePackages ={
        BootConstant.BOOT_REDIS,
        BootConstant.BOOT_HYSTRIX,
        BootConstant.BOOT_MYBATIS_PLUS,
        BootConstant.BOOT_TRANSACTION,
        BootConstant.BOOT_SWAGGER,
        BootConstant.BOOT_EXCEPTION,
        BootConstant.BOOT_FEIGN,
        BootConstant.BOOT_ENDPOINT,
        BootConstant.BOOT_MY_CONFIG,
        BootConstant.BOOT_BUS})
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Resource
    private RedisAuthenticationCodeServices redisAuthenticationCodeServices;
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        return new Mysource().dataSource(env);
    }

    @Bean
    public CompensateDataSource compensateDataSource() {
        return new Mysource().compensateDataSource(env);
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
//                .authenticationEntryPoint(new AuthExceptionEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui.html",//让他们可以通过spring的加密体系
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/webjars/**"
                ).permitAll()
                /*.antMatchers("/auth*//**").permitAll()*/
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/token/**").permitAll()
                .antMatchers("/feign/**/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/registerByMobile").permitAll()
                .antMatchers("/user/verCodeLogin").permitAll()
                .antMatchers("/user/findAccount").permitAll()
                .antMatchers("/user/resetPwd").permitAll()
                .antMatchers("/sysUser/sysUserLogin").permitAll()
                .antMatchers("/valid/imageCode").permitAll()
                .antMatchers("/valid/mobileCode").permitAll()
                .antMatchers("/token/refreshToken").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenExtractor(new MyBearerTokenExtractor(redisAuthenticationCodeServices))
                .authenticationEntryPoint(new AuthExceptionEntryPoint()).accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}
