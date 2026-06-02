package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaymentAccountRequestDto;
import com.mdsl.model.dto.response.PaymentAccountResponseDto;
import com.mdsl.model.entity.BankCode;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.PaymentAccount;
import com.mdsl.model.mapper.PaymentAccountMapper;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.PaymentAccountRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentAccountService {

	@Autowired
	private PaymentAccountMapper paymentAccountMapper;

	@Autowired
	private PaymentAccountRepository paymentAccountRepository;

	@Autowired
	private BankCodeRepository bankCodeRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private EntitiesRepository entityRepository;
    private final MakerCheckerEngine makerCheckerEngine;

	public List<PaymentAccountResponseDto> fetchAllPaymentAccount() {
		List<PaymentAccount> paymentAccount = paymentAccountRepository
				.findAll(Sort.by(Sort.Direction.ASC, "paymentAccountId"));
		List<PaymentAccountResponseDto> dto = new ArrayList<PaymentAccountResponseDto>();

		for (PaymentAccount temp : paymentAccount) {
			PaymentAccountResponseDto dtoTemp = paymentAccountMapper.toDto(temp);
			dto.add(dtoTemp);
		}
		return dto;
	}

	public PaymentAccountResponseDto fetchPaymentAccountById(int id) {
		PaymentAccount paymentAccount = paymentAccountRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.PAY_PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
		PaymentAccountResponseDto dto = paymentAccountMapper.toDto(paymentAccount);
		return dto;
	}

	public List<PaymentAccountResponseDto> getPaymentAccountByEntitiesId(String entitiesId) {

		List<PaymentAccountResponseDto> paymentAccountResponseDtos = new ArrayList<PaymentAccountResponseDto>();
		List<PaymentAccount> paymentAccounts = paymentAccountRepository.findPaymentAccountsByEntityId(entitiesId);

		paymentAccounts.stream().forEach((paymentAccount) -> {
			PaymentAccountResponseDto paymentAccountResponseDto = paymentAccountMapper.toDto(paymentAccount);
			paymentAccountResponseDtos.add(paymentAccountResponseDto);
		});

		return paymentAccountResponseDtos;
	}

	public PaymentAccountResponseDto saveOrUpdatePaymentAccount(
			@Valid PaymentAccountRequestDto paymentAccountRequestDto) {
		PaymentAccount paymentAccount;
		PaymentAccount finalList;
		Currency transferCurrency = null;
		Currency settlementCurrency = null;

		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		BankCode bankCode = bankCodeRepository.findById(paymentAccountRequestDto.getBankCodeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (paymentAccountRequestDto.getTransferCurrencyId() != 0) {
			transferCurrency = currencyRepository.findById(paymentAccountRequestDto.getTransferCurrencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
		}

		if (paymentAccountRequestDto.getSettlementCurrencyId() != 0) {
			settlementCurrency = currencyRepository.findById(paymentAccountRequestDto.getSettlementCurrencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
		}

		Institution institution = institutionRepository.findById(paymentAccountRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		Entities entity = entityRepository.findByEntityIdAndInstitution(paymentAccountRequestDto.getEntityId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (paymentAccountRequestDto.getPaymentAccountId() == 0) {
			paymentAccount = paymentAccountMapper.toEntity(paymentAccountRequestDto);

			paymentAccount.setBankCode(bankCode.getBankCode());
			paymentAccount.setBankCodeEntity(bankCode);

			paymentAccount.setTransferCurrency(transferCurrency);
			paymentAccount.setBeneficiaryName(paymentAccountRequestDto.getBeneficiaryName());
			paymentAccount.setSettlementCurrency(settlementCurrency);
			paymentAccount.setInstitution(institution);
			
			paymentAccount.setEntityObject(entity);
			paymentAccount.setEntity(entity.getEntityId());

			if(userDetails!=null) {
				paymentAccount.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
			}
			paymentAccount.setCreatedDate(new Date());
		} else {
			paymentAccount = paymentAccountRepository.findById(paymentAccountRequestDto.getPaymentAccountId())
					.orElseThrow(() -> new BusinessException(ResponseCode.PAY_PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));

			paymentAccount.setAccountNumber(paymentAccountRequestDto.getAccountNumber());
			paymentAccount.setIban(paymentAccountRequestDto.getIban());
			paymentAccount.setCurrencyMarkup(paymentAccountRequestDto.getCurrencyMarkup());
			paymentAccount.setBranch(paymentAccountRequestDto.getBranch());
			paymentAccount.setBeneficiaryName(paymentAccountRequestDto.getBeneficiaryName());

			paymentAccount.setBankCode(bankCode.getBankCode());
			paymentAccount.setBankCodeEntity(bankCode);
			
			paymentAccount.setTransferCurrency(transferCurrency);
			paymentAccount.setSettlementCurrency(settlementCurrency);
			paymentAccount.setInstitution(institution);
			paymentAccount.setEntity(entity.getEntityId());
			paymentAccount.setEntityObject(entity);

		}
   		if (makerCheckerEngine.processIfRequired(paymentAccountRequestDto, PaymentAccountService.class.getName(), "saveOrUpdatePaymentAccount", "")) {
			return null;
		}
		finalList = paymentAccountRepository.save(paymentAccount);
        return paymentAccountMapper.toDto(finalList);
	}

	public void deletePaymentAccountById(int id) throws Exception {
		paymentAccountRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.PAY_PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
   		if (makerCheckerEngine.processIfRequired(id, PaymentAccountService.class.getName(), "deletePaymentAccountById", "")) {
			return;
		}
		paymentAccountRepository.deleteById(id);

	}

	public List<PaymentAccountResponseDto> fetchPaymentAccountByInstitutionId(String instId) {
		List<PaymentAccount> paymentAccount = paymentAccountRepository.findPaymentAccountByInstitutionId(instId,
				Sort.by(Sort.Direction.ASC, "paymentAccountId"));
		List<PaymentAccountResponseDto> dto = new ArrayList<PaymentAccountResponseDto>();

		for (PaymentAccount temp : paymentAccount) {
			PaymentAccountResponseDto dtoTemp = paymentAccountMapper.toDto(temp);
			dto.add(dtoTemp);
		}
		return dto;
	}

}
