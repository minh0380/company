package kr.co.company.common.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto<T> {
	
	private HttpStatus status;
	private String message;
	private T resultData;
	
}
