package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.exceptionHandling.RestResponseEntityExceptionHandler;
import com.mdsl.model.dto.request.ActivityPackageTierRequestDto;
import com.mdsl.model.dto.response.ActivityPackageDetailResponseDto;
import com.mdsl.model.dto.response.ActivityPackageTierResponseDto;
import com.mdsl.service.ActivityPackageDetailService;
import com.mdsl.service.ActivityPackageTierService;
import com.mdsl.utils.ResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Activity fee Package Charge Detail Controller")
@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/activityfeepackagechargedetail")
public class ActivityPackageTierController {

	private ActivityPackageTierService activityPackageTierService;
	private static final Logger logger = LoggerFactory.getLogger(ActivityPackageTierController.class);
	@Autowired
	private ActivityPackageDetailService activityPackageDetailService;

	ActivityPackageTierController(ActivityPackageTierService activityPackageTierService) {
		this.activityPackageTierService = activityPackageTierService;
	}

	@ApiOperation(value = "Get all Activity Fee Package Tiers",response = ActivityPackageTierResponseDto.class)
	@GetMapping("/packagedetail/{id}")
	public ResponseEntity<List<ActivityPackageTierResponseDto>> getAllActivityFeePackages(@PathVariable(value = "id") int packageDetailId) {
		return ResponseEntity.ok(activityPackageTierService.getAllActivationPackageTierByPkgDetailId(packageDetailId));
	}

	@ApiOperation(value = "Get Activity Fee Package Tier by Id",response = ActivityPackageTierResponseDto.class)
	@GetMapping("/packagetier/{id}")
	public ResponseEntity<ActivityPackageTierResponseDto> getActivityFeePackageById(@PathVariable(value = "id") int packageDetailId) {
		return ResponseEntity.ok(activityPackageTierService.getActivityPackageTierById(packageDetailId));
	}

	@ApiOperation(value = "Save Activity Fee Package Tier",response = ActivityPackageTierResponseDto.class)
	@PostMapping
	public ResponseEntity saveOrUpdateActivityFeePackage(@Valid @RequestBody ActivityPackageTierRequestDto activityPackageTierRequestDto, BindingResult bindingResult)  {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(activityPackageTierService.saveOrUpdateActivityPackageTier(activityPackageTierRequestDto));
		}catch(HttpMessageNotReadableException e) {
			e.printStackTrace();
		}

		catch(Exception e) {
			String exceptionResult=e.getLocalizedMessage();
			String result1[]= exceptionResult.split("is");
			String result2[]=new String[30];
			boolean indexCheckingOfresult1=activityPackageDetailService.isValidIndex(result1,1);
			if(indexCheckingOfresult1) {
				result2=result1[1].trim().split(":");
				boolean indexCheckingOfresult2=activityPackageDetailService.isValidIndex(result2,0);
				if(indexCheckingOfresult2) {
					if(result2[0].equals("org.hibernate.exception.DataException")) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("value larger than specified precision allowed for column"); 
					}
				}
			}
			System.out.println(e.getCause());
		}
		return null;
	}

	@ApiOperation(value = "Delete Activity Fee Package Tier",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteActivityFeePackage(@PathVariable(value = "id") int packageTierId) {
		try {
			return ResponseEntity.ok(activityPackageTierService.deleteActivationPackageTier(packageTierId));
		} catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	    } catch (Exception e) {
		logger.error("@ActivityPackageTierController#deleteActivityFeePackage: " + e.getMessage());
		throw new BusinessException (ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_NO_SAVE, HttpStatus.BAD_REQUEST);
	   }
	}
}
