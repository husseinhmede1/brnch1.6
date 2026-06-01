package com.mdsl.exceptionHandling;

import org.springframework.http.HttpStatus;

import com.mdsl.utils.ResponseCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -3938869422445705593L;
	
	private ResponseCode errorCode;
	private HttpStatus httpStatus; 
	
	public BusinessException(ResponseCode errorCode) {
		super("BusinessException: " + errorCode);
		this.errorCode = errorCode;
	}
	public BusinessException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus; 
	}
}