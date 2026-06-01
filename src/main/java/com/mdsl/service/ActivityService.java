package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.ActivityApiResponseDto;
import com.mdsl.model.dto.response.ActivityResponseDto;
import com.mdsl.model.entity.Activity;
import com.mdsl.model.entity.ActivityApi;
import com.mdsl.model.mapper.ActivityApiMapper;
import com.mdsl.model.mapper.ActivityMapper;
import com.mdsl.repository.ActivityRepository;
import com.mdsl.repository.ActivityApiRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {
	private final ActivityRepository activityRepository;

	private final ActivityApiRepository activityApiRepository;

	private final ActivityMapper activityMapper;

	private final ActivityApiMapper activityApiMapper;

	public List<ActivityResponseDto> getAllActivities() {
		try {
			List<Activity> allActivities = activityRepository.findAll(Sort.by(Sort.Direction.ASC, "activityId"));

			return allActivities.stream()
					.map(activityMapper::toDto)
					.collect(Collectors.toList());

		} catch (Exception ex) {
			throw new BusinessException("Failed to fetch activities: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ActivityResponseDto getActivityById(int id) {
		Optional<Activity> activity = activityRepository.findById(id);
		Activity activity2 = activity
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACTIVITY_NOT_FOUND, HttpStatus.NOT_FOUND));
		return activityMapper.toDto(activity2);	}

	public List<ActivityApiResponseDto> getActivityApiByActivityId(int id) {
		
		Activity activity = activityRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACTIVITY_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		List<ActivityApiResponseDto> allActivityDto = new ArrayList<ActivityApiResponseDto>();
		List<ActivityApi> allActivities = activityApiRepository.findByActivity(activity,
				Sort.by(Sort.Direction.ASC, "activityApiId"));

		allActivities.stream().forEach((act) -> {
			ActivityApiResponseDto dto = activityApiMapper.toDto(act);
			allActivityDto.add(dto);
		});

		return allActivityDto;	}
}