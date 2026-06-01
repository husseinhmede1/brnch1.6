package com.mdsl.exceptionHandling;

import org.springframework.http.HttpStatus;

import com.mdsl.utils.ResponseCode;

import lombok.Getter;

@Getter
public class HaltValidationException extends RuntimeException {

	/***/
	private static final long serialVersionUID = 1L;
	
	private ResponseCode errorCode;
	private HttpStatus httpStatus; 
	
	public HaltValidationException(String message) {
		super(message);
	}
	public HaltValidationException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus; 
	}
}