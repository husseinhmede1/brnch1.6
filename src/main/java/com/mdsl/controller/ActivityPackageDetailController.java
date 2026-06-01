package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import com.mdsl.model.dto.request.ActivityPackageDetailRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.ActivityPackageDetailResponseDto;
import com.mdsl.service.ActivityPackageDetailService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(value = "Activity fee Package Defination Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/activityfeepackagedefination")
@RequiredArgsConstructor
public class ActivityPackageDetailController {
	private final ActivityPackageDetailService activityPackageDetailService;
	private static final Logger logger = LoggerFactory.getLogger(ActivityPackageDetailController.class);

	@ApiOperation(value = "Get all Activity Fee Packages",response = ActivityPackageDetailResponseDto.class)
	@GetMapping("/activitypackage/{id}/{instId}")
	public ResponseEntity<List<ActivityPackageDetailResponseDto>> getAllActivityFeePackages(@PathVariable(value = "id") String packageId,@PathVariable(value = "instId") String instId,HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(activityPackageDetailService.getAllActivityPackageDetailByPkgId(packageId,instId));
	}

	@ApiOperation(value = "Get Activity Fee Package by Id",response = ActivityPackageDetailResponseDto.class)
	@GetMapping("/packagedetailid/{id}")
	public ResponseEntity<ActivityPackageDetailResponseDto> getActivityFeePackageById(@PathVariable(value = "id") int packageDetailId) {
		return ResponseEntity.ok(activityPackageDetailService.getActivityPackageDetailById(packageDetailId));
	}

	@ApiOperation(value = "Save Activity Fee Package",response = ActivityPackageDetailResponseDto.class)
	@PostMapping
	public ResponseEntity saveOrUpdateActivityFeePackage(@Valid @RequestBody ActivityPackageDetailRequestDto activityPackageDetailRequestDto,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		try {
		return ResponseEntity.ok(activityPackageDetailService.saveOrUpdateActivityPackageDetail(activityPackageDetailRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ActivityPackageDetailController#saveOrUpdateActivityFeePackage: " + e.getMessage());
		    throw new BusinessException (ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
	    }
	}

	@ApiOperation(value = "Delete Activity Fee Package",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteActivityFeePackage(@PathVariable(value = "id") int packageDetailId) {
		try {
			return ResponseEntity.ok(activityPackageDetailService.deleteActivationPackageDetail(packageDetailId));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@ActivityPackageDetailController#deleteActivityFeePackage: " + e.getMessage());
		    throw new BusinessException (ResponseCode.VAL_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
	    }
	}

	@ApiOperation(value = "Change Activity Fee Package Status")
	@PostMapping("/status-change")
	public void changeStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		activityPackageDetailService.changeStatus(changeStatusRequestDTO);
	}
}