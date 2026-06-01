package com.mdsl.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.utils.Validations;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.AcquiringTransactionRequestDto;
import com.mdsl.model.dto.request.RepresentmentRequestDto;
import com.mdsl.model.dto.response.AcquiringTransactionResponseDto;
import com.mdsl.model.dto.response.RepresentmentResponseDto;
import com.mdsl.repository.RepresentmentRepository;
import com.mdsl.service.RepresentmentService;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/representment")
@RequiredArgsConstructor
public class RePresentmentController {

	private final RepresentmentService representmentService;
	private static final Logger logger = LoggerFactory.getLogger(RePresentmentController.class);

	@PostMapping
	public ResponseEntity<RepresentmentResponseDto> saveOrUpdateRepresentment(@Valid @RequestBody RepresentmentRequestDto representmentRequestDto, HttpServletRequest httpServletRequest , BindingResult bindingResult)throws ParseException {
		Validations.validate(bindingResult);
		String institutionId = httpServletRequest.getHeader("institutionId");
		return ResponseEntity.ok(representmentService.saveOrUpdateRepresentment(representmentRequestDto));
	}

	@GetMapping
	public ResponseEntity viewRepresentment() {
		return ResponseEntity.ok(representmentService.viewRepresentment());
	}

	@GetMapping("/{representmentId}")
	public ResponseEntity getRepresentment(@PathVariable("representmentId") int representmentId) {
		return ResponseEntity.ok(representmentService.getRepresentment(representmentId));
	}

	@DeleteMapping("/{representmentId}")
	public ResponseEntity<String> deleteRepresentment(@PathVariable("representmentId") int representmentId) {

		try {
			representmentService.deleteRepresentment(representmentId);
			String message = "An item is deleted with id : " + representmentId;
			return ResponseEntity.ok(message);
		}catch(BusinessException ex) {
			logger.error("@RePresentmentController#deleteRepresentment-business exception "+ex.toString());
			return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
		} catch(Exception ex){
			logger.error("@RePresentmentController#deleteRepresentment-generic exception "+ex.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
		}
	}

}
