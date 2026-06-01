package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.FileTypesResponseDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.framework.DatabaseMessageSource;
import com.mdsl.model.dto.request.BkdServiceRequestDto;
import com.mdsl.model.dto.response.BkdServiceResponseDto;
import com.mdsl.model.entity.BKDParameterService;
import com.mdsl.model.entity.BKDService;
import com.mdsl.model.mapper.BkdServiceMapper;
import com.mdsl.repository.BKDParameterRepository;
import com.mdsl.repository.BKDParameterServiceRepository;
import com.mdsl.repository.BKDServiceRepository;
import com.mdsl.utils.CommonUtils;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.LoggingCategoriesEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BkdServiceService {
	
	private final BKDServiceRepository bkdServiceRepository;
	private final BKDParameterRepository bkdParameterRepository;
	private final BKDParameterServiceRepository bkdParameterServiceRepository;

	private final BkdServiceMapper bkdServiceMapper;
	
	private final DatabaseMessageSource databaseMessageSource;
	private final CommonService commonService;
	private final UserService userService;
		
	/*
	 * Returns the list of all bkd services
	 */
	@Cacheable(key = "#root.methodName", cacheResolver="cacheResolver")
	public List<BkdServiceResponseDto> getAllBkdServices() {
		List<BkdServiceResponseDto> allBkdServicesDto = new ArrayList<BkdServiceResponseDto>();
		List<BKDService> allBkdServices = this.bkdServiceRepository.findAll();
		
		Validations.isEmpty(allBkdServices);
		allBkdServices.forEach((service)->{
			allBkdServicesDto.add(this.bkdServiceMapper.toDto(service));
		});
		
		return allBkdServicesDto;
	}
	
	/*
	 * Returns the list of all bkd services
	 */
	@Cacheable(key = "#parameterServiceId", cacheResolver="cacheResolver")
	public String findParameterName(int parameterServiceId) {
		BKDParameterService bkdParameterService = this.bkdParameterServiceRepository.findById(parameterServiceId).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_SERVICE_PARAMETER, HttpStatus.NOT_FOUND));
		String parameterName = this.bkdParameterRepository.findParameterName(bkdParameterService.getParametersServiceId());
		return parameterName;
	}
	
	/*
	 * Updates a bkd service's batch size based on the unique identifier
	 * (serviceId)
	 * The transactions are logged in table MD_ADT_BKD_LOG
	 */
	@CacheEvict
	public BkdServiceResponseDto updateBkdServiceBatchSize(BkdServiceRequestDto bkdServiceRequestDto, int instId, String remoteAddress) {

		UserDetailsImpl userDetails = commonService.getLoggedInUser();
		BKDService bkdService = this.bkdServiceRepository.findById(bkdServiceRequestDto.getServiceId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_SERVICE_ID, HttpStatus.NOT_FOUND));
		
		bkdServiceRepository.updateBatchSize(bkdService.getServiceId(), bkdServiceRequestDto.getBatchSize(), userDetails.getId());
		return bkdServiceMapper.toDto(bkdService);
	}

	public void startProcessExecution(BKDService bkdService, List<String> jobDefinitionTaskList, Locale locale) {
		if (CommonUtils.isBlank(bkdService.getServiceName())) {
			jobDefinitionTaskList.add(databaseMessageSource.getMessageFromKey(ResponseCode.CFG_INVALID_SERVICE_NAME,locale, true).get(0).toString());
		}
	}

	public FileTypesResponseDto getFileTypes() {
		List<String> fileTypes = bkdServiceRepository.getFileTypes();
		return new FileTypesResponseDto(fileTypes);
	}
}