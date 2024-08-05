package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  // Ioc 대상 (싱글톤 패턴)
public class GlobalControllerAdvice {
	
	@ExceptionHandler(value = Exception.class) //예외의 클래스를 잡아라 
	@ResponseBody // 데이터를 반환한다.
	public ResponseEntity<Object> handeleResourceNotFoundException(Exception e) {
		System.out.println("GlobalControllerAdvice : 오류 확인 : ");
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
