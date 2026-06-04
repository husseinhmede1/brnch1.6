package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.IssuerProfileRequestDto;
import com.mdsl.model.dto.response.IssuerProfileResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.IssuerProfile;
import com.mdsl.model.entity.IssuerRelation;
import com.mdsl.model.mapper.IssuerProfileMapper;
import com.mdsl.repository.ActivityPackageDetailRepository;
import com.mdsl.repository.InstitutionAccountsRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.IssuerProfileRepository;
import com.mdsl.repository.IssuerRelationRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssuerProfileService {
	
	private final IssuerProfileRepository issuerProfileRepository;
	private final InstitutionRepository institutionRepository;
	private final IssuerRelationRepository issuerRelationRepository;
	private final ActivityPackageDetailRepository activityPackageDetailRepository;
	private final InstitutionAccountsRepository institutionAccountsRepository;
	
	private final IssuerProfileMapper issuerProfileMapper;
	private final MakerCheckerEngine makerCheckerEngine;

	public List<IssuerProfileResponseDto> getAllIssuerProfiles() {
		List<IssuerProfileResponseDto> issuerProfileResponseDto = new ArrayList<IssuerProfileResponseDto>();
		List<IssuerProfile> allIssuerProfiles = issuerProfileRepository.findAll(Sort.by(Sort.Direction.ASC, "profileId"));
		allIssuerProfiles.stream().forEach((issuerProfile) -> {
			IssuerProfileResponseDto issuerProfileDto = issuerProfileMapper.toDto(issuerProfile);
			issuerProfileResponseDto.add(issuerProfileDto);
		});
		
		return issuerProfileResponseDto;
	}
	
	public IssuerProfileResponseDto getIssuerProfileById(int id) {
		IssuerProfile issuerProfile = issuerProfileRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_ACQ_PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND));
		IssuerProfileResponseDto issuerProfileDto = issuerProfileMapper.toDto(issuerProfile);
		return issuerProfileDto;
	}
	
	public IssuerProfileResponseDto getIssuerProfileByIssuerAcqProfile(String issuerAcqProfile) {
		IssuerProfile issuerProfile = issuerProfileRepository.findByIssuerAcqProfile(issuerAcqProfile.trim().toUpperCase()).orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_ACQ_PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND));
		IssuerProfileResponseDto issuerProfileDto = issuerProfileMapper.toDto(issuerProfile);
		return issuerProfileDto;
	}
	
	public List<IssuerProfileResponseDto> getIssuerProfilesByInstitutionId(String institutionId) {
		List<IssuerProfileResponseDto> issuerProfileResponseDtos = new ArrayList<IssuerProfileResponseDto>();
		Institution institution = institutionRepository.findById(institutionId.trim()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<IssuerProfile> issuerProfiles = issuerProfileRepository.findByInstitutionId(institution.getInstitutionId());
		issuerProfiles.stream().forEach((issuerProfile) -> {
			IssuerProfileResponseDto issuerProfileDto = issuerProfileMapper.toDto(issuerProfile);
			issuerProfileResponseDtos.add(issuerProfileDto);
		});
		
		return issuerProfileResponseDtos;
	}
	
	@Transactional
	public IssuerProfileResponseDto saveIssuerProfile(IssuerProfileRequestDto issuerProfileRequestDto) {
		IssuerProfile saveIssuerProfile;
		IssuerProfile savedIssuerProfile;
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<IssuerProfile> requestIssuerProfile = issuerProfileRepository.findById(issuerProfileRequestDto.getProfileId());
		
		if(institutionRepository.findById(issuerProfileRequestDto.getInstitutionId().trim()).isEmpty()) {
			throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		
		if (makerCheckerEngine.processIfRequired(issuerProfileRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		if (requestIssuerProfile.isPresent()) {//Case of update

			if(issuerProfileRepository.existsByIssuerAcqProfileAndInstitutionIdAndProfileIdNot(issuerProfileRequestDto.getIssuerAcqProfile().toUpperCase().trim(), issuerProfileRequestDto.getInstitutionId(), issuerProfileRequestDto.getProfileId())) {
				throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
			
			saveIssuerProfile = this.issuerProfileMapper.toEntity(issuerProfileRequestDto);
			saveIssuerProfile.setIssuerAcqProfile(issuerProfileRequestDto.getIssuerAcqProfile().toUpperCase().trim());
			saveIssuerProfile.setCreatedBy(requestIssuerProfile.get().getCreatedBy());
			saveIssuerProfile.setCreatedDate(requestIssuerProfile.get().getCreatedDate());
			saveIssuerProfile.setUpdatedBy(Integer.valueOf(userDetails.getId()));
			saveIssuerProfile.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			savedIssuerProfile = this.issuerProfileRepository.save(saveIssuerProfile);
		} else { //Case of create
			
			if(issuerProfileRepository.existsByIssuerAcqProfileAndInstitutionId(issuerProfileRequestDto.getIssuerAcqProfile().toUpperCase().trim(), issuerProfileRequestDto.getInstitutionId())) {
				throw new BusinessException(ResponseCode.ISS_ISSUER_PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
			
			saveIssuerProfile = this.issuerProfileMapper.toEntity(issuerProfileRequestDto);
			saveIssuerProfile.setIssuerAcqProfile(issuerProfileRequestDto.getIssuerAcqProfile().toUpperCase().trim());
			saveIssuerProfile.setCreatedBy(Integer.valueOf(userDetails.getId()));
			saveIssuerProfile.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			savedIssuerProfile = this.issuerProfileRepository.save(saveIssuerProfile);
		}
		IssuerProfileResponseDto issuerProfileDto = issuerProfileMapper.toDto(savedIssuerProfile);
		
		return issuerProfileDto;
	}
	
	public void deleteIssuerProfile(int id) {
		IssuerProfile issuerProfile = issuerProfileRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_ACQ_PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND));
		Optional<Institution> institution=institutionRepository.findById(issuerProfile.getInstitutionId());
		if (activityPackageDetailRepository.existsByIssuerAcqProfileEntityAndInstitution(issuerProfile,institution.get())) {
			throw new BusinessException (ResponseCode.ISS_PRF_USED_ACQ_PKG, HttpStatus.BAD_REQUEST);
		}
		if(this.institutionAccountsRepository.existsByIssuerAcqProfileAndInstitutionId(issuerProfile.getIssuerAcqProfile(),institution.get().getInstitutionId())){
			throw new BusinessException (ResponseCode.ISS_PRF_USED_INST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		List<IssuerRelation> issuerRelations = this.issuerRelationRepository.findByIssuerAcqProfileAndInstitutionId(issuerProfile.getIssuerAcqProfile(),institution.get().getInstitutionId());
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		for(IssuerRelation issuerRelation : issuerRelations) {
			this.issuerRelationRepository.delete(issuerRelation);
		}
		this.issuerProfileRepository.delete(issuerProfile);
	}
}
