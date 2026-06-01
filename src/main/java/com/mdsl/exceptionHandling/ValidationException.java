package com.mdsl.exceptionHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private List<String> errors;
	private Boolean validateContactSource = false;

	public void addError(String error) {
		this.errors.add(error);
	}

	public ValidationException() {
		super();
	}

	public ValidationException(BindingResult bindingResult) {
		super("Validations failed");
		List<String> tempList = new ArrayList<String>();

		for(FieldError fieldError: bindingResult.getFieldErrors()) {
			if(fieldError.getField().trim().contains("cardContactInfo") || fieldError.getField().trim().contains("cardInfo")) {
				validateContactSource = true;
				if(!tempList.contains(fieldError.getDefaultMessage()+",[CA]")) {
					tempList.add(fieldError.getDefaultMessage()+",[CA]");
				}
			} else if(fieldError.getField().trim().contains("customerContactInfo") || fieldError.getField().trim().contains("customerInfo")) {
				validateContactSource = true;
				if(!tempList.contains(fieldError.getDefaultMessage()+",[CU]")) {
					tempList.add(fieldError.getDefaultMessage()+",[CU]");
				}
			} else {
				tempList.add(fieldError.getDefaultMessage());
			}
		}
		this.errors = tempList;
	}

	public ValidationException(String message) {
		super(message);
	}
}