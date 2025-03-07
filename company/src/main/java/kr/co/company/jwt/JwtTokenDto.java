package kr.co.company.jwt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtTokenDto {
	
	private String grantType;
	private String accessToken;
	private String refreshToken;

}
