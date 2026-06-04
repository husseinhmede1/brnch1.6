package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.CurrencyConversionRequestDto;
import com.mdsl.model.dto.response.CurrencyConversionResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.CurrencyConversion;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.CurrencyConversionMapper;
import com.mdsl.repository.CurrencyConversionRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

	@Autowired
	private CurrencyConversionRepository currencyConversionRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private CurrencyConversionMapper currencyConversionMapper;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public List<CurrencyConversionResponseDto> getAllCurrencyConversions() {
		List<CurrencyConversionResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyConversionResponseDto>();
		List<CurrencyConversion> allCurrencyCodes = currencyConversionRepository
				.findAll(Sort.by(Sort.Direction.ASC, "currencyConversionId"));

		allCurrencyCodes.stream().forEach((currencyCode) -> {
			CurrencyConversionResponseDto currencyCodeResponseDto = currencyConversionMapper.toDto(currencyCode);
			allCurrencyCodesDto.add(currencyCodeResponseDto);
		});

		return allCurrencyCodesDto;
	}

	public CurrencyConversionResponseDto getCurrencyConversionById(int id) {
		Optional<CurrencyConversion> currency = currencyConversionRepository.findByCurrencyConversionId(id);
		CurrencyConversion currCode = currency
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		return currencyConversionMapper.toDto(currCode);
	}

	public List<CurrencyConversionResponseDto> getCurrencyConversionByInstitutionId(String id) {
		List<CurrencyConversionResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyConversionResponseDto>();
		
		Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		List<CurrencyConversion> allCurrencyCodes = currencyConversionRepository
				.findByInstitution_InstitutionId(institution.getInstitutionId(), Sort.by(Sort.Direction.ASC, "currencyConversionId"));

		allCurrencyCodes.stream().forEach((currencyCode) -> {
			CurrencyConversionResponseDto currencyCodeResponseDto = currencyConversionMapper.toDto(currencyCode);
			allCurrencyCodesDto.add(currencyCodeResponseDto);
		});

		return allCurrencyCodesDto; 
	}
	
	public CurrencyConversionResponseDto saveOrUpdateCurrencyConversion(
			CurrencyConversionRequestDto currencyConversionRequestDto) {
		
		if(currencyConversionRequestDto.getInstitutionId()!=null) {
			currencyConversionRequestDto.setInstitutionId(currencyConversionRequestDto.getInstitutionId().trim());
		}
		
		if(currencyConversionRequestDto.getRoundingRule()!=null) {
			currencyConversionRequestDto.setRoundingRule(currencyConversionRequestDto.getRoundingRule().trim());
		}
		
		if(currencyConversionRequestDto.getRateExpression()!=null) {
			currencyConversionRequestDto.setRateExpression(currencyConversionRequestDto.getRateExpression().trim());
		}
		
		if(currencyConversionRequestDto.getMidRateUsed()!=null) {
			currencyConversionRequestDto.setMidRateUsed(currencyConversionRequestDto.getMidRateUsed().trim());
		}

		CurrencyConversion curr;
		CurrencyConversion currency=new CurrencyConversion();
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Currency currency2 = currencyRepository.findById(currencyConversionRequestDto.getCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));

		Currency baseCurrency = currencyRepository.findById(currencyConversionRequestDto.getBaseCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));

		Institution institution = institutionRepository.findById(currencyConversionRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (makerCheckerEngine.processIfRequired(currencyConversionRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}

		if (Objects.isNull(currencyConversionRequestDto.getCurrencyConversionId())
				|| currencyConversionRequestDto.getCurrencyConversionId() == 0) {
//			List<CurrencyConversion> currencyConversions= currencyConversionRepository.findByCurrency_CurrencyCodeEqualsIgnoreCaseAndBaseCurrency_CurrencyCodeEqualsIgnoreCaseAndInstitution_InstitutionIdEqualsIgnoreCaseAndRoundingRuleEqualsIgnoreCaseAndRateExpressionEqualsIgnoreCaseAndMidRateUsedEqualsIgnoreCase(currency2.getCurrencyCode(),baseCurrency.getCurrencyCode(),currencyConversionRequestDto.getInstitutionId(),currencyConversionRequestDto.getRoundingRule(),currencyConversionRequestDto.getRateExpression(),currencyConversionRequestDto.getMidRateUsed());
			if(!checkCurrencyConversionOnAdd(currency2.getCurrencyCode(), baseCurrency.getCurrencyCode(), institution.getInstitutionId())) {
				throw new BusinessException(ResponseCode.CUR_CURRENCY_CONVERSION_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}else {
				currency = currencyConversionMapper.toEntity(currencyConversionRequestDto);
				currency.setCurrency(currency2);
				currency.setInstitution(institution);
				currency.setBaseCurrency(baseCurrency);
				//currency.setRecordSeqId(0);
				//currency.setUserCreate("test");
				currency.setDateCreate(new Date());
				if(userDetails!=null) {
					currency.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				}
				currency = currencyConversionRepository.save(currency);
			}
			
		}else {
			
//			List<CurrencyConversion> currencyConversions= currencyConversionRepository.findByCurrency_CurrencyCodeEqualsIgnoreCaseAndBaseCurrency_CurrencyCodeEqualsIgnoreCaseAndInstitution_InstitutionIdEqualsIgnoreCaseAndRoundingRuleEqualsIgnoreCaseAndRateExpressionEqualsIgnoreCaseAndMidRateUsedEqualsIgnoreCaseAndCurrencyConversionIdNot(currency2.getCurrencyCode(),baseCurrency.getCurrencyCode(),currencyConversionRequestDto.getInstitutionId(),currencyConversionRequestDto.getRoundingRule(),currencyConversionRequestDto.getRateExpression(),currencyConversionRequestDto.getMidRateUsed(),currencyConversionRequestDto.getCurrencyConversionId());
			if(!checkCurrencyConversionOnUpdate(currency2.getCurrencyCode(), baseCurrency.getCurrencyCode(), institution.getInstitutionId(), currencyConversionRequestDto.getCurrencyConversionId())) {
				throw new BusinessException(ResponseCode.CUR_CURRENCY_CONVERSION_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}else {
				curr = currencyConversionRepository.findById(currencyConversionRequestDto.getCurrencyConversionId())
						.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
				curr.setCurrency(currency2);
				curr.setInstitution(institution);
				curr.setBaseCurrency(baseCurrency);

				curr.setRoundingRule(currencyConversionRequestDto.getRoundingRule());
				curr.setRateExpression(currencyConversionRequestDto.getRateExpression());
				curr.setMidRateUsed(currencyConversionRequestDto.getMidRateUsed());
			//	curr.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				currency = currencyConversionRepository.save(curr);
			}
			
		}

		return currencyConversionMapper.toDto(currency);
	}

	public void deleteCurrencyConversion(int id) throws Exception{
		currencyConversionRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		currencyConversionRepository.deleteById(id);
	}
	
	public boolean checkCurrencyConversionOnAdd(String currency, String baseCurrency, String instId) {
		List<CurrencyConversion> currencies = currencyConversionRepository
				.findByCurrency_CurrencyCodeIgnoreCaseAndBaseCurrency_CurrencyCodeIgnoreCaseAndInstitution_InstitutionId(currency, baseCurrency, instId);
		return currencies.size() == 0;
	}

	public boolean checkCurrencyConversionOnUpdate(String currency, String baseCurrency, String instId, Integer recordSeqId) {
		List<CurrencyConversion> currencies = currencyConversionRepository
				.findByCurrency_CurrencyCodeIgnoreCaseAndBaseCurrency_CurrencyCodeIgnoreCaseAndInstitution_InstitutionIdAndCurrencyConversionIdNot(currency, baseCurrency, instId,
						recordSeqId);
		
		if(currencies.size()>0) {
			return false;
		}else {
			return true;
		}
		
	}
}
