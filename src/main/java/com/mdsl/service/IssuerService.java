package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import com.mdsl.repository.IssuerRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.IssuerRequestDto;
import com.mdsl.model.dto.response.IssuerRelationResponseDto;
import com.mdsl.model.dto.response.IssuerResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.IssuerProfile;
import com.mdsl.model.entity.IssuerRelation;
import com.mdsl.model.mapper.IssuerMapper;
import com.mdsl.model.mapper.IssuerRelationMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.IssuerRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

@Service
public class IssuerService {

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private IssuerRepository issuerRepository;

	@Autowired
	private IssuerMapper issuerMapper;

	@Autowired
	private IssueRelationService issueRelationService;

	@Autowired
	private IssuerRelationMapper issuerRelationMapper;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;
	@Autowired
	private IssuerRelationRepository issuerRelationRepository;

	public List<IssuerResponseDto> getAllIssuer() {
		List<IssuerProfile> issuer = issuerRepository.findAll(Sort.by(Sort.Direction.ASC, "issuerId"));
		List<IssuerResponseDto> allIssuerResponseDto = new ArrayList<IssuerResponseDto>();
		for (IssuerProfile temp : issuer) {
			IssuerResponseDto issuerResponseDto = issuerMapper.toDto(temp);
			List<IssuerRelationResponseDto> issuerRelation = issueRelationService
					.getAllIssuerRelationByIssuerId(issuerResponseDto.getIssuerId());
			issuerResponseDto.setIssuerRelation(issuerRelation);
			allIssuerResponseDto.add(issuerResponseDto);
		}
		return allIssuerResponseDto;
	}

	public List<IssuerResponseDto> getAllIssuerByIstitutionId(String instId) {
		List<IssuerProfile> issuer = issuerRepository.findAllByInstId(instId, Sort.by(Sort.Direction.ASC, "profileId"));
		List<IssuerResponseDto> allIssuerResponseDto = new ArrayList<IssuerResponseDto>();
		for (IssuerProfile temp : issuer) {
			IssuerResponseDto issuerResponseDto = issuerMapper.toDto(temp);
			List<IssuerRelationResponseDto> issuerRelation = issueRelationService
					.getAllIssuerRelationByIssuerId(issuerResponseDto.getIssuerId());
			issuerResponseDto.setIssuerRelation(issuerRelation);
			allIssuerResponseDto.add(issuerResponseDto);
		}
		return allIssuerResponseDto;
	}

	public IssuerResponseDto saveOrUpdateIssuer(IssuerRequestDto issuerRequestDto) {
		IssuerProfile issuer = null;
		IssuerRelation issuerRelation = null;
		IssuerRelationResponseDto issuerRelationResponseDto = null;
		IssuerResponseDto dto = null;
		Institution institution = institutionRepository.findById(issuerRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (issuerRequestDto.getIssuerId() != 0) {
			issuer = issuerRepository.findById(issuerRequestDto.getIssuerId())
					.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND));
			issuer.setInstitutionId(institution.getInstitutionId());
			issuer.setProfileId(issuerRequestDto.getProfile());
			issuer.setProfileDescription(issuerRequestDto.getDescription());
		} else {
			issuer = issuerMapper.toEntity(issuerRequestDto);
			issuer.setInstitutionId(institution.getInstitutionId());
		}
		if (issuerRequestDto.getIssuerRelation().getRecordSeqId() != 0) {
			issuerRelation = issuerRelationRepository.findById(issuerRequestDto.getIssuerRelation().getRecordSeqId())
					.orElseThrow(
							() -> new BusinessException(ResponseCode.ISS_ISSUER_RELATION_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		if (makerCheckerEngine.processIfRequired(issuerRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		List<IssuerRelationResponseDto> institutionRelationResponseDto = new ArrayList<>();
		issuerRelationResponseDto = issueRelationService.saveOrUpdateIssuerRelation(issuerRequestDto.getIssuerRelation(),
				institution.getInstitutionId(), issuerRelation);
		institutionRelationResponseDto.add(issuerRelationResponseDto);
		issuer = issuerRepository.save(issuer);
		dto = issuerMapper.toDto(issuer);
		dto.setIssuerRelation(institutionRelationResponseDto);
		return dto;
	}

	public String deleteIssuer(int issuerID) {
		issuerRepository.findById(issuerID)
				.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<IssuerRelationResponseDto> institutionRelationResponseDto = issueRelationService
				.getAllIssuerRelationByIssuerId(issuerID);
		IssuerRelation issuerRelation = issuerRelationRepository.findById(institutionRelationResponseDto.get(0).getRecordSeqId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_RELATION_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(issuerID, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		issueRelationService.deleteIssuerRelation(institutionRelationResponseDto.get(0).getRecordSeqId(), issuerRelation);
		issuerRepository.deleteById(issuerID);
		return "Deleted Issuer Successfully";
	}
}
