package kr.co.company.check;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.company.common.dto.ResultDto;

@Tag(name = "1. 헬스 체크", description = "서버 헬스 체크")
@RequestMapping("check")
@RestController
public class CheckController {
	
	@GetMapping("health")
	public ResponseEntity<ResultDto<Object>> health() {
		ResultDto<Object> result = ResultDto.builder()
				.status(HttpStatus.OK)
				.resultData("Health Check Complete!")
				.build();
		
		return ResponseEntity.ok(result);
	}

}
