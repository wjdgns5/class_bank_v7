package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SignInDTO;
import com.tenco.bank.dto.SignUpDTO;
import com.tenco.bank.handler.exception.DataDeliveryException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

@Controller // IoC에 대상(싱글톤 패턴으로 관리됨) 
@RequestMapping("/user") // 대문 처리 
public class UserController {
	
	
	private UserService userService;
	
	private final HttpSession session; // 세션  // final은 단 한번 초기화 해야한다.
	
	// DI 처리
	@Autowired // 노란색 경고는 사용할 필요는 없음 -- 가독성을 위해서 선언해도 됨
	public UserController(UserService service, HttpSession session) {
		this.userService = service;
		this.session = session; // 메모리에 띄울려면 생성자에서 선언
	}

	/**
	 * 회원 가입 페이지 요청 
	 * 주소 설계 : http://localhost:8080/user/sign-up
	 * @return signUp.jsp 
	 */
	@GetMapping("/sign-up")
	public String signUpPage() {
		return "user/signUp";
	}
	
	/**
	 * 회원 가입 로직 처리 요청
	 * 주소 설계 : http://localhost:8080/user/sign-up
	 * @param dto
	 * @return
	 */
	@PostMapping("/sign-up")
	public String signUpProc(SignUpDTO dto) {
		// controller 에서 일반적이 코드 작업 
		// 1. 인증검사 (여기서는 인증검사 불 필요) 
		// 2. 유효성 검사 
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_USERNAME, HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_PASSWORD , HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_FULLNAME, HttpStatus.BAD_REQUEST);
		}
		// 서비스 객체로 전달 
		userService.createUser(dto);
		return "redirect:/user/sign-in";
	}
	
	/**
	 * 로그인 화면 요청
	 * 주소설계 : http://localhost:8080/user/sign-in
	 * @return
	 */
	@GetMapping("/sign-in")
	public String signInPage() {
		// 인증검사 , 유효성 검사 로그인 페이지에서 할 필요가 없다.
		
		return "user/signIn";
	}
	
	/**
	 * 회원 로그인 요청 처리
	 * 주소설계 : http://localhost:8080/user/sign-in
	 * @return
	 */
	@PostMapping("/sign-in")
	public String signProc(SignInDTO dto) {
		
		// 1, 인증 검사 x
		// 2. 유효성 검사 O
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_USERNAME, HttpStatus.BAD_REQUEST);
		} // 이름 검사
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		} // 패스워드 검사
		
		// 서비스 호출
		User principal =  userService.readUser(dto); // DB에 조회된 데이터가 들어가고
		
		// 세션 메모리에 등록 처리
		session.setAttribute(Define.PRINCIPAL, principal); // User 값 세션에 등록완룐
		
		// 새로운 페이지로 이동 처리 
		// TODO - 계좌 목록 페이지 이동처리 예정
		return "redirect:/account/list";
	}
	
	/**
	 * 로그아웃 기능 추가
	 * 로그아웃 처리
	 * 코드 추가
	 * @return
	 */
	@GetMapping("/logout")
	public String logout() {
		
		session.invalidate(); // 로그아웃 됨 (세션 무효화 된다.)
		
		return "redirect:/user/sign-in";
	}
	
}
