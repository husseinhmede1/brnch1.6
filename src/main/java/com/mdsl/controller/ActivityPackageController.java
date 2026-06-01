package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
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
import com.mdsl.model.dto.request.ActivityEntityMappingRequestDto;
import com.mdsl.model.dto.request.ActivityPackageRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.ActivityPackageResponseDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.service.ActivityPackageService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Activity fee Package Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/activityfeepackage")
@RequiredArgsConstructor
public class ActivityPackageController {
	private final ActivityPackageService activityPackageService;

	@ApiOperation(value = "Get all Activity Fee Packages",response = ActivityPackageResponseDto.class)
	@GetMapping
	public ResponseEntity<List<ActivityPackageResponseDto>> getAllActivityFeePackages() {
		return ResponseEntity.ok(activityPackageService.getAllActivityPackage());
	}

	@ApiOperation(value = "Get all Activity Fee Packages by Institution Id",response = ActivityPackageResponseDto.class)
	@GetMapping("/institution/{id}")
	public ResponseEntity<List<ActivityPackageResponseDto>> getByInstitutionId(@PathVariable(value = "id") String packageId) {
		return ResponseEntity.ok(activityPackageService.findActivityPackageByInstitutionId(packageId));
	}

	@ApiOperation(value = "Get Activity Fee Package by Id",response = ActivityPackageResponseDto.class)
	@GetMapping("/{id}")
	public ResponseEntity<ActivityPackageResponseDto> getActivityFeePackageById(@PathVariable(value = "id") Integer id) {
		return ResponseEntity.ok(activityPackageService.getActivityPackageById(id));
	}

	@ApiOperation(value = "Save Activity Fee Package",response = ActivityPackageResponseDto.class)
	@PostMapping
	public ResponseEntity saveOrUpdateActivityFeePackage(@Valid @RequestBody ActivityPackageRequestDto activityPackageRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(activityPackageService.saveOrUpdateActivityPackage(activityPackageRequestDto));
		} catch (BusinessException e) {
			if (e.getMessage().equals("ACT-1000")) {
				throw new BusinessException(ResponseCode.ACT_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.BAD_REQUEST);
			} else if (e.getMessage().equals("ACT-PKG-001")) {
				System.out.println("Act Id invalid");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Id can't be 0 or Empty");
			} else if(e.getMessage().equals("ACT-PKG-006")) {
				System.out.println("Package Name Invalid");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Package Name can't Empty");
			} else if (e.getMessage().equals("INT-001")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid institution Id");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some unexpected error occured");
			}
		}
	}

	@ApiOperation(value = "Delete Activity Fee Package",response = String.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteActivityFeePackage(@PathVariable(value = "id") Integer recordSeqId) {

		try {
			activityPackageService.deleteActivityPackage(recordSeqId);
			String message = "An item is deleted with id : " + recordSeqId;
			return ResponseEntity.ok(message);
		} catch (DataIntegrityViolationException e) {
			System.out.println("reference exists");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataIntegrityViolationException");
		} catch (ConstraintViolationException e) {
			System.out.println("reference exists");
		} catch(BusinessException e) {
			if (e.getMessage().equals("ACT-PKG-001")) {
				System.out.println("Invalid Activity Package Id");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid Activity Package Id");
			} 
		}
		
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("reference exists..");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataIntegrityViolationException");
		}
		return null;
	}

	@ApiOperation(value = "Change Activity Fee Package Status")
	@PostMapping("/status-change")
	public ResponseEntity<String> changeStatus(@Valid @RequestBody ChangeStatusRequestDto changeStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(activityPackageService.changeStatus(changeStatusRequestDTO));
	}

	@ApiOperation(value = "Get all Active Activity Fee Packages by Institution Id",response = ActivityPackageResponseDto.class)
	@GetMapping("/active-activitypackage/{id}")
	public ResponseEntity<List<ActivityPackageResponseDto>> getActiveActivityPackageByInstitutionId(
			@PathVariable(value = "id") String id) {
		return ResponseEntity.ok(activityPackageService.getActiveActivityPackageByInstitutionId(id));
	}

	@ApiOperation(value = "Get Mapped Entities by Activity Package Id",response = EntitiesResponseDto.class)
	@GetMapping("/mapped-entities/{id}/{instId}")
	public ResponseEntity<List<EntitiesResponseDto>> getMappedEntitiesByActivityPackageId(
			@PathVariable(value = "id") String id,@PathVariable(value = "instId") String instId, HttpServletRequest request) {
		return ResponseEntity.ok(activityPackageService.getMappedEntitiesByActivityPackageId(id, instId));
	}

	@ApiOperation(value = "Map Entity to Activity Package",response = ActivityEntityMappingRequestDto.class)
	@PostMapping("/assignment")
	public ResponseEntity<@Valid ActivityEntityMappingRequestDto> mapPackageWithEntity(
			@Valid @RequestBody ActivityEntityMappingRequestDto requestDto, BindingResult bindingResult,HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(activityPackageService.mapPackageWithEntity(requestDto,requestDto.getInstId()));
	}
}
