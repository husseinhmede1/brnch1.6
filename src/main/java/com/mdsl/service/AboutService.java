package com.mdsl.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//import com.mdsl.config.AppProperties;
import com.mdsl.exceptionHandling.BusinessException;
//import com.mdsl.model.dto.response.AppInfoResponseDto;
//import com.mdsl.model.dto.response.VersionResponseDto;
//import com.mdsl.model.dto.response.VersionRnResponseDto;
//import com.mdsl.model.entity.AppInfo;
//import com.mdsl.model.entity.AppRn;
//import com.mdsl.model.entity.AppVersion;
//import com.mdsl.model.mapper.AppVersionMapper;
//import com.mdsl.model.mapper.VersionMapper;
//import com.mdsl.model.mapper.VersionRnMapper;
//import com.mdsl.repository.AppInfoRepository;
//import com.mdsl.repository.AppRnRepository;
//import com.mdsl.repository.AppVersionRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AboutService {
	
//	private final AppInfoRepository appInfoRepository;
//	private final AppVersionRepository appVersionRepository;
//	private final AppRnRepository appRnRepository;
//	private final AppVersionMapper appVersionMapper;
//	private final VersionMapper versionMapper;
//	private final VersionRnMapper versionRnmapper;
//	private final AppProperties appProperties;
//	
//	public AppInfoResponseDto getAllAppVersionInfos() {
//		Set<VersionResponseDto> allAppVersionsDto = new HashSet<VersionResponseDto>(); 
//		List<AppVersion> allAppversions = appVersionRepository.findAll(Sort.by(Sort.Direction.ASC, "appVersionId"));
//		
//		allAppversions.stream().forEach((appVersion) -> {
//			VersionResponseDto versionResponseDto = versionMapper.toDto(appVersion);
//			allAppVersionsDto.add(versionResponseDto); 
//	    });
//		
//		AppInfo appInfo = appInfoRepository.findById(appProperties.getAppId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_NO_DATA_FOUND, HttpStatus.NOT_FOUND));
//		AppInfoResponseDto appInfoResponseDto = appVersionMapper.toDto(appInfo);
//		appInfoResponseDto.setAppVersions(allAppVersionsDto);
//		
//		Validations.isEmpty(allAppversions);
//		return appInfoResponseDto;
//	}
//	public List<VersionRnResponseDto> getVersionRn(int versionId) {
//		AppVersion appVersion = appVersionRepository.findById(versionId).orElseThrow(()->new BusinessException(ResponseCode.ABT_INVALID_VERSION, HttpStatus.NOT_FOUND)); 
//		
//		List<VersionRnResponseDto> allAppRnsDto = new ArrayList<VersionRnResponseDto>(); 
//		List<AppRn> allAppRns = appRnRepository.findByAppVersion(appVersion);
//		
//		allAppRns.stream().forEach((appRn) -> {
//			VersionRnResponseDto versionRnResponseDto = versionRnmapper.toDto(appRn);
//			versionRnResponseDto.setAppVersionId(appRn.getAppVersion().getAppVersionId());
//			versionRnResponseDto.setAppVersionDesc(appRn.getAppVersion().getVersionDesc());
//			allAppRnsDto.add(versionRnResponseDto); 
//	    });
//		Validations.isEmpty(allAppRns);
//		return allAppRnsDto;
//	}
}