package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validation;

import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.exceptionHandling.RestResponseEntityExceptionHandler;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.service.NonActivityPackageDetailsService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn = Exception.class)
@RequestMapping("/nonactivityfeepackagedetails")
@RequiredArgsConstructor
public class NonActivityPackageDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(NonActivityPackageDetailsController.class);

	private final NonActivityPackageDetailsService nonActivityPackageDetailsService;

	@GetMapping
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getAllNonActivityPackageDetails() {
		List<NonActivityPackageDetailsResponseDto> packages = nonActivityPackageDetailsService
				.getAllNonActivityPackageDetails();
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/package/{id}/{instId}")
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getAllNonActivityPackageDetailsByPackageId(
			@PathVariable("id") String id,@PathVariable("instId") String instId, HttpServletRequest request) {
		List<NonActivityPackageDetailsResponseDto> packages = nonActivityPackageDetailsService
				.getAllNonActivityPackageDetailsByPackageId(id,instId);
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/active-nonactivity-package-details")
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getActiveNonActivityPackageDetails() {
		List<NonActivityPackageDetailsResponseDto> packages = nonActivityPackageDetailsService
				.getActiveNonActivityPackageDetails();
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/institution/{id}")
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getNonActivityPackageDetailsByInstitutionId(
			@PathVariable("id") String id) {
		return ResponseEntity.ok(nonActivityPackageDetailsService.getNonActivityPackageDetailsByInstitutionId(id));
	}

	@GetMapping("/{instid}/{packageid}")
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getAllNonActivityPackageDetailsByPackageIdAndInstitutionId(
			@PathVariable("instid") String instid, @PathVariable("packageid") String packageid) {
		List<NonActivityPackageDetailsResponseDto> packages = nonActivityPackageDetailsService
				.getAllNonActivityPackageDetailsByPackageIdAndInstitutionId(instid, packageid);
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/active/{instid}/{packageid}")
	public ResponseEntity<List<NonActivityPackageDetailsResponseDto>> getAllActiveNonActivityPackageDetailsByPackageIdAndInstitutionId(
			@PathVariable("instid") String instid, @PathVariable("packageid") String packageid) {
		List<NonActivityPackageDetailsResponseDto> packages = nonActivityPackageDetailsService
				.getAllActiveNonActivityPackageDetailsByPackageIdAndInstitutionId(instid, packageid);
		return ResponseEntity.ok(packages);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NonActivityPackageDetailsResponseDto> getNonActivityPackageDetailsById(
			@PathVariable("id") int id) {
		NonActivityPackageDetailsResponseDto nonActivityPackage = nonActivityPackageDetailsService
				.getNonActivityPackageDetailsById(id);
		return ResponseEntity.ok(nonActivityPackage);
	}

	@PostMapping
	public ResponseEntity saveOrUpdateNonActivityPackageDetails(@Valid @RequestBody NonActivityPackageDetailsRequestDto nonActivityPackageRequestDto, BindingResult bindingResult){
		Validations.validate(bindingResult);
        try {
			return ResponseEntity.ok(
					nonActivityPackageDetailsService.saveOrUpdateNonActivityPackageDetails(nonActivityPackageRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@NonActivityPackageDetailsController#saveOrUpdateNonActivityPackageDetails: " + e.getMessage());
		    throw new BusinessException (ResponseCode.CFG_NON_ACT_PKG_DETAILS_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
//		}catch(HttpMessageNotReadableException e) {
//			e.printStackTrace();
//		}catch(Exception e) {
//			String exceptionResult=e.getLocalizedMessage();
//			String result1[]= exceptionResult.split("is");
//			String result2[]=new String[30];
//			boolean indexCheckingOfresult1= NonActivityPackageDetailsService.isValidIndex(result1,1);
//			if(indexCheckingOfresult1) {
//				result2=result1[1].trim().split(":");
//				boolean indexCheckingOfresult2= NonActivityPackageDetailsService.isValidIndex(result2,0);
//				if(indexCheckingOfresult2) {
//					if(result2[0].equals("org.hibernate.exception.DataException")) {
//						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("value larger than specified precision allowed for column"); 
//					}
//				}
//			}
//			
//			//DataException
//			System.out.println(e.getCause());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
//		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNonActivityPackageDetails(@PathVariable("id") int id, HttpServletRequest request) {

		try {
			return ResponseEntity.ok(nonActivityPackageDetailsService.deleteNonActivityPackageDetails(id, request.getHeader("instId")));
		} catch (BusinessException e) {
			logger.error("@NonActivityPackageDetailsController#deleteNonActivityPackageDetails -BusinessException- "+e.toString());
			throw new BusinessException(e.getMessage(),e.getHttpStatus());
		} catch(Exception ex){
			logger.error("@NonActivityPackageDetailsController#deleteNonActivityPackageDetails -generic exception- "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/status-change")
	public void changeNonActivityPackageDetailsStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult) {
			Validations.validate(bindingResult);
		nonActivityPackageDetailsService.changeStatus(changeStatusRequestDto);
	}
}
