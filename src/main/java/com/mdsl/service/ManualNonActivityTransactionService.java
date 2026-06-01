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
import com.mdsl.model.dto.request.ManualNonActivityFeesPackageRequestDto;
import com.mdsl.model.dto.request.ManualNonActivityTransactionRequestDto;
import com.mdsl.model.dto.response.ManualNonActivityTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.ManualNonActivityTransaction;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.mapper.ManualNonActivityTransactionMapper;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.ManualNonActivityTransactionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManualNonActivityTransactionService {
	@Autowired
	private ManualNonActivityTransactionRepository manualNonActivityTransactionRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;

	@Autowired
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private ManualNonActivityTransactionMapper manualNonActivityTransactionMapper;

//	public List<ManualNonActivityTransactionResponseDto> getAllTransactions() {
//		List<ManualNonActivityTransactionResponseDto> allManualNonActivityTransactionCodesDto = new ArrayList<ManualNonActivityTransactionResponseDto>();
//		List<ManualNonActivityTransaction> allManualNonActivityTransactionCodes = manualNonActivityTransactionRepository
//				.findAll(Sort.by(Sort.Direction.ASC, "manualNonActivityTransactionId"));
//
//		allManualNonActivityTransactionCodes.stream().forEach((manualNonActivityTransactionCode) -> {
//			ManualNonActivityTransactionResponseDto manualNonActivityTransactionCodeResponseDto = manualNonActivityTransactionMapper
//					.toDto(manualNonActivityTransactionCode);
//			allManualNonActivityTransactionCodesDto.add(manualNonActivityTransactionCodeResponseDto);
//		});
//
//		return allManualNonActivityTransactionCodesDto;
//	}

	public ResponseEntity<PaginationResponseDto> getAllTransactions(
			ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto) {

		Page<ManualNonActivityTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualNonActivityTransactionRequestDto.getSort(), "manualNonActivityTransactionId",
				manualNonActivityTransactionRequestDto.getPageNo(),
				manualNonActivityTransactionRequestDto.getPageSize());

		page = manualNonActivityTransactionRepository.findAll(pageRequest);

		List<ManualNonActivityTransactionResponseDto> allManualNonActivityTransactionDto = new ArrayList<ManualNonActivityTransactionResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			ManualNonActivityTransactionResponseDto manualNonActivityTransactionCodeResponseDto = manualNonActivityTransactionMapper
					.toDto(transaction);
			manualNonActivityTransactionCodeResponseDto.setPageNo(manualNonActivityTransactionRequestDto.getPageNo());
			manualNonActivityTransactionCodeResponseDto
					.setPageSize(manualNonActivityTransactionRequestDto.getPageSize());

			allManualNonActivityTransactionDto.add(manualNonActivityTransactionCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allManualNonActivityTransactionDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}

	public ManualNonActivityTransactionResponseDto getManualNonActivityTransactionById(int id) {
		Optional<ManualNonActivityTransaction> manualNonActivityTransaction = manualNonActivityTransactionRepository
				.findById(id);
		ManualNonActivityTransaction transCode = manualNonActivityTransaction.orElseThrow(
				() -> new BusinessException(ResponseCode.MNT_NOT_FOUND, HttpStatus.NOT_FOUND));
		return manualNonActivityTransactionMapper.toDto(transCode);
	}

	public ManualNonActivityTransactionResponseDto saveOrUpdateManualNonActivityTransaction(
			ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto) {

//		ManualNonActivityTransaction trans = null;
		ManualNonActivityTransaction manualNonActivityTransaction;
		Institution institution = null;
		SystemCode systemCode = null;
		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Currency currency = currencyRepository
				.findById(manualNonActivityTransactionRequestDto.getTransactionCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (manualNonActivityTransactionRequestDto.getInstitutionId() != null) {
			System.out.println("manualNonActivityTransactionRequestDto.getInstitutionId()>>>"+manualNonActivityTransactionRequestDto.getInstitutionId());
			institution = institutionRepository.findById(manualNonActivityTransactionRequestDto.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
				.findByTransactionIdAndInstitution(manualNonActivityTransactionRequestDto.getTransactionId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND,
						HttpStatus.NOT_FOUND));


		Entities entities = entitiesRepository.findByEntityIdAndInstitution(manualNonActivityTransactionRequestDto.getOutletId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (manualNonActivityTransactionRequestDto.getSystemCodeId() != 0) {
			systemCode = systemCodeRepository.findById(manualNonActivityTransactionRequestDto.getSystemCodeId())
					.orElseThrow(
							() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		if (Objects.isNull(manualNonActivityTransactionRequestDto.getManualNonActivityTransactionId())
				|| manualNonActivityTransactionRequestDto.getManualNonActivityTransactionId() == 0) {
			manualNonActivityTransaction = manualNonActivityTransactionMapper
					.toEntity(manualNonActivityTransactionRequestDto);
	//		manualNonActivityTransaction.setRecordSeqId(0);
	//		manualNonActivityTransaction.setUserCreate("test");
			manualNonActivityTransaction.setDateCreate(new Date());

			manualNonActivityTransaction.setEntitiesObject(entities);
			manualNonActivityTransaction.setEntities(entities.getEntityId());

			manualNonActivityTransaction.setTransactionEntity(defaultTransactionId);
			manualNonActivityTransaction.setTransaction(defaultTransactionId.getTransactionId());
			manualNonActivityTransaction.setTransactionCurrency(currency);
			manualNonActivityTransaction.setInstitution(institution);
			manualNonActivityTransaction.setReasonCode(systemCode);
			if(userDetails!=null) {
				manualNonActivityTransaction.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}

			manualNonActivityTransaction = manualNonActivityTransactionRepository.save(manualNonActivityTransaction);
		}

		else {

			manualNonActivityTransaction = manualNonActivityTransactionRepository
					.findById(manualNonActivityTransactionRequestDto.getManualNonActivityTransactionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.MNT_NOT_FOUND,
							HttpStatus.NOT_FOUND));

			ManualNonActivityTransaction manualNonActivityTransaction1=manualNonActivityTransaction;
			
			manualNonActivityTransaction = manualNonActivityTransactionMapper
					.toEntity(manualNonActivityTransactionRequestDto);

		//	manualNonActivityTransaction.setRecordSeqId(0);
		//	manualNonActivityTransaction.setUserCreate("test");
			manualNonActivityTransaction.setDateCreate(new Date());
			manualNonActivityTransaction.setTransactionDate(manualNonActivityTransactionRequestDto.getTransactionDate());
			
			manualNonActivityTransaction.setEntities(entities.getEntityId());
			manualNonActivityTransaction.setEntitiesObject(entities);

			manualNonActivityTransaction.setTransactionCurrency(currency);
			manualNonActivityTransaction.setTransactionEntity(defaultTransactionId);
			manualNonActivityTransaction.setTransaction(defaultTransactionId.getTransactionId());
			manualNonActivityTransaction.setInstitution(institution);
			manualNonActivityTransaction.setReasonCode(systemCode);
			manualNonActivityTransaction.setUserCreate(manualNonActivityTransaction1.getUserCreate());

			manualNonActivityTransaction = manualNonActivityTransactionRepository.save(manualNonActivityTransaction);

		}

		ManualNonActivityTransactionResponseDto manualNonActivityTransactionResponseDto= manualNonActivityTransactionMapper.toDto(manualNonActivityTransaction);
		
		return manualNonActivityTransactionResponseDto;
	}

	public void deleteManualNonActivityTransaction(int id) throws Exception {
		manualNonActivityTransactionRepository.findById(id).orElseThrow(
				() -> new BusinessException(ResponseCode.MNT_NOT_FOUND, HttpStatus.NOT_FOUND));
		manualNonActivityTransactionRepository.deleteById(id);
	}

//	public List<ManualNonActivityTransactionResponseDto> getTransactionsBySearch(
//			ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto) {
//
//		List<ManualNonActivityTransactionResponseDto> responseDtos = new ArrayList<ManualNonActivityTransactionResponseDto>();
//		List<ManualNonActivityTransaction> manualNonActivityTransactions = null;
//
//		if (manualNonActivityTransactionRequestDto.getTransactionId() == null) {
//			manualNonActivityTransactions = manualNonActivityTransactionRepository
//					.findByInstitution_InstitutionIdAndEntities_EntityIdAndTransactionDateBetween(
//							manualNonActivityTransactionRequestDto.getInstitutionId(),
//							manualNonActivityTransactionRequestDto.getOutletId(),
//							manualNonActivityTransactionRequestDto.getFromTransactionDate(),
//							manualNonActivityTransactionRequestDto.getToTransactionDate());
//		}
//
//		else if (manualNonActivityTransactionRequestDto.getTransactionId() != null) {
//			manualNonActivityTransactions = manualNonActivityTransactionRepository
//					.findByInstitution_InstitutionIdAndEntities_EntityIdAndTransaction_TransactionIdAndTransactionDateBetween(
//							manualNonActivityTransactionRequestDto.getInstitutionId(),
//							manualNonActivityTransactionRequestDto.getOutletId(),
//							manualNonActivityTransactionRequestDto.getTransactionId(),
//							manualNonActivityTransactionRequestDto.getFromTransactionDate(),
//							manualNonActivityTransactionRequestDto.getToTransactionDate());
//		}
//
//		manualNonActivityTransactions.stream().forEach((trans) -> {
//			ManualNonActivityTransactionResponseDto dto = manualNonActivityTransactionMapper.toDto(trans);
//			responseDtos.add(dto);
//		});
//
//		return responseDtos;
//	}

//	public List<ManualNonActivityTransactionResponseDto> getManualNonActivityTransactionByInstitutionId(
//			ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto) {
//		List<ManualNonActivityTransactionResponseDto> allManualNonActivityTransactionCodesDto = new ArrayList<ManualNonActivityTransactionResponseDto>();
//
//		Institution institution = institutionRepository
//				.findById(manualNonActivityTransactionRequestDto.getInstitutionId())
//				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
//
//		List<ManualNonActivityTransaction> allManualNonActivityTransactionCodes = manualNonActivityTransactionRepository
//				.findByInstitution_InstitutionId(institution.getInstitutionId(),
//						Sort.by(Sort.Direction.ASC, "manualNonActivityTransactionId"));
//
//		allManualNonActivityTransactionCodes.stream().forEach((manualNonActivityTransactionCode) -> {
//			ManualNonActivityTransactionResponseDto manualNonActivityTransactionCodeResponseDto = manualNonActivityTransactionMapper
//					.toDto(manualNonActivityTransactionCode);
//			allManualNonActivityTransactionCodesDto.add(manualNonActivityTransactionCodeResponseDto);
//		});
//
//		return allManualNonActivityTransactionCodesDto;
//	}

	public ResponseEntity<PaginationResponseDto> getManualNonActivityTransactionByInstitutionId(
			ManualNonActivityFeesPackageRequestDto manualNonActivityTransactionRequestDto) {

		Page<ManualNonActivityTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualNonActivityTransactionRequestDto.getSort(), "manualNonActivityTransactionId",
				manualNonActivityTransactionRequestDto.getPageNo(),
				manualNonActivityTransactionRequestDto.getPageSize());

		page = manualNonActivityTransactionRepository.findByInstitution_InstitutionId(pageRequest,
				manualNonActivityTransactionRequestDto.getInstitutionId());

		List<ManualNonActivityTransactionResponseDto> allManualNonActivityTransactionDto = new ArrayList<ManualNonActivityTransactionResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			ManualNonActivityTransactionResponseDto manualNonActivityTransactionCodeResponseDto = manualNonActivityTransactionMapper
					.toDto(transaction);
			manualNonActivityTransactionCodeResponseDto.setPageNo(manualNonActivityTransactionRequestDto.getPageNo());
			manualNonActivityTransactionCodeResponseDto
					.setPageSize(manualNonActivityTransactionRequestDto.getPageSize());

			allManualNonActivityTransactionDto.add(manualNonActivityTransactionCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allManualNonActivityTransactionDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}

	public ResponseEntity<PaginationResponseDto> getTransactionsBySearch(ManualNonActivityTransactionRequestDto manualNonActivityTransactionRequestDto) {
		Page<ManualNonActivityTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualNonActivityTransactionRequestDto.getSort(), "manualNonActivityTransactionId",
				manualNonActivityTransactionRequestDto.getPageNo(),
				manualNonActivityTransactionRequestDto.getPageSize());

		List<ManualNonActivityTransactionResponseDto> responseDtos = new ArrayList<ManualNonActivityTransactionResponseDto>();

		if ((!manualNonActivityTransactionRequestDto.getInstitutionId().equals(null)|| (!manualNonActivityTransactionRequestDto.getInstitutionId().equals("")))
				&& (manualNonActivityTransactionRequestDto.getOutletId().equals(null)|| (manualNonActivityTransactionRequestDto.getOutletId().equals("")))
				&& manualNonActivityTransactionRequestDto.getFromTransactionDate() != null && manualNonActivityTransactionRequestDto.getToTransactionDate() != null
				&& (manualNonActivityTransactionRequestDto.getTransactionId().equals(null) || (manualNonActivityTransactionRequestDto.getTransactionId().equals("")) || (manualNonActivityTransactionRequestDto.getTransactionId().equals("0")))) {
			page = manualNonActivityTransactionRepository.findByInstitution_InstitutionIdAndTransactionDateBetween(
					pageRequest, manualNonActivityTransactionRequestDto.getInstitutionId(),
					manualNonActivityTransactionRequestDto.getFromTransactionDate(),
					manualNonActivityTransactionRequestDto.getToTransactionDate());
		}

		else if (manualNonActivityTransactionRequestDto.getInstitutionId() != null
				&& manualNonActivityTransactionRequestDto.getOutletId() != null
				&& manualNonActivityTransactionRequestDto.getFromTransactionDate() == null
				&& manualNonActivityTransactionRequestDto.getToTransactionDate() == null
				&& (manualNonActivityTransactionRequestDto.getTransactionId().equals(null)
						|| (manualNonActivityTransactionRequestDto.getTransactionId().equals("")) || (manualNonActivityTransactionRequestDto.getTransactionId().equals("0")))) {
			page = manualNonActivityTransactionRepository.findByInstitution_InstitutionId(pageRequest,
					manualNonActivityTransactionRequestDto.getInstitutionId());
		}

		else if ((manualNonActivityTransactionRequestDto.getTransactionId().equals(null)
				|| (manualNonActivityTransactionRequestDto.getTransactionId().equals("")) || (manualNonActivityTransactionRequestDto.getTransactionId().equals("0")))) {
			page = manualNonActivityTransactionRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetween(pageRequest,
							manualNonActivityTransactionRequestDto.getInstitutionId(),
							manualNonActivityTransactionRequestDto.getOutletId(),
							manualNonActivityTransactionRequestDto.getFromTransactionDate(),
							manualNonActivityTransactionRequestDto.getToTransactionDate());
		}

		else if ((!manualNonActivityTransactionRequestDto.getTransactionId().equals(null)
				|| (!manualNonActivityTransactionRequestDto.getTransactionId().equals("")) || (!manualNonActivityTransactionRequestDto.getTransactionId().equals("0")))) {
			page = manualNonActivityTransactionRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionEntity_TransactionIdAndTransactionDateBetween(
							pageRequest, manualNonActivityTransactionRequestDto.getInstitutionId(),
							manualNonActivityTransactionRequestDto.getOutletId(),
							manualNonActivityTransactionRequestDto.getTransactionId(),
							manualNonActivityTransactionRequestDto.getFromTransactionDate(),
							manualNonActivityTransactionRequestDto.getToTransactionDate());
		}

		page.stream().forEach((trans) -> {
			ManualNonActivityTransactionResponseDto dto = manualNonActivityTransactionMapper.toDto(trans);
			dto.setPageNo(manualNonActivityTransactionRequestDto.getPageNo());
			dto.setPageSize(manualNonActivityTransactionRequestDto.getPageSize());
			responseDtos.add(dto);
		});

		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, responseDtos, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

}
