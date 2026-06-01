package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.InstitutionAccountsAccountTypeRequestDto;
import com.mdsl.model.dto.request.InstitutionAccountsGetRequestDto;
import com.mdsl.model.dto.request.InstitutionAccountsRequestDto;
import com.mdsl.model.dto.response.InstitutionAccountsResponseDto;
import com.mdsl.model.entity.BankCode;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.InstitutionAccounts;
import com.mdsl.model.mapper.InstitutionAccountsMapper;
import com.mdsl.repository.BankCodeRepository;
import com.mdsl.repository.CardSchemeRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.InstitutionAccountsRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.IssuerProfileRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstitutionAccountsService
{
    private final InstitutionAccountsRepository institutionAccountsRepository;
    private final InstitutionRepository institutionRepository;
    private final IssuerProfileRepository issuerProfileRepository;
    private final CardSchemeRepository cardSchemeRepository;
    private final BankCodeRepository bankCodeRepository;
    private final CurrencyRepository currencyRepository;
    private final InstitutionAccountsMapper institutionAccountsMapper;
    
    public List<InstitutionAccountsResponseDto> getAllInstitutionAccountsByInstitution(final String institutionId) {
        List<InstitutionAccountsResponseDto> institutionAccountsResponseDtos = new ArrayList<InstitutionAccountsResponseDto>();
        Institution institution = this.institutionRepository.findById(institutionId.trim()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        List<InstitutionAccounts> institutionAccounts = this.institutionAccountsRepository.findByInstitutionId(institution.getInstitutionId());
        institutionAccounts.stream().forEach((institutionAccount) -> {
        	Currency currency = this.currencyRepository.findByCurrencyCode(institutionAccount.getCurrencyCode()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
        	InstitutionAccountsResponseDto institutionAccountsResponseDto = this.institutionAccountsMapper.toDto(institutionAccount);
        	institutionAccountsResponseDto.setCurrencyName(currency.getCurrencyName());
        	if(!Objects.isNull(institutionAccount.getChargingInstitution())) {
        		Institution chargingInstitutions = this.institutionRepository.findById(institutionAccount.getChargingInstitution()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
            	institutionAccountsResponseDto.setChargingInstitutionName(chargingInstitutions.getInstitutionName());
        	}
        	institutionAccountsResponseDtos.add(institutionAccountsResponseDto);
		});
        return institutionAccountsResponseDtos;
    }
    
    public List<InstitutionAccountsResponseDto> getAllInstitutionAccountsByInstitutionChargingInstitutionAndBankCode(InstitutionAccountsGetRequestDto institutionAccountsGetRequestDto) {
        List<InstitutionAccountsResponseDto> institutionAccountsResponseDtos = new ArrayList<InstitutionAccountsResponseDto>();
        List<InstitutionAccounts> institutionAccounts;
        Institution chargingInstitution = new Institution();
        
        Institution institution = this.institutionRepository.findById(institutionAccountsGetRequestDto.getInstitutionId().trim())
        		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        if(!institutionAccountsGetRequestDto.getChargingInstitution().equals("")) {
        	chargingInstitution = this.institutionRepository.findById(institutionAccountsGetRequestDto.getChargingInstitution().trim())
            		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        }
        
        if(!institutionAccountsGetRequestDto.getBankCode().equals("DEFAULT")) {
        	BankCode bankCode = this.bankCodeRepository.findByBankCodeAndInstitutionId(institutionAccountsGetRequestDto.getBankCode(), institution.getInstitutionId())
            		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
        	institutionAccounts = this.institutionAccountsRepository.findByInstitutionIdAndChargingInstitutionAndBankCode(institution.getInstitutionId(),
            		chargingInstitution.getInstitutionId(), bankCode.getBankCode());
        }
        else {
        	institutionAccounts = this.institutionAccountsRepository.findByInstitutionIdAndChargingInstitution(institution.getInstitutionId(),
            		chargingInstitution.getInstitutionId());
        }
        
        institutionAccounts.stream().forEach((institutionAccount) -> {
        	Currency currency = this.currencyRepository.findByCurrencyCode(institutionAccount.getCurrencyCode()).orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
        	InstitutionAccountsResponseDto institutionAccountsResponseDto = this.institutionAccountsMapper.toDto(institutionAccount);
        	institutionAccountsResponseDto.setCurrencyName(currency.getCurrencyName());
        	if(!Objects.isNull(institutionAccount.getChargingInstitution())) {
        		Institution chargingInstitutions = this.institutionRepository.findById(institutionAccount.getChargingInstitution()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
            	institutionAccountsResponseDto.setChargingInstitutionName(chargingInstitutions.getInstitutionName());
        	}
        	institutionAccountsResponseDtos.add(institutionAccountsResponseDto);
		});
        return institutionAccountsResponseDtos;
    }
    
    public List<String> getDistinctAccountTypes(InstitutionAccountsAccountTypeRequestDto institutionAccountsAccountTypeRequestDto) {
    	List<String> accountTypes = new ArrayList<String>();
    	Institution chargingInstitution = new Institution();
        Institution institution = this.institutionRepository.findById(institutionAccountsAccountTypeRequestDto.getInstitutionId().trim())
        		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        
        if(!institutionAccountsAccountTypeRequestDto.getChargingInstitution().equals("")) {
        	chargingInstitution = this.institutionRepository.findById(institutionAccountsAccountTypeRequestDto.getChargingInstitution().trim())
            		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        }

    	if(!institutionAccountsAccountTypeRequestDto.getBankCode().equals("DEFAULT")) {
        	BankCode bankCode = this.bankCodeRepository.findByBankCodeAndInstitutionId(institutionAccountsAccountTypeRequestDto.getBankCode(), institution.getInstitutionId())
            		.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
        	accountTypes = this.institutionAccountsRepository.findBySearchCriteria(institution.getInstitutionId(), institutionAccountsAccountTypeRequestDto.getAccountOrigin(), chargingInstitution.getInstitutionId(), bankCode.getBankCode());
        }
        else {
        	accountTypes = this.institutionAccountsRepository.findBySearchCriteria(institution.getInstitutionId(), institutionAccountsAccountTypeRequestDto.getAccountOrigin(), chargingInstitution.getInstitutionId(), "");
        }
        
        return accountTypes;
    }
    
    public InstitutionAccountsResponseDto getInstitutionAccountsById(final int id) {
        InstitutionAccounts institutionAccounts = this.institutionAccountsRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_ACCOUNTS_NOT_FOUND, HttpStatus.NOT_FOUND));
        Currency currency = this.currencyRepository.findByCurrencyCode(institutionAccounts.getCurrencyCode()).orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
    	InstitutionAccountsResponseDto institutionAccountsResponseDto = this.institutionAccountsMapper.toDto(institutionAccounts);
    	institutionAccountsResponseDto.setCurrencyName(currency.getCurrencyName());
    	if(!Objects.isNull(institutionAccounts.getChargingInstitution())) {
    		Institution chargingInstitution = this.institutionRepository.findById(institutionAccounts.getChargingInstitution()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        	institutionAccountsResponseDto.setChargingInstitutionName(chargingInstitution.getInstitutionName());
    	}
    	return institutionAccountsResponseDto;
    }
    
    public InstitutionAccountsResponseDto saveInstitutionAccounts(final InstitutionAccountsRequestDto institutionAccountsRequestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<InstitutionAccounts> requestInstitutionAccounts = this.institutionAccountsRepository.findById(institutionAccountsRequestDto.getInstitutionAcctsId());
        if (this.institutionRepository.findById(institutionAccountsRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.issuerProfileRepository.findByIssuerAcqProfileAndInstitutionId(institutionAccountsRequestDto.getIssuerAcqProfile().trim(),institutionAccountsRequestDto.getInstitutionId()).isEmpty()) {
            throw new BusinessException(ResponseCode.ISS_ISSUER_ACQ_PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.bankCodeRepository.findByBankCodeAndInstitutionId(institutionAccountsRequestDto.getBankCode().trim(), institutionAccountsRequestDto.getInstitutionId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.cardSchemeRepository.findById(institutionAccountsRequestDto.getCardSchemeId().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_CARDSCHEME_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (this.currencyRepository.findByCurrencyCode(institutionAccountsRequestDto.getCurrencyCode().trim()).isEmpty()) {
            throw new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        
        InstitutionAccounts savedInstitutionAccounts;
        if (requestInstitutionAccounts.isPresent()) { //Case of update
            if (this.institutionAccountsRepository.existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndBankCodeAndAccountOriginAndChargingInstitutionAndInstitutionAcctsIdNot(
                    institutionAccountsRequestDto.getInstitutionId(),
                    institutionAccountsRequestDto.getAccountType(),
                    institutionAccountsRequestDto.getCardSchemeId(),
                    institutionAccountsRequestDto.getIssuerAcqProfile(),
                    institutionAccountsRequestDto.getCurrencyCode(),
                    institutionAccountsRequestDto.getBankCode().trim().toUpperCase(),
                    institutionAccountsRequestDto.getAccountOrigin(),
                    institutionAccountsRequestDto.getChargingInstitution(),
                    institutionAccountsRequestDto.getInstitutionAcctsId()
            )) {
                throw new BusinessException(ResponseCode.INT_INSTITUTION_ACCOUNTS_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            InstitutionAccounts saveInstitutionAccounts = this.institutionAccountsMapper.toEntity(institutionAccountsRequestDto);
            saveInstitutionAccounts.setBankCode(institutionAccountsRequestDto.getBankCode().trim().toUpperCase());
            saveInstitutionAccounts.setIssuerAcqProfile(institutionAccountsRequestDto.getIssuerAcqProfile().trim().toUpperCase());
            if (institutionAccountsRequestDto.getAccountOrigin().trim().toUpperCase().equals("B")) {
            	saveInstitutionAccounts.setChargingInstitution("");
            }
            saveInstitutionAccounts.setCreatedBy(requestInstitutionAccounts.get().getCreatedBy());
            saveInstitutionAccounts.setCreatedDate(requestInstitutionAccounts.get().getCreatedDate());
            saveInstitutionAccounts.setUpdatedBy(Integer.valueOf(userDetails.getId()));
            saveInstitutionAccounts.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            savedInstitutionAccounts = (InstitutionAccounts)this.institutionAccountsRepository.save(saveInstitutionAccounts);
        } else { //Case of create
            if (this.institutionAccountsRepository.existsByInstitutionIdAndAccountTypeAndCardSchemeIdAndIssuerAcqProfileAndCurrencyCodeAndBankCodeAndAccountOriginAndChargingInstitution(
                    institutionAccountsRequestDto.getInstitutionId(),
                    institutionAccountsRequestDto.getAccountType(),
                    institutionAccountsRequestDto.getCardSchemeId(),
                    institutionAccountsRequestDto.getIssuerAcqProfile(),
                    institutionAccountsRequestDto.getCurrencyCode(),
                    institutionAccountsRequestDto.getBankCode().trim().toUpperCase(),
                    institutionAccountsRequestDto.getAccountOrigin(),
                    institutionAccountsRequestDto.getChargingInstitution()
            )) {
                throw new BusinessException(ResponseCode.INT_INSTITUTION_ACCOUNTS_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            InstitutionAccounts saveInstitutionAccounts = this.institutionAccountsMapper.toEntity(institutionAccountsRequestDto);
            saveInstitutionAccounts.setBankCode(institutionAccountsRequestDto.getBankCode().trim().toUpperCase());
            saveInstitutionAccounts.setIssuerAcqProfile(institutionAccountsRequestDto.getIssuerAcqProfile().trim().toUpperCase());
            saveInstitutionAccounts.setCreatedBy(Integer.valueOf(userDetails.getId()));
            saveInstitutionAccounts.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            savedInstitutionAccounts = (InstitutionAccounts)this.institutionAccountsRepository.save(saveInstitutionAccounts);
        }
        Currency currency = this.currencyRepository.findByCurrencyCode(savedInstitutionAccounts.getCurrencyCode()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
    	InstitutionAccountsResponseDto institutionAccountsResponseDto = this.institutionAccountsMapper.toDto(savedInstitutionAccounts);
    	institutionAccountsResponseDto.setCurrencyName(currency.getCurrencyName());
    	if(Objects.nonNull(savedInstitutionAccounts.getChargingInstitution()) && !savedInstitutionAccounts.getChargingInstitution().equals("")) {
    		Institution chargingInstitutions = this.institutionRepository.findById(savedInstitutionAccounts.getChargingInstitution()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        	institutionAccountsResponseDto.setChargingInstitutionName(chargingInstitutions.getInstitutionName());
    	}
    	return institutionAccountsResponseDto;
    }
    
    public void deleteInstitutionAccounts(final int id) {
        InstitutionAccounts institutionAccounts = this.institutionAccountsRepository.findById(id).orElseThrow(() -> new BusinessException(ResponseCode.INT_INSTITUTION_ACCOUNTS_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.institutionAccountsRepository.delete(institutionAccounts);
    }
}