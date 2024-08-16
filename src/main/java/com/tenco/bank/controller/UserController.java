package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.tenco.bank.dto.KakaoProfile;
import com.tenco.bank.dto.OAuthToken;
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

	private final HttpSession session; // 세션 // final은 단 한번 초기화 해야한다.

	@Value("${tenco.key}")
	private String tencoKey;

	// DI 처리
	@Autowired // 노란색 경고는 사용할 필요는 없음 -- 가독성을 위해서 선언해도 됨
	public UserController(UserService service, HttpSession session) {
		this.userService = service;
		this.session = session; // 메모리에 띄울려면 생성자에서 선언
	}

	/**
	 * 회원 가입 페이지 요청 주소 설계 : http://localhost:8080/user/sign-up
	 * 
	 * @return signUp.jsp
	 */
	@GetMapping("/sign-up")
	public String signUpPage() {
		return "user/signUp";
	}

	/**
	 * 회원 가입 로직 처리 요청 주소 설계 : http://localhost:8080/user/sign-up
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/sign-up")
	public String signUpProc(SignUpDTO dto) {
		// controller 에서 일반적이 코드 작업
		// 1. 인증검사 (여기서는 인증검사 불 필요)
		// 2. 유효성 검사
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_USERNAME, HttpStatus.BAD_REQUEST);
		}

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}

		if (dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_FULLNAME, HttpStatus.BAD_REQUEST);
		}
		// 서비스 객체로 전달
		userService.createUser(dto);
		return "redirect:/user/sign-in";
	}

	/**
	 * 로그인 화면 요청 주소설계 : http://localhost:8080/user/sign-in
	 * 
	 * @return
	 */
	@GetMapping("/sign-in")
	public String signInPage() {
		// 인증검사 , 유효성 검사 로그인 페이지에서 할 필요가 없다.

		return "user/signIn";
	}

	/**
	 * 회원 로그인 요청 처리 주소설계 : http://localhost:8080/user/sign-in
	 * 
	 * @return
	 */
	@PostMapping("/sign-in")
	public String signProc(SignInDTO dto) {

		// 1, 인증 검사 x
		// 2. 유효성 검사 O
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_USERNAME, HttpStatus.BAD_REQUEST);
		} // 이름 검사

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new DataDeliveryException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		} // 패스워드 검사

		// 서비스 호출
		User principal = userService.readUser(dto); // DB에 조회된 데이터가 들어가고

		// 세션 메모리에 등록 처리
		session.setAttribute(Define.PRINCIPAL, principal); // User 값 세션에 등록완룐

		// 새로운 페이지로 이동 처리
		// TODO - 계좌 목록 페이지 이동처리 예정
		return "redirect:/account/list";
	}

	/**
	 * 로그아웃 기능 추가 로그아웃 처리 코드 추가
	 * 
	 * @return
	 */
	@GetMapping("/logout")
	public String logout() {

		session.invalidate(); // 로그아웃 됨 (세션 무효화 된다.)

		return "redirect:/user/sign-in";
	}

	/**
	 * 카카오
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping("/kakao")
	public String getMethodName(@RequestParam(name = "code") String code) {
		System.out.println("code : " + code); // 인가 코드 받기

		// POST - 카카오 토큰 요청
		// Header, body 구성
		RestTemplate rt1 = new RestTemplate(); // REST 방식의 API를 요청하고 json , xml , String 등 응답받을 수 있다, (비동기)
		// rest 방식 : 네트워크 상에서 Client와 Server 사이의 통신 방식

		// 헤더 구성
		HttpHeaders header1 = new HttpHeaders();
		header1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 바디 구성
		MultiValueMap<String, String> params1 = new LinkedMultiValueMap<String, String>();
		// MultiValueMap : 보통의 Map과는 달리, 동일한 키에 대해 여러 개의 값을 가질 수 있습니다. , 하나의 키에 여러 개의 값을
		// 매핑
		params1.add("grant_type", "authorization_code");
		params1.add("client_id", "4338cd3d11df8f8f1654fff2e37bd659");
		params1.add("redirect_uri", "http://localhost:8080/user/kakao");
		params1.add("code", code);

		// 헤더 + 바디 결합
		// HttpEntity : Http 통신에 사용되는 객체 --> 헤더와 바디 정보를 담고 있다.
		HttpEntity<MultiValueMap<String, String>> reqKakaoMessage = new HttpEntity<>(params1, header1);

		// 통신 요청
		// ResponseEntity : 전체 HTTP 응답(상태 코드, 헤더 및 본문)을 포함한다.
		ResponseEntity<OAuthToken> response1 = rt1.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				reqKakaoMessage, OAuthToken.class);

		System.out.println("response1 : " + response1.getBody().toString());
		// return response1.getBody().getAccessToken();
		// -------------------------------------------------------------------

		// 카카오 리소스서버 사용자 정보 가져오기
		RestTemplate rt2 = new RestTemplate(); // REST 방식의 API를 요청하고 json , xml , String 등 응답받을 수 있다, (비동기

		// 헤더 설정
		HttpHeaders headers2 = new HttpHeaders();
		// GET/ POST 둘 다 사용 가능 - 카카오 문서 -
		// GET 에는 body 영역이 필요 없다.
		// 반드시 Bearer 값 다음에 공백한칸 추가해야 한다. !!
		headers2.add("Authorization", "Bearer " + response1.getBody().getAccessToken()); // Bearer : 앞에 무조건 공백
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// 본문 X

		// HTTP Entity 만들기
		HttpEntity<MultiValueMap<String, String>> reqkakaoInfoMessage = new HttpEntity<>(headers2);

		// 통신 요청
		// ResponseEntity<String> response2 =
		// rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
		// reqkakaoInfoMessage, String.class);
		// System.out.println("response2 : " + response2);
		// return response2.getBody();

		// 통신 요청
		ResponseEntity<KakaoProfile> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				reqkakaoInfoMessage, KakaoProfile.class);
		System.out.println("KakaoProfile : " + response2.getBody());
		KakaoProfile kakaoProfile = response2.getBody();
		// return kakaoProfile.toString();

		// ------- 카카오 사용자 정보 응답 완료

		// 최조 사용자라면 자동 회원 가입 처리 (우리 서버)
		// 회원가입 이력이 있는 사용자라면 바로 세션 처리 (우리 서버)
		// 사전기반 --> 소셜 사용자는 비밀번호를 입력하는가? 안하는가?
		// 우리서버에 회원가입시에 --> password -> not null (무조건 만들어야 함 DB 정책)

		// 1. 회원가입 데이터 생성

		SignUpDTO signUpDTO = SignUpDTO.builder()
				.username(kakaoProfile.getProperties().getNickname() + "_" + kakaoProfile.getId())
				.fullname("OAuth_" + kakaoProfile.getProperties().getNickname()).password(tencoKey).build();

		// 2. 우리사이트 최소 소셜 사용자 인지 판별해야 한다.
		User oldUser = userService.searchUsername(signUpDTO.getUsername());
		if (oldUser == null) {
			// 사용자가 최초 소셜 로그인 사용자 임 (처음 방문)
			oldUser = new User();
			signUpDTO.setOriginFileName(kakaoProfile.getProperties().getThumbnailImage());
			userService.createUser(signUpDTO);
			// 고민 !!!
			oldUser.setUsername(signUpDTO.getUsername());
			oldUser.setPassword(null);
			oldUser.setFullname(signUpDTO.getFullname());
			// 프로필 이미지 여불에 따라 조건식 추가
			// signUpDTO.setOriginFileName(code)
		}

		// 자동 로그인 처리
		session.setAttribute(Define.PRINCIPAL, oldUser);

		return "redirect:/account/list";
	}

}
