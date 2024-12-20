package com.project.easyBuild.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("controllers")
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}

	@Autowired
	private SessionInterceptor sessionInterceptor;

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(sessionInterceptor)
//				.addPathPatterns("/my/**", "/qa/**", "/review/**") //체크 필요한 패턴
//				.excludePathPatterns("/login", "/register"); //체크 제외할 패턴
//	}
}