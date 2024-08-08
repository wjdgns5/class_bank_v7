package com.tenco.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SaveDTO;
import com.tenco.bank.handler.exception.DataDeliveryException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;

import jakarta.servlet.http.HttpSession;

@Controller // IOC 대상 (싱글톤으로 관리)
@RequestMapping("/account")
public class AccountController {

	// 계좌 생성 화면 요청 - DI 처리
	private final HttpSession session;
	private final AccountService accountService;

	@Autowired
	public AccountController(HttpSession session, AccountService accountService) {
		this.session = session; // 의존 주입
		this.accountService = accountService; // 의존 주입
	}

	/**
	 * 계좌 생성 페이지 요청 주소설계 : http://localhost:8080/account/save :
	 * 
	 * @return
	 */
	@GetMapping("/save")
	public String savePage() {

		// 1. 인증 검사가 필요 (account 전체가 필요함)
		User principal = (User) session.getAttribute("principal");
		if (principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}

		return "account/save";
	}

	/**
	 * 계좌 생성 기능 요청 
	 * 주소 설계 : http://localhost:8080/account/save
	 * @retrun : 추후 계좌 목록 페이지 이동 처리
	 */
	
	@PostMapping("/save")
	public String saveProc(SaveDTO dto) {
		// 1. form 데이터 추출 (파싱 전략) 
		// 2. 인증 검사 
		// 3. 유효성 검사 
		// 4. 서비스 호출 
		User principal = (User)session.getAttribute("principal"); 
		
		// 인증 검사
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new DataDeliveryException("계좌 번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException("계좌 비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getBalance() == null || dto.getBalance() <= 0) {
			throw new DataDeliveryException("계좌 잔액을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
		// 서비스에 dto와 유저아이디를 보낸다.
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/index";
	}
	
	/**
	 * 계좌 목록 화면 요청
	 * 주소 설계 : http://localhost:8080/account/list, ..../
	 * @return list.jsp
	 */
	// 페이지 리턴해야 되서 string으로 짓는다.
	@GetMapping({"/list", "/"})
	public String listPage(Model model) {
		
		// 1. 인증검사
		User principal = (User) session.getAttribute("principal"); // 유저세션 가져옴
		if(principal == null) {
			// 로그인을 안한 상태
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		// 2. 유효성 검사
		
		// 3. 서비스 호출 (서비스컨트롤러 : 핵심기능 )
		List<Account> accountList = accountService.readAccountListByUserId(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList); // 모델에서 키,값을 던져버린다.
		}
	
		// JSP 데이터를 넣어 주는 방법
		return "account/list";
		
	}

}
