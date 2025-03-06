package kr.co.company.check;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.company.common.dto.ResultDto;

@RequestMapping("check")
@RestController
public class CheckController {
	
	@GetMapping("health")
	public ResponseEntity<ResultDto<String>> health() {
		ResultDto<String> result = new ResultDto<>();
		result.setStatus(HttpStatus.OK);
		result.setResultData("Health Check Complete!");
		
		return ResponseEntity.ok(result);
	}

}
