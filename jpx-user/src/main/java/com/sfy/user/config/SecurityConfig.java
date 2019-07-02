package com.sfy.user.config;


import com.sfy.user.service.auth.JPAUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private MyAuthenticationProvider provider;//自定义验证
    @Autowired
    private JPAUserDetailsService jpaUserDetailsService;

    @Autowired
    Md5PasswordEncoder passwordEncoder;

/*    @Autowired
    PasswordEncoder passwordEncoder;*/

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
////                .antMatchers("/oauth/**").permitAll()
//                .antMatchers(
//                        "/swagger-ui.html",//让他们可以通过spring的加密体系
//                        "/swagger-resources/**",
//                        "/v2/api-docs/**",
//                        "/webjars/**"
//                ).permitAll()
//                /*.antMatchers("/auth*//**").permitAll()*/
//                .antMatchers("/oauth/**").permitAll()
//                .antMatchers("/token/**").permitAll()
//                .antMatchers("/feign/**/**").permitAll()
//                .antMatchers("/user/login").permitAll()
//                .antMatchers("/user/register").permitAll()
//                .antMatchers("/user/registerByMobile").permitAll()
//                .antMatchers("/user/verCodeLogin").permitAll()
//                .antMatchers("/user/findAccount").permitAll()
//                .antMatchers("/user/resetPwd").permitAll()
//                .antMatchers("/sysUser/sysUserLogin").permitAll()
//                .antMatchers("/valid/imageCode").permitAll()
//                .antMatchers("/valid/mobileCode").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .cors()
//                .and()
//                .csrf()
//                .and()
//                .httpBasic()
//                .disable();
//    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsService);
        return daoAuthenticationProvider;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoder);
    }

    //不定义没有password grant_type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        //将验证过程交给自定义验证工具
//        auth.authenticationProvider(provider);
//    }
}