package com.tenco.bank.dto;

import com.tenco.bank.repository.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpDTO {
	// 화면에서 사용자의 id pw  등 받는 객체를 만든다.
	
	private String username;
	private String password;
	private String fullname;
	
	// 2단계 로직 -- User Object 반환
	public User toUser() {
		return User.builder()
				.username(this.username)
				.password(this.password)
				.fullname(this.fullname)
				.build();
	}
	
	
	// TODO - 추후 사진 업로드 기능 추가 예정
	
	

}
