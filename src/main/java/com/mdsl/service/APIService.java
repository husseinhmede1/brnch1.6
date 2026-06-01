package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ApiListRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.dto.response.*;
import com.mdsl.model.entity.Api;
import com.mdsl.model.mapper.ApiMapper;
import com.mdsl.model.mapper.ObjectMapper;
import com.mdsl.model.objects.ObjectAndScope;
import com.mdsl.repository.ApiRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.AllowStpEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIService {
	private static final Logger logger = LoggerFactory.getLogger(APIService.class);

	private final CommonService commonService;
	private final ApiRepository apiRepository;
	private final ApiMapper apiMapper;
	private final ObjectMapper objectMapper;

	/*
	 * Returns the list of objects from table MD_BKD_API_LIST
	 */
	public List<ObjectResponseDto> getApiObjects (int instId){
		List<ObjectResponseDto> listOfObjects = new ArrayList<ObjectResponseDto>();
		List<ObjectAndScope> objects = apiRepository.findApiObjects(instId);

		objects.forEach(object->{
			listOfObjects.add(objectMapper.toDto(object));
		});
		
		Validations.isEmpty(listOfObjects);
		return listOfObjects;
	}
	
	/*
	 * Returns the list of apis and their details from table MD_BKD_API_LIST based on the object
	 */
	public PageableApiResponseDto getApi (String object, int instId, PaginationRequestDto paginationRequestDto){
		Pageable pageable = PageRequest.of(paginationRequestDto.getOffset(), paginationRequestDto.getPageSize(), paginationRequestDto.getAsc().trim().equalsIgnoreCase("true") ? Sort.by(paginationRequestDto.getSortBy()).ascending(): Sort.by(paginationRequestDto.getSortBy()).descending());
		List<ApiResponseDto> listOfApis = new ArrayList<ApiResponseDto>();
		Page<Api> apis = apiRepository.findByInstIdAndAllowStpAndApiDescIgnoreCase(instId, AllowStpEnum.ALLOW_STP.getValue(), pageable, object.trim());
		PageableApiResponseDto pageableApiResponseDto = new PageableApiResponseDto();
		apis.forEach((api)->{
			listOfApis.add(apiMapper.toApi(api));
		});
		pageableApiResponseDto.setApiResponseDto(listOfApis);
		pageableApiResponseDto.setPaginationResponseDto(this.commonService.getPaginationResponseDto(paginationRequestDto, apis));
		Validations.isEmpty(listOfApis);
		return pageableApiResponseDto;
	}
	
	/*
	 * Returns the list of apis and their details from table MD_BKD_API_LIST
	 */
	public PageableApiResponseDto getAllApis (int instId, PaginationRequestDto paginationRequestDto){
		Pageable pageable = PageRequest.of(paginationRequestDto.getOffset(), paginationRequestDto.getPageSize(), paginationRequestDto.getAsc().trim().equalsIgnoreCase("true") ? Sort.by(paginationRequestDto.getSortBy()).ascending(): Sort.by(paginationRequestDto.getSortBy()).descending());
		List<ApiResponseDto> listOfApis = new ArrayList<ApiResponseDto>();
		Page<Api> apis = apiRepository.findByInstIdAndAllowStp(instId, AllowStpEnum.ALLOW_STP.getValue(), pageable);
		PageableApiResponseDto pageableApiResponseDto = new PageableApiResponseDto();
		apis.forEach((api)->{
			listOfApis.add(apiMapper.toApi(api));
		});
		pageableApiResponseDto.setApiResponseDto(listOfApis);
		pageableApiResponseDto.setPaginationResponseDto(this.commonService.getPaginationResponseDto(paginationRequestDto, apis));
		Validations.isEmpty(listOfApis);
		return pageableApiResponseDto;
	}	
	
	/*
	 * Updates the API's STP flag based on the API id
	 */
	public void updateStpFlag(List<ApiListRequestDto> apiListRequestDtos, int instId) {
		UserDetailsImpl userDetails = commonService.getLoggedInUser();
		Validations.isEmpty(apiListRequestDtos);

		apiListRequestDtos.forEach((apiDto) -> {
			Api api = apiRepository.findByInstIdAndApiId(instId, apiDto.getApiId()).orElseThrow(()->new BusinessException(ResponseCode.CFG_INVALID_API_LIST, HttpStatus.NOT_FOUND));

			if(api.getAllowStp().toString().trim().equalsIgnoreCase(AllowStpEnum.NOT_ALLOW_STP.getValue())) {
				throw new BusinessException(ResponseCode.CFG_INVALID_API_LIST_STP,HttpStatus.BAD_REQUEST);
			}
			apiRepository.updateApiStpFlag(api.getApiId(), apiDto.getStp(), userDetails.getId());
		});
	}
}