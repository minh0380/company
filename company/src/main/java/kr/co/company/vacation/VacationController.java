package kr.co.company.vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.dto.ResultDto;
import kr.co.company.member.Member;
import kr.co.company.member.MemberService;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "5. 휴가 관리", description = "휴가 관리 관련 API")
@Slf4j
@RequestMapping("/api/vacation")
@RestController
public class VacationController {
	
	private final VacationService vacationService;
	private final MemberService memberService;
	
	public VacationController(VacationService vacationService, MemberService memberService) {
		this.vacationService = vacationService;
		this.memberService = memberService;
	}
	
	@PostMapping(value = "apply")
	@Operation(summary = "휴가 신청", description = "휴가 신청 API")
	public ResponseEntity<ResultDto<Object>> apply(@ParameterObject VacationDto vacationDto
			, @AuthenticationPrincipal UserDetails user) {
		Vacation vacation = new Vacation();
		Member member = memberService.findByUserIdAndIsLeave(user.getUsername(), false);
		ResultDto<Object> result;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		log.info("dto : {}", vacationDto);
		
		vacation.setMember(member);
		try {
			vacation.setStartDate(sdf.parse(vacationDto.getStartDate()));
			vacation.setEndDate(sdf.parse(vacationDto.getEndDate()));
			log.info("vacation : {}", vacation);
			
			vacationService.saveVacation(vacation);
			
			result = ResultDto.builder()
					.status(HttpStatus.OK)
					.message("휴가 신청이 완료되었습니다.")
					.resultData(vacation)
					.build();
		} catch (ParseException e) {
			result = ResultDto.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.message("휴가 신청 중 오류가 발생했습니다.")
					.resultData(vacationDto)
					.build();
		}
		
		return ResponseEntity.status(result.getStatus()).body(result);
	}

}
