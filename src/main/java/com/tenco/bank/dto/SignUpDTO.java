package com.tenco.bank.dto;

import org.springframework.web.multipart.MultipartFile;

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
	
	private MultipartFile mFile; // 해당하는 파일에 대한 데이터를 받아낼 수 있다.
	// 리스트가 아닌 배열로 받는 이유 : 리스트가 가끔씩 잘 안먹혀서 
	private String originFileName;
	private String uploadFileName; 
	
	// 2단계 로직 -- User Object 반환
	public User toUser() {
		return User.builder()
				.username(this.username)
				.password(this.password)
				.fullname(this.fullname)
				.originFileName(this.originFileName)
				.uploadFileName(this.uploadFileName)
				.build();
	}
	
	// TODO - 추후 사진 업로드 기능 추가 예정
}
