package com.project.easyBuild.config;


import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableWebMvc
@ComponentScan("controllers")
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}

	//로그인 세션 체크
	@Autowired
	private SessionInterceptor sessionInterceptor;

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(sessionInterceptor)
//				.addPathPatterns("/my/**", "/qa/**", "/review/**") //체크 필요한 패턴
//				.excludePathPatterns("/login", "/register"); //체크 제외할 패턴
//	}
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FormHttpMessageConverter());
        //Jackson HttpMessageConverter를 추가
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}