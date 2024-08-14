package com.tenco.bank.repository.model;

import java.sql.Timestamp;

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
public class User {
	
	private Integer id;
	private String username;
	private String password;
	private String fullname;
	
	private String originFileName; // 파일 업로드 관련 
	private String uploadFileName; // 파일 업로드 관련 
	
	private Timestamp createdAt;
	
	// 이미지 파일명 작업?
	public String setUpUserImage() {
		
	
		// 만약에 업로드 파일이름이 없다면 기본값 https://picsum.photos/id/1/350  아니라면
		// 내 서버 컴퓨터 경로에 있는 /images/uploads/ + uploadFileName 찾아라
		return uploadFileName == null ? "https://picsum.photos/id/1/350": "/images/uploads/" + uploadFileName;
	}
}
