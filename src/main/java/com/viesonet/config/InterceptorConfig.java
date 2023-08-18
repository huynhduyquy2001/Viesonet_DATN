package com.viesonet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.viesonet.component.AccessTimeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private AccessTimeInterceptor accessTimeInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessTimeInterceptor).addPathPatterns("/**")
		.excludePathPatterns("/login", "/register", "/dangnhap", "/dangky", "dangky/guima", "/forgotpassword", "quenmatkhau/guima", "/quenmatkhau/xacnhan");;

	}

}
