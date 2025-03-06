package kr.co.company.common.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResultDto<T> {
	
	private HttpStatus status;
	private T resultData;
	
}
