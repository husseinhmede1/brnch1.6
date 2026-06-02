package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mdsl.model.dto.request.DeleteNonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.service.NonActivityPackageDetailsService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import lombok.RequiredArgsConstructor;

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
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNonActivityPackageDetails(@PathVariable("id") int id,HttpServletRequest request) {

	    DeleteNonActivityPackageDetailsRequestDto dto =new DeleteNonActivityPackageDetailsRequestDto();
	    dto.setId(id);
	    dto.setInstId(request.getHeader("instId"));
	    dto.setRemoteAddress(request.getRemoteAddr());

	    try {
	        return ResponseEntity.ok(
	                nonActivityPackageDetailsService.deleteNonActivityPackageDetails(dto)
	        );

	    } catch (BusinessException e) {
	        logger.error("@NonActivityPackageDetailsController#deleteNonActivityPackageDetails -BusinessException- {}", e.toString());
	        throw new BusinessException(e.getMessage(), e.getHttpStatus());

	    } catch (Exception ex) {
	        logger.error("@NonActivityPackageDetailsController#deleteNonActivityPackageDetails -generic exception- {}", ex.toString());
	        throw new BusinessException(
	                ResponseCode.VAL_ERROR_OCCURRED,
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}

	@PostMapping("/status-change")
	public void changeNonActivityPackageDetailsStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDto,BindingResult bindingResult) {
			Validations.validate(bindingResult);
		nonActivityPackageDetailsService.changeStatus(changeStatusRequestDto);
	}
}
