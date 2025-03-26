package kr.co.company.common;

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
