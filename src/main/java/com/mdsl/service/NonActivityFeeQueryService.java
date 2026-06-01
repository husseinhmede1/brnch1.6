package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.NonActivityFeeQueryRequestDto;
import com.mdsl.model.dto.response.NonActivityFeeQueryResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.NonActivityFeeQuery;
import com.mdsl.model.mapper.NonActivityFeeQueryMapper;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.NonActivityFeeQueryRepository;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NonActivityFeeQueryService {
	@Autowired
	private NonActivityFeeQueryRepository nonActivityFeeQueryRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;

	@Autowired
	private NonActivityFeeQueryMapper nonActivityFeeQueryMapper;

//	public List<NonActivityFeeQueryResponseDto> getAllTransactions() {
//		List<NonActivityFeeQueryResponseDto> allNonActivityFeeQueryCodesDto = new ArrayList<NonActivityFeeQueryResponseDto>();
//		List<NonActivityFeeQuery> allNonActivityFeeQueryCodes = nonActivityFeeQueryRepository
//				.findAll(Sort.by(Sort.Direction.ASC, "nonActivityFeeQueryId"));
//
//		allNonActivityFeeQueryCodes.stream().forEach((nonActivityFeeQueryCode) -> {
//			NonActivityFeeQueryResponseDto nonActivityFeeQueryCodeResponseDto = nonActivityFeeQueryMapper
//					.toDto(nonActivityFeeQueryCode);
//			allNonActivityFeeQueryCodesDto.add(nonActivityFeeQueryCodeResponseDto);
//		});
//
//		return allNonActivityFeeQueryCodesDto;
//	}

	public ResponseEntity<PaginationResponseDto> getAllTransactions(
			NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto) {

		Page<NonActivityFeeQuery> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				nonActivityFeeQueryRequestDto.getSort(), "nonActivityFeeQueryId",
				nonActivityFeeQueryRequestDto.getPageNo(), nonActivityFeeQueryRequestDto.getPageSize());

		page = nonActivityFeeQueryRepository.findAll(pageRequest);

		List<NonActivityFeeQueryResponseDto> allNonActivityFeeQueryDto = new ArrayList<NonActivityFeeQueryResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			NonActivityFeeQueryResponseDto nonActivityFeeQueryCodeResponseDto = nonActivityFeeQueryMapper
					.toDto(transaction);
			nonActivityFeeQueryCodeResponseDto.setPageNo(nonActivityFeeQueryRequestDto.getPageNo());
			nonActivityFeeQueryCodeResponseDto.setPageSize(nonActivityFeeQueryRequestDto.getPageSize());

			allNonActivityFeeQueryDto.add(nonActivityFeeQueryCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allNonActivityFeeQueryDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}

	public NonActivityFeeQueryResponseDto getNonActivityFeeQueryById(int id) {
		Optional<NonActivityFeeQuery> nonActivityFeeQuery = nonActivityFeeQueryRepository.findById(id);
		NonActivityFeeQuery transCode = nonActivityFeeQuery.orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_MANUAL_TRANSACTION, HttpStatus.NOT_FOUND));
		return nonActivityFeeQueryMapper.toDto(transCode);
	}

	public NonActivityFeeQueryResponseDto saveOrUpdateNonActivityFeeQuery(
			NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto) {

//		NonActivityFeeQuery trans = null;
		NonActivityFeeQuery nonActivityFeeQuery;
		Institution institution = null;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Currency currency = currencyRepository.findById(nonActivityFeeQueryRequestDto.getTransactionCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
		if (nonActivityFeeQueryRequestDto.getInstitutionId() != null) {
			institution = institutionRepository.findById(nonActivityFeeQueryRequestDto.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
		}
		DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
				.findByTransactionIdAndInstitution(nonActivityFeeQueryRequestDto.getTransactionId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION,
						HttpStatus.NOT_FOUND));

		Entities entities = entitiesRepository.findByEntityIdAndInstitution(nonActivityFeeQueryRequestDto.getEntityId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (Objects.isNull(nonActivityFeeQueryRequestDto.getNonActivityFeeQueryId())
				|| nonActivityFeeQueryRequestDto.getNonActivityFeeQueryId() == 0) {
			nonActivityFeeQuery = nonActivityFeeQueryMapper.toEntity(nonActivityFeeQueryRequestDto);
			// nonActivityFeeQuery.setRecordSeqId(0);
			//nonActivityFeeQuery.setUserCreate("test");
			nonActivityFeeQuery.setDateCreate(new Date());

			nonActivityFeeQuery.setEntities(entities.getEntityId());
			nonActivityFeeQuery.setEntitiesObject(entities);

			nonActivityFeeQuery.setTransaction(defaultTransactionId.getTransactionId());
			nonActivityFeeQuery.setTransactionEntity(defaultTransactionId);
			
			nonActivityFeeQuery.setTransactionCurrency(currency);
			nonActivityFeeQuery.setInstitution(institution);
			if(userDetails!=null) {
				nonActivityFeeQuery.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}
			nonActivityFeeQuery = nonActivityFeeQueryRepository.save(nonActivityFeeQuery);
		}

		else {
			nonActivityFeeQuery = nonActivityFeeQueryRepository
					.findById(nonActivityFeeQueryRequestDto.getNonActivityFeeQueryId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_MANUAL_TRANSACTION,
							HttpStatus.NOT_FOUND));

			nonActivityFeeQuery = nonActivityFeeQueryMapper.toEntity(nonActivityFeeQueryRequestDto);

			// nonActivityFeeQuery.setRecordSeqId(0);
			nonActivityFeeQuery.setUserCreate("test");
			nonActivityFeeQuery.setDateCreate(new Date());

			nonActivityFeeQuery.setEntitiesObject(entities);
			nonActivityFeeQuery.setEntities(entities.getEntityId());

			nonActivityFeeQuery.setTransactionCurrency(currency);
			nonActivityFeeQuery.setTransaction(defaultTransactionId.getTransactionId());
			nonActivityFeeQuery.setTransactionEntity(defaultTransactionId);
			nonActivityFeeQuery.setInstitution(institution);
		//	nonActivityFeeQuery.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			nonActivityFeeQuery = nonActivityFeeQueryRepository.save(nonActivityFeeQuery);
		}

		return nonActivityFeeQueryMapper.toDto(nonActivityFeeQuery);
	}

	public void deleteNonActivityFeeQuery(int id) throws Exception {
		nonActivityFeeQueryRepository.findById(id).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_INVALID_MANUAL_TRANSACTION, HttpStatus.NOT_FOUND));
		nonActivityFeeQueryRepository.deleteById(id);
	}

	public ResponseEntity<PaginationResponseDto> getNonActivityFeeQueryByInstitutionId(
			NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto) {

		Page<NonActivityFeeQuery> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				nonActivityFeeQueryRequestDto.getSort(), "nonActivityFeeQueryId",
				nonActivityFeeQueryRequestDto.getPageNo(), nonActivityFeeQueryRequestDto.getPageSize());

		page = nonActivityFeeQueryRepository.findByInstitution_InstitutionId(pageRequest,
				nonActivityFeeQueryRequestDto.getInstitutionId());

		List<NonActivityFeeQueryResponseDto> allNonActivityFeeQueryDto = new ArrayList<NonActivityFeeQueryResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			NonActivityFeeQueryResponseDto nonActivityFeeQueryCodeResponseDto = nonActivityFeeQueryMapper
					.toDto(transaction);
			nonActivityFeeQueryCodeResponseDto.setPageNo(nonActivityFeeQueryRequestDto.getPageNo());
			nonActivityFeeQueryCodeResponseDto.setPageSize(nonActivityFeeQueryRequestDto.getPageSize());

			allNonActivityFeeQueryDto.add(nonActivityFeeQueryCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allNonActivityFeeQueryDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}

	public ResponseEntity<PaginationResponseDto> getQueriesBySearch(NonActivityFeeQueryRequestDto nonActivityFeeQueryRequestDto) {
		Page<NonActivityFeeQuery> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				nonActivityFeeQueryRequestDto.getSort(), "nonActivityFeeQueryId",
				nonActivityFeeQueryRequestDto.getPageNo(), nonActivityFeeQueryRequestDto.getPageSize());

		List<NonActivityFeeQueryResponseDto> responseDtos = new ArrayList<NonActivityFeeQueryResponseDto>();

		if ((!nonActivityFeeQueryRequestDto.getInstitutionId().equals(null)
				|| !nonActivityFeeQueryRequestDto.getInstitutionId().equals(""))
				&& (nonActivityFeeQueryRequestDto.getEntityId().equals(null)
						|| nonActivityFeeQueryRequestDto.getEntityId().equals(""))
				&& nonActivityFeeQueryRequestDto.getFromProcessingDate() != null
				&& nonActivityFeeQueryRequestDto.getToProcessingDate() != null
				&& (nonActivityFeeQueryRequestDto.getTransactionId().equals(null)
						|| (nonActivityFeeQueryRequestDto.getTransactionId().equals("")))) {
			page = nonActivityFeeQueryRepository.findByInstitution_InstitutionIdAndProcessingDateBetween(pageRequest,
					nonActivityFeeQueryRequestDto.getInstitutionId(),
					nonActivityFeeQueryRequestDto.getFromProcessingDate(),
					nonActivityFeeQueryRequestDto.getToProcessingDate());
		}

		else if (nonActivityFeeQueryRequestDto.getTransactionId().equals("")
				|| nonActivityFeeQueryRequestDto.getTransactionId().equals(null)) {
			page = nonActivityFeeQueryRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndProcessingDateBetween(pageRequest,
							nonActivityFeeQueryRequestDto.getInstitutionId(),
							nonActivityFeeQueryRequestDto.getEntityId(),
							nonActivityFeeQueryRequestDto.getFromProcessingDate(),
							nonActivityFeeQueryRequestDto.getToProcessingDate());
		}

		else if ((!nonActivityFeeQueryRequestDto.getInstitutionId().equals(null)
				|| !nonActivityFeeQueryRequestDto.getInstitutionId().equals(""))
				&& (!nonActivityFeeQueryRequestDto.getEntityId().equals(null)
						|| !nonActivityFeeQueryRequestDto.getEntityId().equals(""))
				&& nonActivityFeeQueryRequestDto.getFromProcessingDate() == null
				&& nonActivityFeeQueryRequestDto.getToProcessingDate() == null
				&& (nonActivityFeeQueryRequestDto.getTransactionId().equals(null)
						|| (nonActivityFeeQueryRequestDto.getTransactionId().equals("")))) {
			page = nonActivityFeeQueryRepository.findByInstitution_InstitutionId(pageRequest,
					nonActivityFeeQueryRequestDto.getInstitutionId());
		}

		else if (!nonActivityFeeQueryRequestDto.getTransactionId().equals("")
				|| (!nonActivityFeeQueryRequestDto.getTransactionId().equals(null))) {
			page = nonActivityFeeQueryRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndProcessingDateBetweenAndTransactionEntity_TransactionId(
							pageRequest, nonActivityFeeQueryRequestDto.getInstitutionId(),
							nonActivityFeeQueryRequestDto.getEntityId(),
							nonActivityFeeQueryRequestDto.getFromProcessingDate(),
							nonActivityFeeQueryRequestDto.getToProcessingDate(),
							nonActivityFeeQueryRequestDto.getTransactionId());
		}

		page.stream().forEach((trans) -> {
			NonActivityFeeQueryResponseDto dto = nonActivityFeeQueryMapper.toDto(trans);

			dto.setPageNo(nonActivityFeeQueryRequestDto.getPageNo());
			dto.setPageSize(nonActivityFeeQueryRequestDto.getPageSize());

			responseDtos.add(dto);
		});

		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, responseDtos, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

}
