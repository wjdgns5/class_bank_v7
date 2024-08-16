
package com.tenco.bank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

// JSON 형식에 코딩 컨벤션이 스네이크 케이스를 카멜 어노테이션으로 할당하라!
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) // Strategies : 전략
@Data
@ToString
public class OAuthToken {

    public String accessToken;

    public String tokenType;

    public String refreshToken;

    public Integer expiresIn;

    public String scope;
  
    public Integer refreshTokenExpiresIn;

}
