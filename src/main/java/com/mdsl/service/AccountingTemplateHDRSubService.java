package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.AccountingTemplateHDRSubRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRSubResponseDto;
import com.mdsl.model.entity.AccountingTemplateHDR;
import com.mdsl.model.entity.AccountingTemplateHDRSub;
import com.mdsl.model.mapper.AccountingTemplateHDRSubMapper;
import com.mdsl.repository.AccountingTemplateDetailsRepository;
import com.mdsl.repository.AccountingTemplateHDRRepository;
import com.mdsl.repository.AccountingTemplateHDRSubRepository;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountingTemplateHDRSubService {
	
	private final AccountingTemplateHDRSubRepository accountingTemplateHDRSubRepository;
	private final AccountingTemplateHDRRepository accountingTemplateHDRRepository;
	private final AccountingTemplateDetailsRepository accountingTemplateDetailsRepository;
	private final InstitutionRepository institutionRepository;
	private final BankCodeRepository bankCodeRepository;
	
	private final AccountingTemplateHDRSubMapper accountingTemplateHDRSubMapper;
	
	public List<AccountingTemplateHDRSubResponseDto> getAllAccountingTemplateHDRSubByAccrTemplateHdrId(Integer acctTemplateHdrId) {
		List<AccountingTemplateHDRSubResponseDto> allAccountingTemplateHDRSubResponseDtos = new ArrayList<AccountingTemplateHDRSubResponseDto>();
		AccountingTemplateHDR accountingTemplateHDR = this.accountingTemplateHDRRepository.findById(acctTemplateHdrId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE, HttpStatus.NOT_FOUND));
		List<AccountingTemplateHDRSub> allAccountingTemplateHDRSub = this.accountingTemplateHDRSubRepository.findByAcctTemplateHdrId(accountingTemplateHDR.getAcctTemplateHdrId());
		
		allAccountingTemplateHDRSub.stream().forEach((accountingTemplateHDRSub) -> {
			allAccountingTemplateHDRSubResponseDtos.add(accountingTemplateHDRSubMapper.toDto(accountingTemplateHDRSub));
		});
		
		return allAccountingTemplateHDRSubResponseDtos;
	}
	
	public AccountingTemplateHDRSubResponseDto getAccountingTemplateHDRSubById(int id) {
        AccountingTemplateHDRSub accountingTemplateHDRSub = this.accountingTemplateHDRSubRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB, HttpStatus.NOT_FOUND));
        return this.accountingTemplateHDRSubMapper.toDto(accountingTemplateHDRSub);
    }

    public AccountingTemplateHDRSubResponseDto saveAccountingTemplateHDRSub(AccountingTemplateHDRSubRequestDto accountingTemplateHDRSubRequestDto) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            String institutionId = accountingTemplateHDRSubRequestDto.getInstitutionId().trim();

            if (this.institutionRepository.findById(institutionId).isEmpty()) {
                throw new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.BAD_REQUEST);
            }

            if (this.accountingTemplateHDRRepository.findById(accountingTemplateHDRSubRequestDto.getAcctTemplateHdrId()).isEmpty()) {
                throw new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE, HttpStatus.BAD_REQUEST);
            }

            if (!"DEFAULT".equals(accountingTemplateHDRSubRequestDto.getBankCode())) {
                boolean bankExists = this.bankCodeRepository
                        .findByBankCodeAndInstitutionId(accountingTemplateHDRSubRequestDto.getBankCode(), institutionId)
                        .isPresent();

                if (!bankExists) {
                    throw new BusinessException(ResponseCode.CFG_INVALID_BANK_CODE, HttpStatus.BAD_REQUEST);
                }
            }
            Optional<AccountingTemplateHDRSub> requestAccountingTemplateHDRSub =
                    this.accountingTemplateHDRSubRepository.findById(accountingTemplateHDRSubRequestDto.getAcctTemplateHdrSubId());
            AccountingTemplateHDRSub entityToSave;
            AccountingTemplateHDRSub savedEntity;
            if (requestAccountingTemplateHDRSub.isPresent()) {
                AccountingTemplateHDRSub existing = requestAccountingTemplateHDRSub.get();
//                if (this.accountingTemplateHDRSubRepository
//                        .existsByInstitutionIdAndBankCodeAndAcctTemplateHdrSubIdNot(
//                                institutionId,
//                                accountingTemplateHDRSubRequestDto.getBankCode(),
//                                accountingTemplateHDRSubRequestDto.getAcctTemplateHdrSubId())) {
//                    throw new BusinessException(ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_SUB_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
//                }
                entityToSave = this.accountingTemplateHDRSubMapper.toEntity(accountingTemplateHDRSubRequestDto);
                if ("DEFAULT".equals(accountingTemplateHDRSubRequestDto.getBankCode())) {
                    entityToSave.setBankCode("");
                }
                entityToSave.setCreatedBy(existing.getCreatedBy());
                entityToSave.setCreatedDate(existing.getCreatedDate());
                entityToSave.setUpdatedBy(Integer.valueOf(userDetails.getId()));
                entityToSave.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                savedEntity = this.accountingTemplateHDRSubRepository.save(entityToSave);
            } else {
                if (this.accountingTemplateHDRSubRepository.existsByInstitutionIdAndBankCodeAndAcctTemplateHdrId(
                        institutionId,
                        accountingTemplateHDRSubRequestDto.getBankCode(),
                        accountingTemplateHDRSubRequestDto.getAcctTemplateHdrId())) {
                    throw new BusinessException(ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_SUB_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }

                entityToSave = this.accountingTemplateHDRSubMapper.toEntity(accountingTemplateHDRSubRequestDto);

                if ("DEFAULT".equals(accountingTemplateHDRSubRequestDto.getBankCode())) {
                    entityToSave.setBankCode("");
                    if (this.accountingTemplateHDRSubRepository.existsByInstitutionIdAndBankCode(institutionId, null)) {
                        throw new BusinessException(ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_SUB_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                    }
                }
                entityToSave.setCreatedBy(Integer.valueOf(userDetails.getId()));
                entityToSave.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                savedEntity = this.accountingTemplateHDRSubRepository.save(entityToSave);
            }
            return this.accountingTemplateHDRSubMapper.toDto(savedEntity);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    ResponseCode.VAL_ERROR_OCCURRED,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void deleteAccountingTemplateSubHDR(int id) {
        try {
            AccountingTemplateHDRSub accountingTemplateHDRSub = accountingTemplateHDRSubRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB,
                            HttpStatus.NOT_FOUND
                    ));
            accountingTemplateDetailsRepository.deleteByAcctTemplateHdrSubId(accountingTemplateHDRSub.getAcctTemplateHdrSubId());
            accountingTemplateHDRSubRepository.delete(accountingTemplateHDRSub);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    ResponseCode.VAL_ERROR_OCCURRED,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}