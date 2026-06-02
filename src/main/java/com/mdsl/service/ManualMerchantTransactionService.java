package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ManualMerchantTransactionRequestDto;
import com.mdsl.model.dto.response.ManualMerchantTransactionResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.ManualMerchantTransaction;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.mapper.ManualMerchantTransactionMapper;
//import com.mdsl.repository.AcquiringTransactionRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.ManualMerchantTransactionRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManualMerchantTransactionService {

	@Autowired
	private ManualMerchantTransactionRepository manualMerchantTransactionRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private TerminalRepository terminalRepository;

	@Autowired
	private DefaultTransactionIdRepository defaultTransactionIdRepository;

	@Autowired
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private ManualMerchantTransactionMapper manualMerchantTransactionMapper;
    private final MakerCheckerEngine makerCheckerEngine;

	public ResponseEntity<PaginationResponseDto> getAllTransactions(
			ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto) {

		Page<ManualMerchantTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualMerchantTransactionRequestDto.getSort(), "merchantTransactionId",
				manualMerchantTransactionRequestDto.getPageNo(), manualMerchantTransactionRequestDto.getPageSize());

		page = manualMerchantTransactionRepository.findAll(pageRequest);

		List<ManualMerchantTransactionResponseDto> allManualMerchantTransactionDto = new ArrayList<ManualMerchantTransactionResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			ManualMerchantTransactionResponseDto manualMerchantTransactionCodeResponseDto = manualMerchantTransactionMapper
					.toDto(transaction);
			manualMerchantTransactionCodeResponseDto.setPageNo(manualMerchantTransactionRequestDto.getPageNo());
			manualMerchantTransactionCodeResponseDto.setPageSize(manualMerchantTransactionRequestDto.getPageSize());

			allManualMerchantTransactionDto.add(manualMerchantTransactionCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allManualMerchantTransactionDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}


	public ManualMerchantTransactionResponseDto getManualMerchantTransactionById(int id) {
		Optional<ManualMerchantTransaction> manualMerchantTransaction = manualMerchantTransactionRepository
				.findById(id);
		ManualMerchantTransaction transCode = manualMerchantTransaction.orElseThrow(
				() -> new BusinessException(ResponseCode.MMT_NOT_FOUND, HttpStatus.NOT_FOUND));
		return manualMerchantTransactionMapper.toDto(transCode);
	}

	public ManualMerchantTransactionResponseDto saveOrUpdateManualMerchantTransaction(ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto) {

		ManualMerchantTransaction manualMerchantTransaction;
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Currency tipsCurrency = null;
		SystemCode systemCode = null;
		Terminal terminal = null;

		Currency transCurrency = currencyRepository
				.findById(manualMerchantTransactionRequestDto.getTransactionCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (manualMerchantTransactionRequestDto.getTipsCurrencyId() != 0) {
			tipsCurrency = currencyRepository.findById(manualMerchantTransactionRequestDto.getTipsCurrencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (manualMerchantTransactionRequestDto.getSystemCodeId() != 0) {
			systemCode = systemCodeRepository.findById(manualMerchantTransactionRequestDto.getSystemCodeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		Institution institution = institutionRepository.findById(manualMerchantTransactionRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		DefaultTransactionId defaultTransactionId = defaultTransactionIdRepository
				.findFirstByTransactionIdAndInstitutionId(
						manualMerchantTransactionRequestDto.getTransactionId(),
						institution.getInstitutionId()
				)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_DEFAULT_TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND));

		Entities entities = entitiesRepository.findByEntityIdAndInstitution(manualMerchantTransactionRequestDto.getOutletId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (manualMerchantTransactionRequestDto.getInstitutionId() != null) {
			terminal = terminalRepository
					.findByTerminalIdAndInstitutionEntity_InstitutionId(
							manualMerchantTransactionRequestDto.getTerminalId(),
							institution.getInstitutionId()
					)
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_TERMINAL_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		if (Objects.isNull(manualMerchantTransactionRequestDto.getMerchantTransactionId())
				|| manualMerchantTransactionRequestDto.getMerchantTransactionId() == 0) {

			manualMerchantTransaction = manualMerchantTransactionMapper
					.toEntityMerchant(manualMerchantTransactionRequestDto);

			if (manualMerchantTransactionRepository.existsByInstitution_InstitutionIdAndTransactionEntity_TransactionId(institution.getInstitutionId(), defaultTransactionId.getTransactionId())) {
				throw new BusinessException (ResponseCode.TRANSACTION_INSTITUTION_EXISTS, HttpStatus.BAD_REQUEST);
			}

				manualMerchantTransaction.setUserCreate(
					userDetails != null ? Integer.valueOf(userDetails.getId()).toString() : "system"
			);
			manualMerchantTransaction.setDateCreate(new Date());
			manualMerchantTransaction.setComments(manualMerchantTransactionRequestDto.getComments());
			manualMerchantTransaction.setEntities(entities.getEntityId());
			manualMerchantTransaction.setEntitiesObject(entities);

			manualMerchantTransaction.setTransactionEntity(defaultTransactionId);
            manualMerchantTransaction.setTransaction(defaultTransactionId.getTransactionId());
            manualMerchantTransaction.setTransactionCurrency(transCurrency);
			manualMerchantTransaction.setTipsCurrency(tipsCurrency);
			manualMerchantTransaction.setTerminalEntity(terminal);
			manualMerchantTransaction.setTerminal(terminal.getTerminalId());
			
			manualMerchantTransaction.setInstitution(institution);
			manualMerchantTransaction.setReasonCode(systemCode);
			manualMerchantTransaction.setPan(manualMerchantTransactionRequestDto.getCardNumber());
			if (makerCheckerEngine.processIfRequired(manualMerchantTransactionRequestDto, ManualMerchantTransactionService.class.getName(), "saveOrUpdateManualMerchantTransaction", "")) {
				return null;
			}
				manualMerchantTransaction = manualMerchantTransactionRepository.save(manualMerchantTransaction);
		} else {
			ArrayList<Integer> merchantTransactionIds = new ArrayList<Integer>();

			ManualMerchantTransaction manualMerchantTransaction1 = manualMerchantTransactionRepository
					.findById(manualMerchantTransactionRequestDto.getMerchantTransactionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.MMT_NOT_FOUND, HttpStatus.NOT_FOUND));

			merchantTransactionIds.add(manualMerchantTransaction1.getMerchantTransactionId());
			if (manualMerchantTransactionRepository.existsByInstitution_InstitutionIdAndTransactionEntity_TransactionIdAndMerchantTransactionIdNotIn(institution.getInstitutionId(), defaultTransactionId.getTransactionId(), merchantTransactionIds)) {
				throw new BusinessException (ResponseCode.TRANSACTION_INSTITUTION_EXISTS, HttpStatus.BAD_REQUEST);
			}

			manualMerchantTransaction1.setUserCreate(
					userDetails != null ? Integer.valueOf(userDetails.getId()).toString() : "system"
			);
			manualMerchantTransaction1.setDateCreate(new Date());
			manualMerchantTransaction1.setComments(manualMerchantTransactionRequestDto.getComments());
			manualMerchantTransaction1.setEntities(entities.getEntityId());
			manualMerchantTransaction1.setEntitiesObject(entities);
			manualMerchantTransaction1.setTransactionEntity(defaultTransactionId);
			if(Objects.nonNull(defaultTransactionId)) {
				manualMerchantTransaction1.setTransaction(defaultTransactionId.getTransactionId());
			} else {
				manualMerchantTransaction1.setTransaction(null);
			}
			manualMerchantTransaction1.setTransactionCurrency(transCurrency);
			manualMerchantTransaction1.setTipsCurrency(tipsCurrency);
			manualMerchantTransaction1.setTerminalEntity(terminal);
			manualMerchantTransaction1.setTerminal(terminal.getTerminalId());
			manualMerchantTransaction1.setInstitution(institution);
			manualMerchantTransaction1.setReasonCode(systemCode);
			manualMerchantTransaction1.setPan(manualMerchantTransactionRequestDto.getCardNumber());
			if (makerCheckerEngine.processIfRequired(manualMerchantTransactionRequestDto, ManualMerchantTransactionService.class.getName(), "saveOrUpdateManualMerchantTransaction", "")) {
				return null;
			}
			manualMerchantTransaction = manualMerchantTransactionRepository.save(manualMerchantTransaction1);
		}

		return manualMerchantTransactionMapper.toDto(manualMerchantTransaction);
	}
	public void deleteManualMerchantTransaction(int id) throws Exception {
		manualMerchantTransactionRepository.findById(id).orElseThrow(
				() -> new BusinessException(ResponseCode.MMT_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(id, ManualMerchantTransactionService.class.getName(), "deleteManualMerchantTransaction", "")) {
			return;
		}
		manualMerchantTransactionRepository.deleteById(id);
	}

	public ResponseEntity<PaginationResponseDto> getManualMerchantTransactionByInstitutionId(
			ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto) {

		Page<ManualMerchantTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualMerchantTransactionRequestDto.getSort(), "merchantTransactionId",
				manualMerchantTransactionRequestDto.getPageNo(), manualMerchantTransactionRequestDto.getPageSize());

		page = manualMerchantTransactionRepository.findByInstitution_InstitutionId(pageRequest,
				manualMerchantTransactionRequestDto.getInstitutionId());

		List<ManualMerchantTransactionResponseDto> allManualMerchantTransactionDto = new ArrayList<ManualMerchantTransactionResponseDto>();

		page.getContent().stream().forEach((transaction) -> {

			ManualMerchantTransactionResponseDto manualMerchantTransactionCodeResponseDto = manualMerchantTransactionMapper
					.toDto(transaction);
			manualMerchantTransactionCodeResponseDto.setPageNo(manualMerchantTransactionRequestDto.getPageNo());
			manualMerchantTransactionCodeResponseDto.setPageSize(manualMerchantTransactionRequestDto.getPageSize());

			allManualMerchantTransactionDto.add(manualMerchantTransactionCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null,
				allManualMerchantTransactionDto, page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

	}

	public ResponseEntity<PaginationResponseDto> getTransactionsBySearch(
			ManualMerchantTransactionRequestDto manualMerchantTransactionRequestDto) {

		Page<ManualMerchantTransaction> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		if (manualMerchantTransactionRequestDto.getInstitutionId() == null ||
				manualMerchantTransactionRequestDto.getOutletId() == null ||
				manualMerchantTransactionRequestDto.getFromTransactionDate() == null ||
				manualMerchantTransactionRequestDto.getToTransactionDate() == null) {
			return new ResponseEntity<>(
					new PaginationResponseDto(true, "Missing mandatory fields", null, 0, 0L),
					HttpStatus.BAD_REQUEST);
		}

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(
				manualMerchantTransactionRequestDto.getSort(), "merchantTransactionId",
				manualMerchantTransactionRequestDto.getPageNo(), manualMerchantTransactionRequestDto.getPageSize());

		List<ManualMerchantTransactionResponseDto> responseDtos = new ArrayList<>();


		if (manualMerchantTransactionRequestDto.getTransactionId() != null &&
				!manualMerchantTransactionRequestDto.getTransactionId().isEmpty()) {

			if (manualMerchantTransactionRequestDto.getTerminalId() != null &&
					!manualMerchantTransactionRequestDto.getTerminalId().equals("0") && !manualMerchantTransactionRequestDto.getTerminalId().equalsIgnoreCase("")) {

				if (manualMerchantTransactionRequestDto.getCardNumber() != null &&
						!manualMerchantTransactionRequestDto.getCardNumber().isEmpty()) {

					// Search by transactionId, terminalId, and cardNumber
					page = manualMerchantTransactionRepository
							.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseAndTransactionDateBetweenAndTransactionEntity_TransactionId(
									pageRequest,
									manualMerchantTransactionRequestDto.getInstitutionId(),
									manualMerchantTransactionRequestDto.getOutletId(),
									manualMerchantTransactionRequestDto.getTerminalId(),
									manualMerchantTransactionRequestDto.getCardNumber(),
									manualMerchantTransactionRequestDto.getFromTransactionDate(),
									manualMerchantTransactionRequestDto.getToTransactionDate(),
									manualMerchantTransactionRequestDto.getTransactionId());
				} else {
					// Search by transactionId and terminalId only
					page = manualMerchantTransactionRepository
							.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndTransactionDateBetweenAndTransactionEntity_TransactionId(
									pageRequest,
									manualMerchantTransactionRequestDto.getInstitutionId(),
									manualMerchantTransactionRequestDto.getOutletId(),
									manualMerchantTransactionRequestDto.getTerminalId(),
									manualMerchantTransactionRequestDto.getFromTransactionDate(),
									manualMerchantTransactionRequestDto.getToTransactionDate(),
									manualMerchantTransactionRequestDto.getTransactionId());
				}
			} else {
				if (manualMerchantTransactionRequestDto.getCardNumber() != null &&
						!manualMerchantTransactionRequestDto.getCardNumber().isEmpty()) {
						// Search by transactionId only
						page = manualMerchantTransactionRepository
								.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetweenAndCardNumberIgnoreCaseAndTransactionEntity_TransactionId(
										pageRequest,
										manualMerchantTransactionRequestDto.getInstitutionId(),
										manualMerchantTransactionRequestDto.getOutletId(),
										manualMerchantTransactionRequestDto.getFromTransactionDate(),
										manualMerchantTransactionRequestDto.getToTransactionDate(),
										manualMerchantTransactionRequestDto.getCardNumber(),
										manualMerchantTransactionRequestDto.getTransactionId());
					} else {
						// Search by transactionId only
						page = manualMerchantTransactionRepository
								.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetweenAndTransactionEntity_TransactionId(
										pageRequest,
										manualMerchantTransactionRequestDto.getInstitutionId(),
										manualMerchantTransactionRequestDto.getOutletId(),
										manualMerchantTransactionRequestDto.getFromTransactionDate(),
										manualMerchantTransactionRequestDto.getToTransactionDate(),
										manualMerchantTransactionRequestDto.getTransactionId());
				}
			}
		} else if (manualMerchantTransactionRequestDto.getTerminalId() != null &&
				!manualMerchantTransactionRequestDto.getTerminalId().equals("0")
				&& !manualMerchantTransactionRequestDto.getTerminalId().isEmpty()) {

			if (manualMerchantTransactionRequestDto.getCardNumber() != null &&
					!manualMerchantTransactionRequestDto.getCardNumber().isEmpty()) {

				// Search by terminalId and cardNumber
				page = manualMerchantTransactionRepository
						.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndCardNumberIgnoreCaseAndTransactionDateBetween(
								pageRequest,
								manualMerchantTransactionRequestDto.getInstitutionId(),
								manualMerchantTransactionRequestDto.getOutletId(),
								manualMerchantTransactionRequestDto.getTerminalId(),
								manualMerchantTransactionRequestDto.getCardNumber(),
								manualMerchantTransactionRequestDto.getFromTransactionDate(),
								manualMerchantTransactionRequestDto.getToTransactionDate());
			} else {
				// Search by terminalId only
				page = manualMerchantTransactionRepository
						.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTerminalEntity_TerminalIdAndTransactionDateBetween(
								pageRequest,
								manualMerchantTransactionRequestDto.getInstitutionId(),
								manualMerchantTransactionRequestDto.getOutletId(),
								manualMerchantTransactionRequestDto.getTerminalId(),
								manualMerchantTransactionRequestDto.getFromTransactionDate(),
								manualMerchantTransactionRequestDto.getToTransactionDate());
			}
		} else if (manualMerchantTransactionRequestDto.getCardNumber() != null &&
				!manualMerchantTransactionRequestDto.getCardNumber().isEmpty()) {

			// Search by cardNumber only
			page = manualMerchantTransactionRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndCardNumberIgnoreCaseAndTransactionDateBetween(
							pageRequest,
							manualMerchantTransactionRequestDto.getInstitutionId(),
							manualMerchantTransactionRequestDto.getOutletId(),
							manualMerchantTransactionRequestDto.getCardNumber(),
							manualMerchantTransactionRequestDto.getFromTransactionDate(),
							manualMerchantTransactionRequestDto.getToTransactionDate());
		} else {
			// Base case: search by institutionId, outletId, and transactionDate
			page = manualMerchantTransactionRepository
					.findByInstitution_InstitutionIdAndEntitiesObject_EntityIdAndTransactionDateBetween(
							pageRequest,
							manualMerchantTransactionRequestDto.getInstitutionId(),
							manualMerchantTransactionRequestDto.getOutletId(),
							manualMerchantTransactionRequestDto.getFromTransactionDate(),
							manualMerchantTransactionRequestDto.getToTransactionDate());
		}

		page.getContent().forEach((transaction) -> {

			ManualMerchantTransactionResponseDto manualMerchantTransactionCodeResponseDto = manualMerchantTransactionMapper
					.toDto(transaction);
			manualMerchantTransactionCodeResponseDto.setPageNo(manualMerchantTransactionRequestDto.getPageNo());
			manualMerchantTransactionCodeResponseDto.setPageSize(manualMerchantTransactionRequestDto.getPageSize());

			responseDtos.add(manualMerchantTransactionCodeResponseDto);
		});

		return new ResponseEntity<>(
				new PaginationResponseDto(true, null, responseDtos, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

}
