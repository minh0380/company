package kr.co.company.member;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// spring이 관리하는 bean으로 등록
@Component
public class MemberValidator implements Validator {
	
	private final MemberRepository memberRepository;
	
	public MemberValidator(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	// 어떤 조건의 컨트롤러 메서드의 파라미터를 검증할 것 인지 작성
	@Override
	public boolean supports(Class<?> clazz) {
		// Class<?> clazz: 컨트롤러 파라미터의 class 객체
		return Member.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// Object target: 컨트롤러 메서드의 파라미터
		// Errors: 검증에 실패할 경우 내용을 저장할 Error 객체, 컨트롤러 메서드의 파라미터로 전달된다.
		Member member = (Member) target;
		
		if(memberRepository.existsByUserIdAndIsLeave(member.getUserId(), false)) {
			errors.rejectValue("userId", "error.userId", "이미 존재하는 아이디입니다.");
		}
	}

}
