package kr.co.company.vacation;

import java.util.Date;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class VacationDto {
	
	@Parameter(description = "휴가 시작 날짜", example = "2025-06-01", required = true)
	private String startDate;
	@Parameter(description = "휴가 종료 날짜", example = "2025-06-03", required = true)
	private String endDate;
	
}
