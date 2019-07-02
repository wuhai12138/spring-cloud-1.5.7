package com.sfy.quartz.config;

import com.lorne.tx.compensate.repository.CompensateDataSource;
import com.sfy.boot.constant.BootConstant;
import com.sfy.boot.endpoint.AuthExceptionEntryPoint;
import com.sfy.boot.endpoint.CustomAccessDeniedHandler;
import com.sfy.boot.redis.RedisAuthenticationCodeServices;
import com.sfy.boot.tools.MyBearerTokenExtractor;
import com.sfy.boot.tools.Mysource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

/****
 * @Description 资源配置
 * @Param
 * @Author jpx
 * @Version  1.0
 * @Return
 * @Exception
 * @Date 2019/5/24
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
//        BootConstant.BOOT_FEIGN,
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
                .authenticationEntryPoint(new AuthExceptionEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui.html",//让他们可以通过spring的加密体系
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**"
                ).permitAll()
                .antMatchers("/test/v1/testGet").permitAll()
                .antMatchers("/test/v1/testPost").permitAll()
                .antMatchers("/job/addjob").permitAll()
                .antMatchers("/job/pausejob").permitAll()
                .antMatchers("/job/resume").permitAll()
                .antMatchers("/job/reschedule").permitAll()
                .antMatchers("/job/delete").permitAll()
                .antMatchers("/job/**").permitAll()
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