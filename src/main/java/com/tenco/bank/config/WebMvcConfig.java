package com.tenco.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bank.handler.AuthInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration // 하나 이상의 bean을 등록
@RequiredArgsConstructor // 생성자 대신에 사용할 수 있음
public class WebMvcConfig implements WebMvcConfigurer {
	// implements WebMvcConfigurer : 설정 파일로 사용할 수 있다. 
	
	@Autowired // DI 
	private final AuthInterceptor authInterceptor; // final 사용하면 @Autowired 사용못함
	
	// @RequiredArgsConstructor // 생성자 대신에 사용할 수 있음
	
	// 우리가 만들어 놓은 AuthInterceptor를 등록해야 함.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
				.addPathPatterns("/account/**") // /account 뒤에 있는 모든 디렉토리 파일을 의미한다.
				.addPathPatterns("/auth/**");
			
	} // end of addInterceptors()
	
	// 코드 추가
	// C:\Lightshot/a.png <-- 서버 컴퓨터상에 실체 이미지 경로지만 
	// 프로젝트 상에서 (클라이언트가 HTML 소스로 보이는 경로는) /images/uploads./**
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 1. 
		registry.addResourceHandler("/images/uploads/**")
		.addResourceLocations("file:\\C:\\work_spring\\upload/");
	}
	
	
	
	
	@Bean // IOC 대상 (싱글톤 처리)
	PasswordEncoder passwordEncoder() { // 회원가입 기능에 DI 해야한다. --> @bean 사용
		return new BCryptPasswordEncoder(); // 회원가입할 때 비밀번호 암호화
	}
	
	
}
