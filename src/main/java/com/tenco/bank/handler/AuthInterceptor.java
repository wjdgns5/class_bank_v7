package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component // 1개의 class 단위로 등록할 때 사용  // IOC의 대상 (싱글톤 패턴)
			// 하나의 클래스를 IOC 하고 싶다면 사용
public class AuthInterceptor implements HandlerInterceptor {
	
	// preHandle 동작 흐름 (단 / 스프링부트 설정파일, 성정 클래스에 등록되어야 한다. : 특정 URL)
	// 컨트롤러 들어 오기 전에 동작 하는 녀석
	// boolean 의 값은 true/ false 이다.
	// true --> 컨트롤러 안으로 들여 보낸다.
	// false --> 컨트롤러 안으로 못 들어간다. 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 사용자 정보 유추하는 방법 ?
		HttpSession session = request.getSession(); // 세션을 긁어낸다.
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		// 만약에 principal 이 null 이 아닌지 비교
		if(principal == null) {
		throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		
		}
		return true;
	} // end of preHandle()
	
	
	// postHandle
	// 뷰가 렌더링 되기 바로전에 콜백 되는 메서드
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
			// TODO Auto-generated method stub
		
			HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		} // end of postHandle()
		
		
	// afterCompletion'
	// 요청 처리가 완료된 후, 즉 뷰가 완전 렌더링 된 후에 호출 된다.
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	} // end of afterCompletion()

}
