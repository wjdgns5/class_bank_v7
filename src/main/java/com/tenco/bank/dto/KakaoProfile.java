package com.tenco.bank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

//JSON 형식에 코딩 컨벤션이 스네이크 케이스를 카멜 어노테이션으로 할당하라!
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) // Strategies : 전략
@Data
@ToString
public class KakaoProfile {
	
	private Long id; // 21억 숫자 넘어가서
	private String connectedAt;
	private properties properties;
	
	

}
