package com.tenco.bank.dto;

import com.tenco.bank.repository.model.Account;

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
public class SaveDTO {
	
	private String number; // 계좌번호 1002-1234
	private String password; // 비밀번호 1234 : String 인 이유 암호화 할때 문자열(String) 값이 들어간다. 
	private Long balance; // 남은 잔액 100000
	
	
	public Account toAccount(Integer userId) {
		return Account.builder()
				.number(this.number)
				.password(this.password)
				.balance(this.balance)
				.userId(userId) 
				.build();
	}
	
	
}
