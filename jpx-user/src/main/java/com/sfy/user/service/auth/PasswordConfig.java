package com.sfy.user.service.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* 密码工具类
*/
@Configuration
public class PasswordConfig {

	@Bean
	public Md5PasswordEncoder passwordEncoder()	{
		return new Md5PasswordEncoder();
	}

	/*@Bean
	public PasswordEncoder passwordEncoderBC()	{
		return new BCryptPasswordEncoder();
	}*/
}
