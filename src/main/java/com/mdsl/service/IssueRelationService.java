package com.mdsl.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mdsl.utils.MakerCheckerEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.IssuerRelationPanRangeRequestDto;
import com.mdsl.model.dto.request.IssuerRelationRequestDto;
import com.mdsl.model.dto.request.IssuerRelationSearchRequestDto;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.response.IssuerRelationResponseDto;
import com.mdsl.model.dto.response.PageableIssuerRelationResponseDto;
import com.mdsl.model.dto.response.PaginatedResponseDto;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.IssuerProfile;
import com.mdsl.model.entity.IssuerRelation;
import com.mdsl.model.mapper.IssuerRelationMapper;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.IssuerProfileRepository;
import com.mdsl.repository.IssuerRelationRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@Service
public class IssueRelationService {
	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private IssuerProfileRepository issuerRepository;

	@Autowired
	private IssuerRelationRepository issuerRelationRepository;

	@Autowired
	private IssuerRelationMapper issuerRelationMapper;
	
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;
	
	public List<IssuerRelationResponseDto> getAllIssuerRelationByIssuerId(int issuerId) {
		IssuerProfile issuer = issuerRepository.findById(issuerId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND));
//		List<IssuerRelation> issuerRelation = issuerRelationRepository.findAllByIssuerId(issuerId,
//				Sort.by(Sort.Direction.ASC, "issuerRelationId"));
		List<IssuerRelation> issuerRelation = issuerRelationRepository.findByIssuerAcqProfile(issuer.getIssuerAcqProfile(),
				Sort.by(Sort.Direction.ASC, "issuerRelationId"));
		List<IssuerRelationResponseDto> allIssuerRelationResponseDto = new ArrayList<IssuerRelationResponseDto>();
		for (IssuerRelation temp : issuerRelation) {
			IssuerRelationResponseDto issuerRelationResponseDto = issuerRelationMapper.toDto(temp);
			Optional<Country> country = this.countryRepository.findByCntryCode(temp.getCntryCode());
			if(country.isPresent()) {
				issuerRelationResponseDto.setCntryName(country.get().getCntryName());
			}
			allIssuerRelationResponseDto.add(issuerRelationResponseDto);
		}
		return allIssuerRelationResponseDto;
	}
	
	public List<IssuerRelationResponseDto> getAllIssuerRelations() {
		List<IssuerRelationResponseDto> issuerRelationResponseDto = new ArrayList<IssuerRelationResponseDto>();
		List<IssuerRelation> allIssuerRelations = issuerRelationRepository.findAll(Sort.by(Sort.Direction.ASC, "recordSeqId"));
		allIssuerRelations.stream().forEach((issuerRelation) -> {
			IssuerRelationResponseDto issRelationResponseDto = issuerRelationMapper.toDto(issuerRelation);
			Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
			if(country.isPresent()) {
				issRelationResponseDto.setCntryName(country.get().getCntryName());
			}
			issuerRelationResponseDto.add(issRelationResponseDto);
		});
		
		return issuerRelationResponseDto;
	}
	
	public List<IssuerRelationResponseDto> getAllIssuerRelationsByPanRange(IssuerRelationPanRangeRequestDto issuerRelationPanRangeRequestDto) {
		List<IssuerRelationResponseDto> issuerRelationResponseDto = new ArrayList<IssuerRelationResponseDto>();
		
		if(issuerRelationPanRangeRequestDto.getCntryCode().equals("")) {
			List<IssuerRelation> allIssuerRelations = issuerRelationRepository.findByInstitutionIdAndPanRangeFromAndPanRangeTo(issuerRelationPanRangeRequestDto.getInstitutionId(),
					issuerRelationPanRangeRequestDto.getPanRangeFrom(),
					issuerRelationPanRangeRequestDto.getPanRangeTo());
			
			allIssuerRelations.stream().forEach((issuerRelation) -> {
				IssuerRelationResponseDto issRelationResponseDto = issuerRelationMapper.toDto(issuerRelation);
				Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
				if(country.isPresent()) {
					issRelationResponseDto.setCntryName(country.get().getCntryName());
				}
				issuerRelationResponseDto.add(issRelationResponseDto);
			});
		}
		else {
			List<IssuerRelation> allIssuerRelations = issuerRelationRepository.findByInstitutionIdAndPanRangeFromAndPanRangeToAndCntryCode(issuerRelationPanRangeRequestDto.getInstitutionId(),
					issuerRelationPanRangeRequestDto.getPanRangeFrom(),
					issuerRelationPanRangeRequestDto.getPanRangeTo(),
					issuerRelationPanRangeRequestDto.getCntryCode());
			
			allIssuerRelations.stream().forEach((issuerRelation) -> {
				IssuerRelationResponseDto issRelationResponseDto = issuerRelationMapper.toDto(issuerRelation);
				Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
				if(country.isPresent()) {
					issRelationResponseDto.setCntryName(country.get().getCntryName());
				}
				issuerRelationResponseDto.add(issRelationResponseDto);
			});
		}
		
		return issuerRelationResponseDto;
	}
	
	public List<IssuerRelationResponseDto> getIssuerRelationsByPanRange(IssuerRelationSearchRequestDto issuerRelationSearchRequestDto) {
		List<IssuerRelationResponseDto> listOfIssuerRelations = new ArrayList<IssuerRelationResponseDto>();
		Pageable pageable = PageRequest.of(issuerRelationSearchRequestDto.getPaginationRequestDto().getOffset(),
				issuerRelationSearchRequestDto.getPaginationRequestDto().getPageSize(),
				issuerRelationSearchRequestDto.getPaginationRequestDto().getAsc().trim().equalsIgnoreCase("true") ? Sort.by(issuerRelationSearchRequestDto.getPaginationRequestDto().getSortBy()).ascending(): Sort.by(issuerRelationSearchRequestDto.getPaginationRequestDto().getSortBy()).descending());
		Page<IssuerRelation> issuerRelations;
		
		if(issuerRelationSearchRequestDto.getPanRange().equals("")) {
			issuerRelations = this.issuerRelationRepository.findByIssuerAcqProfileAndInstitutionId(issuerRelationSearchRequestDto.getIssuerAcqProfile(),issuerRelationSearchRequestDto.getInstitutionId(), pageable);
		} else if(issuerRelationSearchRequestDto.isLikeSearch()) {
			issuerRelations = this.issuerRelationRepository.findByIssuerAcqProfileAndInstitutionIdAndLikePanRangeFromOrPanRangeTo(issuerRelationSearchRequestDto.getIssuerAcqProfile(),issuerRelationSearchRequestDto.getInstitutionId(), issuerRelationSearchRequestDto.getPanRange(), pageable);
		} else {
			issuerRelations = this.issuerRelationRepository.findByIssuerAcqProfileAndInstitutionIdAndPanRangeFromOrPanRangeTo(issuerRelationSearchRequestDto.getIssuerAcqProfile(),issuerRelationSearchRequestDto.getInstitutionId(), Validations.rightPad(issuerRelationSearchRequestDto.getPanRange(), "0", 19), pageable);
		}
		
		PageableIssuerRelationResponseDto pageableIssuerRelationResponseDto = new PageableIssuerRelationResponseDto();
		pageableIssuerRelationResponseDto.setPaginatedResponseDto(getPaginationResponseDto(issuerRelationSearchRequestDto.getPaginationRequestDto(), issuerRelations));
		issuerRelations.forEach((issuerRelation)->{
			Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
			IssuerRelationResponseDto responseDto = issuerRelationMapper.toDto(issuerRelation);
			if(country.isPresent()) {
				responseDto.setCntryName(country.get().getCntryName());
			}
			responseDto.setNbOfRecords(pageableIssuerRelationResponseDto.getPaginatedResponseDto().getTotalNumberOfRecords());
			listOfIssuerRelations.add(responseDto);
		});
		return listOfIssuerRelations; 
	}
	
	public List<IssuerRelationResponseDto> getAllPaginatedIssuerRelationByIssuerId(int issuerId, PaginatedRequestDto paginationRequestDto) {
		IssuerProfile issuer = issuerRepository.findById(issuerId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<IssuerRelationResponseDto> listOfIssuerRelations = new ArrayList<IssuerRelationResponseDto>();
		Pageable pageable = PageRequest.of(paginationRequestDto.getOffset(), paginationRequestDto.getPageSize(), paginationRequestDto.getAsc().trim().equalsIgnoreCase("true") ? Sort.by(paginationRequestDto.getSortBy()).ascending(): Sort.by(paginationRequestDto.getSortBy()).descending());
		Page<IssuerRelation> issuerRelations = issuerRelationRepository.findByIssuerAcqProfile(issuer.getIssuerAcqProfile(), pageable);
		PageableIssuerRelationResponseDto pageableIssuerRelationResponseDto = new PageableIssuerRelationResponseDto();
		pageableIssuerRelationResponseDto.setPaginatedResponseDto(getPaginationResponseDto(paginationRequestDto, issuerRelations));
		issuerRelations.forEach((issuerRelation)->{
			Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
			IssuerRelationResponseDto responseDto = issuerRelationMapper.toDto(issuerRelation);
			if(country.isPresent()) {
				responseDto.setCntryName(country.get().getCntryName());
			}
			responseDto.setNbOfRecords(pageableIssuerRelationResponseDto.getPaginatedResponseDto().getTotalNumberOfRecords());
			listOfIssuerRelations.add(responseDto);
		});
		return listOfIssuerRelations; 
	}
	
	public PaginatedResponseDto getPaginationResponseDto(Object paginatedRequestDto, Page<?> pagesResult) {
		PaginatedResponseDto paginationResponseDto = new PaginatedResponseDto();
		
		if (paginatedRequestDto instanceof  PaginatedRequestDto) {
			PaginatedRequestDto request = (PaginatedRequestDto) paginatedRequestDto;
			paginationResponseDto.setAsc(request.getAsc());
			paginationResponseDto.setPageNumber(request.getOffset());
			paginationResponseDto.setPageSize(request.getPageSize());
			paginationResponseDto.setSortBy(request.getSortBy());
		}

		long totalElements = pagesResult.getTotalElements();
		paginationResponseDto.setTotalNumberOfRecords((int)totalElements);
		return paginationResponseDto;
	} 

	public IssuerRelationResponseDto getIssuerRelationById(int id) {
		IssuerRelation issuerRelation = issuerRelationRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_RELATION_NOT_FOUND, HttpStatus.NOT_FOUND));;
		Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
		IssuerRelationResponseDto responseDto = issuerRelationMapper.toDto(issuerRelation);
		if(country.isPresent()) {
			responseDto.setCntryName(country.get().getCntryName());
		}
		return responseDto;
	}

	public List<IssuerRelationResponseDto> getIssuerRelationByIssuerAcqProfile(String issuerAcqProfile,String instId) {
		List<IssuerRelationResponseDto> issuerRelationResponseDto = new ArrayList<IssuerRelationResponseDto>();		
		Optional<IssuerProfile> issuerProfile = issuerRepository.findByIssuerAcqProfileAndInstitutionId(issuerAcqProfile.trim().toUpperCase(),instId);
		if(issuerProfile.isEmpty()) {
			throw new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		List<IssuerRelation> allIssuerRelations = issuerRelationRepository.findByIssuerAcqProfileAndInstitutionId(issuerAcqProfile.trim().toUpperCase(),instId, Sort.by(Sort.Direction.ASC, "recordSeqId"));
		
		allIssuerRelations.stream().forEach((issuerRelation) -> {
			IssuerRelationResponseDto issRelationResponseDto = issuerRelationMapper.toDto(issuerRelation);
			Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
			if(country.isPresent()) {
				issRelationResponseDto.setCntryName(country.get().getCntryName());
			}
			issuerRelationResponseDto.add(issRelationResponseDto);
		});
		
//		if(issuerRelationResponseDto.isEmpty()) {
//			throw new BusinessException(ResponseCode.CFG_NO_DATA_FOUND, HttpStatus.NOT_FOUND);
//		}
		
		return issuerRelationResponseDto;
	}

	public IssuerRelationResponseDto saveOrUpdateIssuer(IssuerRelationRequestDto issuerRelationRequestDto) {
		IssuerRelation issuerRelation = null;
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		issuerRepository.findByIssuerAcqProfileAndInstitutionId(issuerRelationRequestDto.getIssuerAcqProfile().trim().toUpperCase(),issuerRelationRequestDto.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_NOT_FOUND, HttpStatus.NOT_FOUND));
		institutionRepository.findById(issuerRelationRequestDto.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		Optional<IssuerRelation> requestIssuerRelation = issuerRelationRepository.findById(issuerRelationRequestDto.getRecordSeqId());
		
		BigInteger panRangeFromBigInt = new BigInteger(issuerRelationRequestDto.getPanRangeFrom());
		BigInteger panRangeToBigInt = new BigInteger(issuerRelationRequestDto.getPanRangeTo());
		issuerRelationRequestDto.setIssuerAcqProfile(issuerRelationRequestDto.getIssuerAcqProfile().trim().toUpperCase());
		if(panRangeFromBigInt.compareTo(panRangeToBigInt) > 0) {
			throw new BusinessException("Pan Range From must be lesser than Pan Range To", HttpStatus.METHOD_NOT_ALLOWED);
		}
		
		if (requestIssuerRelation.isPresent()) {//Case of update
			issuerRelation = issuerRelationMapper.toEntity(issuerRelationRequestDto);
			issuerRelation.setCreatedBy(requestIssuerRelation.get().getCreatedBy());
			issuerRelation.setCreatedDate(requestIssuerRelation.get().getCreatedDate());
			issuerRelation.setUpdatedBy(userDetails.getId());
			issuerRelation.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		} else {
			issuerRelation = issuerRelationMapper.toEntity(issuerRelationRequestDto);
			issuerRelation.setCreatedBy(userDetails.getId());
			issuerRelation.setCreatedDate(new Timestamp(System.currentTimeMillis()));

		}
		if (makerCheckerEngine.processIfRequired(issuerRelationRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		issuerRelation = issuerRelationRepository.save(issuerRelation);
		Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
		IssuerRelationResponseDto responseDto = issuerRelationMapper.toDto(issuerRelation);
        country.ifPresent(value -> responseDto.setCntryName(value.getCntryName()));
		return responseDto;
	}
	
	public IssuerRelationResponseDto saveOrUpdateIssuerRelation(IssuerRelationRequestDto issuerRelationRequestDto, String instId, IssuerRelation issuerRelation) {
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		issuerRelationRequestDto.setIssuerAcqProfile(issuerRelationRequestDto.getIssuerAcqProfile().trim().toUpperCase());
		if (issuerRelationRequestDto.getRecordSeqId() != 0) {
			if (issuerRelation == null) {
				issuerRelation = issuerRelationRepository.findById(issuerRelationRequestDto.getRecordSeqId())
						.orElseThrow(
								() -> new BusinessException(ResponseCode.ISS_ISSUER_RELATION_NOT_FOUND, HttpStatus.NOT_FOUND));
			}
			issuerRelation.setInstitutionId(instId);
			issuerRelation.setIssuerAcqProfile(issuerRelationRequestDto.getIssuerAcqProfile());
			issuerRelation.setPanRangeFrom(issuerRelationRequestDto.getPanRangeFrom());
			issuerRelation.setPanRangeTo(issuerRelationRequestDto.getPanRangeTo());
			issuerRelation.setCreatedBy(issuerRelation.getCreatedBy());
			issuerRelation.setCreatedDate(issuerRelation.getCreatedDate());
			issuerRelation.setUpdatedBy(userDetails.getId());
			issuerRelation.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		} else {
			issuerRelation = issuerRelationMapper.toEntity(issuerRelationRequestDto);
			issuerRelation.setIssuerAcqProfile(issuerRelationRequestDto.getIssuerAcqProfile());
			issuerRelation.setCreatedBy(userDetails.getId());
			issuerRelation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		}
		issuerRelation = issuerRelationRepository.save(issuerRelation);
		Optional<Country> country = this.countryRepository.findByCntryCode(issuerRelation.getCntryCode());
		IssuerRelationResponseDto responseDto = issuerRelationMapper.toDto(issuerRelation);
		country.ifPresent(value -> responseDto.setCntryName(value.getCntryName()));
		return responseDto;
	}

	public void deleteIssuerRelation(int issuerRelationId, IssuerRelation issuerRelation) {
		if (issuerRelation == null) {
			issuerRelation = issuerRelationRepository.findById(issuerRelationId)
						.orElseThrow(() -> new BusinessException(ResponseCode.ISS_ISSUER_RELATION_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		issuerRelationRepository.deleteById(issuerRelation.getRecordSeqId());
	}
}