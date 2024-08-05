package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.exceptions.UnableToConnectException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.User;

// Controller -> String (뷰리졸버 동작 --> JSP 파일 찾아서 렌더링 처리 한다.)
// RestController --> 데이터를 반환 처리
@RestController // Controller + @ResponseBody 
public class Test1Controller {
	
	// localhost:8080/test1
	@GetMapping("/test1")
	public User test1() {
		// Gson --> JSON 형식으로 반환 --> String 응답 처리
		
		try {
			
			int result = 10 / 0;
		} catch (Exception e) {
			throw new UnAuthorizedException("인증이 안된 사용자 입니다.", HttpStatus.UNAUTHORIZED);
		}
		return User.builder().username("길동").password("asd123").build();
		
	}

}
