package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageEntityMappingRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageRequestDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.dto.response.NonActivityPackageResponseDto;
import com.mdsl.service.NonActivityPackageService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/nonactivityfeepackage")
@RequiredArgsConstructor
public class NonActivityPackageController {

	private static final Logger logger = LoggerFactory.getLogger(NonActivityPackageController.class);
	private final NonActivityPackageService nonActivityPackageService;

	@GetMapping
	public ResponseEntity<List<NonActivityPackageResponseDto>> getAllNonActivityPackages() {
		List<NonActivityPackageResponseDto> packages = nonActivityPackageService.getAllNonActivityPackages();
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/active-nonactivity-packages/{id}")
	public ResponseEntity<List<NonActivityPackageResponseDto>> getActiveNonActivityPackagesByInstitutionId(
			@PathVariable("id") String id) {
		List<NonActivityPackageResponseDto> packages = nonActivityPackageService
				.getActiveNonActivityPackagesByInstitutionId(id);
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/institution/{id}")
	public ResponseEntity<List<NonActivityPackageResponseDto>> getNonActivityPackageByInstitutionId(
			@PathVariable("id") String id) {
		return ResponseEntity.ok(nonActivityPackageService.getNonActivityPackageByInstitutionId(id));
	}

	@GetMapping("/{id}/{instId}")
	public ResponseEntity<NonActivityPackageResponseDto> getNonActivityPackageById(@PathVariable("id") String id,@PathVariable("instId") String instId, HttpServletRequest request) {
		NonActivityPackageResponseDto nonActivityPackage = nonActivityPackageService.getNonActivityPackageById(id,instId);
		return ResponseEntity.ok(nonActivityPackage);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateNonActivityPackage(@Valid @RequestBody NonActivityPackageRequestDto nonActivityPackageRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(nonActivityPackageService.saveOrUpdateNonActivityPackage(nonActivityPackageRequestDto));
		} catch (BusinessException e) {
			logger.error("@NonActivityPackageController#saveOrUpdateNonActivityPackage -BusinessException- "+e.toString());
			throw new BusinessException(e.getMessage(),e.getHttpStatus());
		} catch(Exception ex){
			logger.error("@NonActivityPackageController#saveOrUpdateNonActivityPackage -generic exception- "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}/{instId}")
	public ResponseEntity<String> deleteNonActivityPackage(@PathVariable("id") String id,@PathVariable("instId") String instId, HttpServletRequest request) {
		try {
			nonActivityPackageService.deleteNonActivityPackage(id,instId);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@UserController#deleteNonActivityPackage "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@UserController#deleteNonActivityPackage "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeNonActivityPackageStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult,HttpServletRequest request) {
	Validations.validate(bindingResult);
	 try {
		return ResponseEntity.ok(nonActivityPackageService.changeStatus(changeStatusRequestDto));
	 } catch(BusinessException ex){
			logger.error("@UserController#changeNonActivityPackageStatus "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@UserController#changeNonActivityPackageStatus "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/mapped-entities/{id}")
	public ResponseEntity<List<EntitiesResponseDto>> getMappedEntitiesByNonActivityPackageId(
			@PathVariable(value = "id") String id, HttpServletRequest request) {
		return ResponseEntity.ok(nonActivityPackageService.getMappedEntitiesByNonActivityPackageId(id, request.getHeader("instId")));
	}

	@PostMapping("/assignment")
	public ResponseEntity<@Valid NonActivityPackageEntityMappingRequestDto> mapPackageWithEntity(
			@Valid @RequestBody NonActivityPackageEntityMappingRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(nonActivityPackageService.mapPackageWithEntity(requestDto, requestDto.getInstId()));
	}
}
