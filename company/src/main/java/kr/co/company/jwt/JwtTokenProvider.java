package kr.co.company.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

// Spring Security와 JWT 토큰을 사용하여 인증과 권한 부여를 처리하는 클래스
// JWT 토큰의 생성, 복호화, 검증 기능 담당
@Slf4j
@Component
public class JwtTokenProvider {
	
	private final Key key;
	
	// application.properties의 jwt.secret 값 key에 저장
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	// Member 정보로 AccessToken, RefreshToken 생성
	public JwtTokenDto generateToken(Authentication authentication) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		long now = new Date().getTime();
		
		// Access Token 생성
		Date accessTokenExpiresIn = new Date(now + 86400000);
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())
				.claim("auth", authorities)
				.setExpiration(accessTokenExpiresIn)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		// Refresh Token 생성
		String refreshToken = Jwts.builder()
				.setExpiration(new Date(now + 86400000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
		
		return JwtTokenDto.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}
	
	// Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	// 주어진 Access token을 복호화하여 사용자의 인증 정보(Authentication)를 생성
	// 토큰의 Claims에서 권한 정보를 추출하고 User 객체를 생성하여 Authentication 객체로 반환
	public Authentication getAuthentication(String accessToken) {
		// Jwt 토큰 복호화
		Claims claims = parseClaims(accessToken);
		
		if(claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}
		
		// 클레임에서 권한 정보 가져오기
		// 권한 정보를 다양한 타입의 객체로 처리하고 더 큰 유연성과 확장성을 가지기 위해 Collection<? extends GrantedAuthority> 사용
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(",")) // 토큰의 클레임에서 auth 클레임은 토큰에 저장된 권한 정보를 나타냄
				.map(SimpleGrantedAuthority::new) // 가져온 권한 정보를 SimpleGrantedAuthority 객체로 변환하여 컬렉션에 추가
				.collect(Collectors.toList());
		
		// UserDetails(interface)를 구현한 User(UserDetails를 구현한 class) 객체를 만들어 주체(subject)와 권한 정보 설정
		// claim.getSubject()는 주어진 토큰의 클레임에서 "sub" 클레임의 값을 반환 -> 이는 토큰의 주체를 나타냄 (ex. 사용자의 식별자나 이메일 주소)
		UserDetails principal = new User(claims.getSubject(), "", authorities);
		
		// UsernamePasswordAuthenticationToken 객체를 생성하여 주체와 권한 정보를 포함한 인증(Authentication) 객체를 생성
		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}
	
	// 주어진 Access token을 복호화하고 만료된 토큰인 경우에도 Claims 반환
	// 클레임(Claims): 토큰에서 사용할 정보의 조각
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(accessToken) // parseClaimsJws(): JWT 토큰의 검증과 파싱을 모두 수행
					.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	
	// 주어진 토큰을 검증하여 유효성 확인
	public boolean validateToken(String token) {
		try {
			// 토큰의 서명 키 설정
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			
			return true;
		// 예외 처리를 통해 토큰의 유효성 여부 판단
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) { // 토큰이 올바른 형식이 아니거나 클레임이 비어있는 경우
			log.info("JWT claims string is empty", e);
		}
		
		return false;
	}

}
