package com.mdsl.controller;

import java.util.List;

import com.mdsl.service.AcquiringTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.ActivityApiResponseDto;
import com.mdsl.model.dto.response.ActivityResponseDto;
import com.mdsl.service.ActivityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Activity Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {

	private final ActivityService activityService;

	@ApiOperation(value = "Get All Activities",response = ActivityResponseDto.class)
	@GetMapping
	public ResponseEntity<List<ActivityResponseDto>> getAllActivities() {
		List<ActivityResponseDto> activities = activityService.getAllActivities();
		return ResponseEntity.ok(activities);
	}
	
	@ApiOperation(value = "Get Activity by Id",response = ActivityResponseDto.class)
	@GetMapping("/{id}")
	public ResponseEntity<ActivityResponseDto> getActivityById(@PathVariable("id") int id) {
		ActivityResponseDto activity = activityService.getActivityById(id);
		return ResponseEntity.ok(activity);
	}
	
	@ApiOperation(value = "Get Activity Api by Activity Id",response = ActivityApiResponseDto.class)
	@GetMapping("/activity/{id}")
	public ResponseEntity<List<ActivityApiResponseDto>> getActivityApiByActivityId(@PathVariable("id") int id) {
		List<ActivityApiResponseDto> activity = activityService.getActivityApiByActivityId(id);
		return ResponseEntity.ok(activity);
	}
}
