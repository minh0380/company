package kr.co.company.member;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.company.common.dto.ResultDto;
import kr.co.company.jwt.JwtTokenDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "2. 회원", description = "회원 관리 관련 API")
@RequestMapping("/api/member")
@RestController
public class MemberController {
	
	private final MemberService memberService;
	private final MemberValidator memberValidator;
	
	public MemberController(MemberService memberService, MemberValidator memberValidator) {
		this.memberService = memberService;
		this.memberValidator = memberValidator;
	}
	
	// @InitBinder: 특정 컨트롤러에서 validator를 사용하기 위해 지정. value 속성에 지정한 요청 파라미터명 또는 moddlAttribute명에 대해서만 validator가 동작한다. 
	@InitBinder(value = "member")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(memberValidator);
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
	// @Valid 어노테이션이 지정된 파라미터 바로 뒤에 Errors를 위치시켜야 한다.
	public ResponseEntity<ResultDto<Object>> join(@ParameterObject @Valid Member member, Errors errors) {
		ResultDto<Object> result;
		
		if(errors.hasErrors()) {
			result = ResultDto.builder()
					.status(HttpStatus.BAD_REQUEST)
					.message(errors.getFieldError().getDefaultMessage())
					.resultData(errors.getAllErrors())
					.build();
		} else {
			memberService.saveMember(member);
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("회원가입이 완료되었습니다.")
					.build();
		}
		
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
	@Operation(summary = "유저 정보 확인", description = "유저 정보 확인 API")
	public ResponseEntity<ResultDto<Object>> isUser(@Parameter(name = "userId", description = "아이디", example = "mhcho") @RequestParam("userId") String userId) {
		Member member = memberService.findByUserIdAndIsLeave(userId);
		ResultDto<Object> result;
		
		if(member != null) {
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("회원입니다.")
					.resultData(member)
					.build();
		}else {
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("회원이 아닙니다.")
					.build();
		}
		
		return ResponseEntity.ok(result);
	}

}
