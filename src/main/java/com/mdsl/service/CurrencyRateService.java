package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.CurrencyRateRequestDto;
import com.mdsl.model.dto.response.CurrencyRateResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.CurrencyRate;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.CurrencyRateMapper;
import com.mdsl.repository.CurrencyRateRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyRateService {

	@Autowired
	private CurrencyRateRepository currencyRateRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private CurrencyRateMapper currencyRateMapper;

	public List<CurrencyRateResponseDto> getAllCurrencyRates() {
		List<CurrencyRateResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyRateResponseDto>();
		List<CurrencyRate> allCurrencyCodes = currencyRateRepository
				.findAll(Sort.by(Sort.Direction.ASC, "currencyRateId"));

		allCurrencyCodes.stream().forEach((currencyCode) -> {
			CurrencyRateResponseDto currencyCodeResponseDto = currencyRateMapper.toDto(currencyCode);
			allCurrencyCodesDto.add(currencyCodeResponseDto);
		});

		return allCurrencyCodesDto;
	}

	public CurrencyRateResponseDto getCurrencyRateById(int id) {
		Optional<CurrencyRate> currency = currencyRateRepository.findByCurrencyRateId(id);
		CurrencyRate currCode = currency
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		return currencyRateMapper.toDto(currCode);
	}

	public List<CurrencyRateResponseDto> getCurrencyRateByInstitutionId(String id) {
		List<CurrencyRateResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyRateResponseDto>();

		Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<CurrencyRate> allCurrencyCodes = currencyRateRepository.findByInstitution(institution.getInstitutionId(),
				Sort.by(Sort.Direction.ASC, "currencyRateId"));

		allCurrencyCodes.stream().forEach((currencyCode) -> {
			CurrencyRateResponseDto currencyCodeResponseDto = currencyRateMapper.toDto(currencyCode);
			allCurrencyCodesDto.add(currencyCodeResponseDto);
		});

		return allCurrencyCodesDto;
	}

	public CurrencyRateResponseDto saveOrUpdateCurrencyRate(CurrencyRateRequestDto currencyRateRequestDto) {

		CurrencyRate curr;
		CurrencyRate currency;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		
		Currency currency2 = currencyRepository.findById(currencyRateRequestDto.getCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(currencyRateRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (Objects.isNull(currencyRateRequestDto.getCurrencyRateId())
				|| currencyRateRequestDto.getCurrencyRateId() == 0) {
//			if (currencyRateRequestDto.getEffectiveDate() == null) {
//				throw new BusinessException(ResponseCode.CFG_INVALID_DATE, HttpStatus.NOT_FOUND);
//			}

			currency = currencyRateMapper.toEntity(currencyRateRequestDto);
			currency.setCurrency(currency2);
			currency.setInstitution(institution);
		//	currency.setRecordSeqId(0);
		//	currency.setUserCreate("test");
			currency.setDateCreate(new Date());
			if(userDetails!=null) {
				currency.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}
//			currency.setEffectiveDate(new Date());
			currency = currencyRateRepository.save(currency);
		}

		else {
			curr = currencyRateRepository.findById(currencyRateRequestDto.getCurrencyRateId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
			curr.setCurrency(currency2);
			curr.setInstitution(institution);
//			curr.setEffectiveDate(currencyRateRequestDto.getEffectiveDate());

			if (currencyRateRequestDto.getEffectiveDate().after(new Date())) {
				curr.setEffectiveDate(currencyRateRequestDto.getEffectiveDate());
			} else if (DateUtils.isSameDay(currencyRateRequestDto.getEffectiveDate(), new Date())) {
				curr.setEffectiveDate(currencyRateRequestDto.getEffectiveDate());
			} else {
				throw new BusinessException(ResponseCode.CUR_INVALID_DATE, HttpStatus.NOT_FOUND);
			}

			curr.setBuyRate(currencyRateRequestDto.getBuyRate());
			curr.setMidRate(currencyRateRequestDto.getMidRate());
			curr.setSellRate(currencyRateRequestDto.getSellRate());
		//	curr.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			currency = currencyRateRepository.save(curr);
		}

		return currencyRateMapper.toDto(currency);
	}

	public void deleteCurrencyRate(int id) throws Exception {
		currencyRateRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		currencyRateRepository.deleteById(id);
	}

//	public List<CurrencyRateResponseDto> getCurrencyRatesBySearch(CurrencyRateRequestDto currency, int id) {
//		List<CurrencyRateResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyRateResponseDto>();
//		List<CurrencyRate> allCurrencyCodes;
//
//		Institution institution = institutionRepository.findById(id)
//				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST_CODE, HttpStatus.NOT_FOUND));
//
//		if (currency.getFromDate() != null && currency.getToDate() == null) {
//			allCurrencyCodes = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateGreaterThanEqual(
//					institution.getInstitutionId(), currency.getFromDate(),
//					Sort.by(Sort.Direction.ASC, "currencyRateId"));
//		} else if (currency.getFromDate() == null && currency.getToDate() != null) {
//			allCurrencyCodes = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateLessThanEqual(
//					institution.getInstitutionId(), currency.getToDate(),
//					Sort.by(Sort.Direction.ASC, "currencyRateId"));
//		} else if (currency.getFromDate() != null && currency.getToDate() != null) {
//			allCurrencyCodes = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateBetween(
//					institution.getInstitutionId(), currency.getFromDate(), currency.getToDate(),
//					Sort.by(Sort.Direction.ASC, "currencyRateId"));
//		} else {
//			allCurrencyCodes = currencyRateRepository.findByInstitution_InstitutionId(institution.getInstitutionId(),
//					Sort.by(Sort.Direction.ASC, "currencyRateId"));
//		}
//
//		allCurrencyCodes.stream().forEach((currencyCode) -> {
//			CurrencyRateResponseDto currencyCodeResponseDto = currencyRateMapper.toDto(currencyCode);
//			allCurrencyCodesDto.add(currencyCodeResponseDto);
//		});
//
//		return allCurrencyCodesDto;
//	}

	public ResponseEntity<PaginationResponseDto> getCurrencyRatesBySearch(CurrencyRateRequestDto currencyRequestDto,
			String id) {
		Page<CurrencyRate> page = null;
		List<CurrencyRateResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyRateResponseDto>();
		List<CurrencyRate> allCurrencyCodes;

		Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(currencyRequestDto.getSort(),
				"currency.currencyName", currencyRequestDto.getPageNo(), currencyRequestDto.getPageSize());

		if (currencyRequestDto.getFromDate() != null && currencyRequestDto.getToDate() == null) {
			page = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateGreaterThanEqual(
					institution.getInstitutionId(), currencyRequestDto.getFromDate(), pageRequest);
		} else if (currencyRequestDto.getFromDate() == null && currencyRequestDto.getToDate() != null) {
			page = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateLessThanEqual(
					institution.getInstitutionId(), currencyRequestDto.getToDate(), pageRequest);
		} else if (currencyRequestDto.getFromDate() != null && currencyRequestDto.getToDate() != null) {
			page = currencyRateRepository.findByInstitution_InstitutionIdAndEffectiveDateBetween(
					institution.getInstitutionId(), currencyRequestDto.getFromDate(), currencyRequestDto.getToDate(),
					pageRequest);
		} else {
			page = currencyRateRepository.findByInstitution_InstitutionId(institution.getInstitutionId(), pageRequest);
		}

		page.getContent().stream().forEach((currencyCode) -> {
			CurrencyRateResponseDto currencyCodeResponseDto = currencyRateMapper.toDto(currencyCode);
			allCurrencyCodesDto.add(currencyCodeResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null, allCurrencyCodesDto,
				page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);
	}

}
