package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.AccountingTemplateHDRRequestDto;
import com.mdsl.model.dto.request.AccountingTemplateHdrEntityMappingRequestDto;
import com.mdsl.model.dto.response.AccountingTemplateHDRResponseDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.entity.AccountingTemplateHDR;
import com.mdsl.model.entity.AccountingTemplateHDRSub;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.AccountingTemplateHDRMapper;
import com.mdsl.model.mapper.EntityMapper;
import com.mdsl.repository.AccountingTemplateHDRRepository;
import com.mdsl.repository.AccountingTemplateHDRSubRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountingTemplateHDRService
{
    private final AccountingTemplateHDRRepository accountingTemplateHdrRepository;
    private final InstitutionRepository institutionRepository;
    private final AccountingTemplateHDRSubRepository accountingTemplateHDRSubRepository;
    private final EntitiesRepository entitiesRepository;
    
    private final AccountingTemplateHDRMapper accountingTemplateHdrMapper;
    private final EntityMapper entityMapper;
    
    private final AccountingTemplateHDRSubService accountingTemplateHDRSubService;
    
    public List<AccountingTemplateHDRResponseDto> getAllAccountingTemplateHDRsByInstitution(final String institutionId) {
        List<AccountingTemplateHDRResponseDto> accountingTemplateHDRResponseDtos = new ArrayList<AccountingTemplateHDRResponseDto>();
        Institution institution = this.institutionRepository.findById(institutionId.trim()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<AccountingTemplateHDR> accountingTemplateHDRs = this.accountingTemplateHdrRepository.findByInstitutionId(institution.getInstitutionId());
        accountingTemplateHDRs.stream().forEach(accountingTemplateHDR -> accountingTemplateHDRResponseDtos.add(this.accountingTemplateHdrMapper.toDto(accountingTemplateHDR)));
        return accountingTemplateHDRResponseDtos;
    }
    
    public AccountingTemplateHDRResponseDto getAccountingTemplateHDRById(final int id) {
        AccountingTemplateHDR accountingTemplateHDR = this.accountingTemplateHdrRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCOUNT_TEMPLATE, HttpStatus.NOT_FOUND));
        return this.accountingTemplateHdrMapper.toDto(accountingTemplateHDR);
    }
    
    public AccountingTemplateHDRResponseDto saveAccountingTemplateHDR(final AccountingTemplateHDRRequestDto accountingTemplateHDRRequestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<AccountingTemplateHDR> requestAccountingTemplateHDR = this.accountingTemplateHdrRepository.findById(accountingTemplateHDRRequestDto.getAcctTemplateHdrId());
        if (this.institutionRepository.findById(accountingTemplateHDRRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        accountingTemplateHDRRequestDto.setAccountTemplate(accountingTemplateHDRRequestDto.getAccountTemplate().trim().toUpperCase());
        AccountingTemplateHDR savedAccountingTemplateHDR;
        if (requestAccountingTemplateHDR.isPresent()) { //Case of Update
            if (this.accountingTemplateHdrRepository.existsByInstitutionIdAndAccountTemplateAndAcctTemplateHdrIdNot(accountingTemplateHDRRequestDto.getInstitutionId(), accountingTemplateHDRRequestDto.getAccountTemplate(), accountingTemplateHDRRequestDto.getAcctTemplateHdrId())) {
                throw new BusinessException(ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            AccountingTemplateHDR saveAccountingTemplateHDR = this.accountingTemplateHdrMapper.toEntity(accountingTemplateHDRRequestDto);
            saveAccountingTemplateHDR.setCreatedBy(requestAccountingTemplateHDR.get().getCreatedBy());
            saveAccountingTemplateHDR.setCreatedDate(requestAccountingTemplateHDR.get().getCreatedDate());
            saveAccountingTemplateHDR.setUpdatedBy(Integer.valueOf(userDetails.getId()));
            saveAccountingTemplateHDR.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            savedAccountingTemplateHDR = (AccountingTemplateHDR)this.accountingTemplateHdrRepository.save(saveAccountingTemplateHDR);
        } else {//Case of create
            if (this.accountingTemplateHdrRepository.existsByInstitutionIdAndAccountTemplate(accountingTemplateHDRRequestDto.getInstitutionId(), accountingTemplateHDRRequestDto.getAccountTemplate())) {
                throw new BusinessException(ResponseCode.CFG_ACCOUNTING_TEMPLATE_HDR_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            AccountingTemplateHDR saveAccountingTemplateHDR = this.accountingTemplateHdrMapper.toEntity(accountingTemplateHDRRequestDto);
            saveAccountingTemplateHDR.setCreatedBy(Integer.valueOf(userDetails.getId()));
            saveAccountingTemplateHDR.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            savedAccountingTemplateHDR = (AccountingTemplateHDR)this.accountingTemplateHdrRepository.save(saveAccountingTemplateHDR);
        }
        return this.accountingTemplateHdrMapper.toDto(savedAccountingTemplateHDR);
    }
    
    public void deleteAccountingTemplateHDR(int id) {
        AccountingTemplateHDR accountingTemplateHDR = this.accountingTemplateHdrRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACCOUNT_TEMPLATE_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        List<AccountingTemplateHDRSub> accountingTemplateHDRSubs = this.accountingTemplateHDRSubRepository.findByAcctTemplateHdrId(accountingTemplateHDR.getAcctTemplateHdrId());
        
        accountingTemplateHDRSubs.forEach((subHdr) -> {
			this.accountingTemplateHDRSubService.deleteAccountingTemplateSubHDR(subHdr.getAcctTemplateHdrSubId());
		});
        
        this.accountingTemplateHdrRepository.delete(accountingTemplateHDR);
    }
    
    public AccountingTemplateHdrEntityMappingRequestDto mapAccountingTemplateWithEntity(AccountingTemplateHdrEntityMappingRequestDto requestDto) {
        AccountingTemplateHDR accountingTemplateHDR = this.accountingTemplateHdrRepository.findById(requestDto.getId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACCOUNT_TEMPLATE_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<String> listofEntityRequestDto = requestDto.getEntities();
        List<Entities> oldENtityAssign = this.entitiesRepository.findByAcctTemplateHdrIdAndInstitution_InstitutionId((int)accountingTemplateHDR.getAcctTemplateHdrId(),accountingTemplateHDR.getInstitutionId());
        if (!oldENtityAssign.isEmpty() && oldENtityAssign.size() != 0) {
            List<String> oldEntityList = oldENtityAssign.stream().map(Entities::getEntityId).collect(Collectors.toList());
            listofEntityRequestDto = listofEntityRequestDto.stream().filter(e -> !oldEntityList.contains(e)).collect(Collectors.toList());
            List<String> entityIds = oldENtityAssign.stream().map(Entities::getEntityId).collect(Collectors.toList());
			entityIds.removeAll(requestDto.getEntities());
			entityIds.forEach((id) -> {
				Entities entities = entitiesRepository.findByEntityIdAndInstitution_InstitutionId(id,accountingTemplateHDR.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
				entities.setAcctTemplateHdrId(null);
				entitiesRepository.save(entities);
			});
		}
        
        listofEntityRequestDto.forEach((id) -> {
			Entities entities = entitiesRepository.findByEntityIdAndInstitution_InstitutionId(String.valueOf(id),accountingTemplateHDR.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
			entities.setAcctTemplateHdrId(accountingTemplateHDR.getAcctTemplateHdrId());
			entitiesRepository.save(entities);
		});

		return requestDto;
    }
    
    public List<EntitiesResponseDto> getMappedEntitiesByAccountingTemplateId(int id) {
        List<EntitiesResponseDto> responseDtos = new ArrayList<EntitiesResponseDto>();
        AccountingTemplateHDR accountingTemplateHDR = this.accountingTemplateHdrRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACCOUNT_TEMPLATE_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<Entities> entities = this.entitiesRepository.findByAcctTemplateHdrIdAndInstitution_InstitutionId(accountingTemplateHDR.getAcctTemplateHdrId(),accountingTemplateHDR.getInstitutionId());
        entities.stream().forEach(ent -> {
        	responseDtos.add(this.entityMapper.toDto(ent));
        });
        return responseDtos;
    }
}