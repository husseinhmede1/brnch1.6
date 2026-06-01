package com.mdsl.exceptionHandling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.mdsl.config.RequestFilter;
import com.mdsl.framework.DatabaseMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mdsl.model.dto.response.ErrorDto;
import com.mdsl.service.ActivityPackageDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private final DatabaseMessageSource databaseMessageSource;
	private List<String> msgs = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDto> handleValidationException(ValidationException validationException, Locale locale) {
		msgs = databaseMessageSource.getMessagesFromKeys(validationException.getErrors(), locale, validationException.getValidateContactSource());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder().errors(msgs).build());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorDto> handleBusinessException(BusinessException businessException, Locale locale) {
		msgs = databaseMessageSource.getMessageFromKey(businessException.getMessage(), locale, true);
		return ResponseEntity.status(businessException.getHttpStatus()).body(ErrorDto.builder().errors(msgs).build());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleException(Exception exception) {
		logger.error("An Error Occurred", exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.builder().errors(Collections.singletonList(exception.getMessage())).build());
	}
}