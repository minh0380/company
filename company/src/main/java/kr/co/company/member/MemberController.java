package kr.co.company.member;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.ResultDto;
import kr.co.company.jwt.JwtTokenDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "회원", description = "회원 관리 관련 API")
@RequestMapping("/api/member")
@RestController
public class MemberController {
	
	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("id-check")
	@Operation(summary = "아이디 검사", description = "아이디 중복 검사")
	public ResponseEntity<ResultDto<Object>> idCheck(@Parameter(name = "userId", description = "아이디", example = "mhcho") @RequestParam("userId") String userId) {
		boolean exist = memberService.existsByUserIdAndIsLeave(userId);
		String message = exist? "이미 존재하는 아이디 입니다.":"사용 가능한 아이디 입니다.";
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.message(message)
				.resultData(memberService.existsByUserIdAndIsLeave(userId))
				.build();
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("join")
	@Operation(summary = "회원가입", description = "회원가입 API")
	public ResponseEntity<ResultDto<Object>> join(@ParameterObject Member member) {
		memberService.saveMember(member);
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.message("회원가입이 완료되었습니다.")
				.build();
		
		return ResponseEntity.ok(result);
	};
	
	@PostMapping("sign-in")
	@Operation(summary = "로그인", description = "로그인 API")
	public JwtTokenDto signIn(@Parameter(name = "userId", description = "아이디", example = "mhcho") @RequestParam("userId") String userId
			, @Parameter(name = "password", description = "비밀번호", example = "mhchopassword") @RequestParam("password") String password) {
		JwtTokenDto jwtToken = memberService.signIn(userId, password);
		
		return jwtToken;
	}
	
	@GetMapping("is-user")
	@Operation(summary = "유저 권한 테스트", description = "유저 권한 테스트 API")
	public ResponseEntity<String> isUser() {
		return ResponseEntity.ok("회원입니다.");
	}

}
