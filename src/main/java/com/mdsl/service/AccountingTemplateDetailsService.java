package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.AccountingTemplateDetailsRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateDetailsResponseDto;
import com.mdsl.model.entity.AccountingTemplateDetails;
import com.mdsl.model.entity.AccountingTemplateHDRSub;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.AccountingTemplateDetailsMapper;
import com.mdsl.repository.AccountingTemplateDetailsRepository;
import com.mdsl.repository.AccountingTemplateHDRSubRepository;
import com.mdsl.repository.DefaultTransactionIdRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountingTemplateDetailsService
{
    private final AccountingTemplateDetailsRepository accountingTemplateDetailsRepository;
    private final AccountingTemplateHDRSubRepository accountingTemplateHdrSubRepository;
    private final InstitutionRepository institutionRepository;
    private final DefaultTransactionIdRepository defaultTransactionIdRepository;
    private final AccountingTemplateDetailsMapper accountingTemplateDetailsMapper;

    public List<AccountingTemplateDetailsResponseDto> getAllAccountingTemplateDetailsByAccountingTemplateHDRSubId(final int id) {
        try {
            AccountingTemplateHDRSub accountingTemplateHdrSub = accountingTemplateHdrSubRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE_SUB, HttpStatus.NOT_FOUND));

            List<AccountingTemplateDetails> allAccountingTemplateDetails = accountingTemplateDetailsRepository.findByAcctTemplateHdrSubIdAndInstitutionId(accountingTemplateHdrSub.getAcctTemplateHdrSubId(),accountingTemplateHdrSub.getInstitutionId(),Sort.by(Sort.Direction.ASC, "acctTemplateDtlId"));

            List<AccountingTemplateDetailsResponseDto> responseList = new ArrayList<>();
            for (AccountingTemplateDetails detail : allAccountingTemplateDetails) {
                Institution destinationInstitution = null;
                if (Objects.nonNull(detail.getDestinationInstitution()) && !detail.getDestinationInstitution().isEmpty()) {
                    destinationInstitution = institutionRepository.findById(detail.getDestinationInstitution())
                            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
                }

                AccountingTemplateDetailsResponseDto dto = accountingTemplateDetailsMapper.toDto(detail);
                dto.setDestinationInstitutionName(destinationInstitution != null ? destinationInstitution.getInstitutionName() : null);
                responseList.add(dto);
            }

            return responseList;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException("Failed to fetch accounting template details for HDRSub ID " + id + ": " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AccountingTemplateDetailsResponseDto getAccountTemplateDetailsById(final int id) {
        try {
            AccountingTemplateDetails accountingTemplateDetails = accountingTemplateDetailsRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(
                            ResponseCode.CFG_INVALID_ACCOUNTING_TEMPLATE_DTL, HttpStatus.NOT_FOUND));

            Institution destinationInstitution = null;
            if (Objects.nonNull(accountingTemplateDetails.getDestinationInstitution())
                    && !accountingTemplateDetails.getDestinationInstitution().isEmpty()) {
                destinationInstitution = institutionRepository.findById(accountingTemplateDetails.getDestinationInstitution())
                        .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
            }

            AccountingTemplateDetailsResponseDto dto = accountingTemplateDetailsMapper.toDto(accountingTemplateDetails);
            dto.setDestinationInstitutionName(destinationInstitution != null ? destinationInstitution.getInstitutionName() : null);
            if (Objects.isNull(dto.getAccountType())) {
                dto.setAccountType("");
            }
            return dto;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    "Failed to fetch accounting template detail for ID " + id + ": " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AccountingTemplateDetailsResponseDto saveAccountingTemplateDetails(final AccountingTemplateDetailsRequestDto dto) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<AccountingTemplateDetails> existingDetails = accountingTemplateDetailsRepository.findById(dto.getAcctTemplateDtlId());

            Institution institution = institutionRepository.findById(dto.getInstitutionId().trim())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.BAD_REQUEST));

            if (defaultTransactionIdRepository.findByTransactionIdAndInstitution(dto.getTransId(),institution).isEmpty()) {
                throw new BusinessException(ResponseCode.CFG_INVALID_TRANS_ID, HttpStatus.BAD_REQUEST);
            }

            if (accountingTemplateHdrSubRepository.findById(dto.getAcctTemplateHdrSubId()).isEmpty()) {
                throw new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE, HttpStatus.BAD_REQUEST);
            }

            Institution destinationInstitution = null;
            if (dto.getDestinationInstitution() != null && !dto.getDestinationInstitution().isEmpty()) {
                destinationInstitution = institutionRepository.findById(dto.getDestinationInstitution())
                        .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));
            }

            AccountingTemplateDetails entityToSave = accountingTemplateDetailsMapper.toEntity(dto);

            if (existingDetails.isPresent()) {
                AccountingTemplateDetails existing = existingDetails.get();
                entityToSave.setCreatedBy(existing.getCreatedBy());
                entityToSave.setCreatedDate(existing.getCreatedDate());
                entityToSave.setUpdatedBy(Integer.valueOf(userDetails.getId()));
                entityToSave.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            } else {
                entityToSave.setCreatedBy(Integer.valueOf(userDetails.getId()));
                entityToSave.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            }

            entityToSave.setDestinationInstitution(destinationInstitution != null ? destinationInstitution.getInstitutionId() : null);

            if (Objects.isNull(entityToSave.getShow())) {
                entityToSave.setShow(0);
            }

            AccountingTemplateDetails saved = accountingTemplateDetailsRepository.save(entityToSave);

            AccountingTemplateDetailsResponseDto responseDto = accountingTemplateDetailsMapper.toDto(saved);
            responseDto.setDestinationInstitutionName(destinationInstitution != null ? destinationInstitution.getInstitutionName() : null);

            return responseDto;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    ResponseCode.CFG_ERROR_OCCURRED,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public void deleteAccountingTemplateDetails(final int id) {
        AccountingTemplateDetails accountingTemplateDetails = this.accountingTemplateDetailsRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCOUNTING_TEMPLATE_DTL, HttpStatus.NOT_FOUND));
        this.accountingTemplateDetailsRepository.delete(accountingTemplateDetails);
    }
}