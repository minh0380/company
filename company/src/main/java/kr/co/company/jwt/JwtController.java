package kr.co.company.jwt;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.dto.ResultDto;
import kr.co.company.member.Member;
import kr.co.company.member.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "4. JWT", description = "JWT 관련 API")
@RequestMapping("jwt")
@RestController
public class JwtController {
	
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	
	public JwtController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@PostMapping("validate")
	@Operation(summary = "jwt 토큰 검증", description = "jwt 토큰 검증 API")
	public ResponseEntity<ResultDto<Object>> validate(@Parameter(name = "token", description = "jwt 토큰") @RequestParam("token") String token) {
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.message("jwt 토큰 유효성 검증")
				.resultData(jwtTokenProvider.validateToken(token))
				.build();
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("issue")
	@Operation(summary = "jwt 토큰 재발급", description = "refresh token을 통한 jwt 재발급 API")
	public ResponseEntity<ResultDto<Object>> issue(@Parameter(name = "token", description = "jwt 재발급을 위한 토큰") @RequestParam("token") String token) {
		ResultDto<Object> result;
		
		if(jwtTokenProvider.validateToken(token)) {
			String sub = jwtTokenProvider.parseClaims(token).getSubject();
			Member member = memberService.findByUserIdAndIsLeave(sub, false);
			
			Collection<? extends GrantedAuthority> authorities = member.getAuthority().stream()
					.collect(Collectors.toList());
			UserDetails principal = new User(sub, "", authorities);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
			
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("jwt 토큰이 재발급 되었습니다.")
					.resultData(jwtTokenProvider.generateToken(authentication))
					.build();
		} else {
			result = ResultDto.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.message("jwt 토큰이 올바르지 않습니다.")
					.build();
		}
		
		return ResponseEntity.status(result.getStatus()).body(result);
	}

}
