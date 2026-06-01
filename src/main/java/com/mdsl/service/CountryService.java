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
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.CountryRequestDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.mapper.CountryMapper;
//import com.mdsl.model.mapper.CountryMapper;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
//@Transactional(readOnly = true)
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private CountryMapper countryMapper;

	public List<CountryResponseDto> getAllCountries() {

		List<CountryResponseDto> countryResponseDto = new ArrayList<CountryResponseDto>();
		List<Country> allCountry = countryRepository.findAll(Sort.by(Sort.Direction.ASC, "cntryName"));
		allCountry.stream().forEach((coutry) -> {
			CountryResponseDto cdto = countryMapper.toDto(coutry);
			countryResponseDto.add(cdto);
		});
		return countryResponseDto;
	}

	public List<CountryResponseDto> getActiveCountries() {
		List<CountryResponseDto> countryResponseDto = new ArrayList<CountryResponseDto>();
		List<Country> activeCountries = countryRepository.findByCntryStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "cntryName"));
		activeCountries.stream().forEach((coutry) -> {
			CountryResponseDto cdto = countryMapper.toDto(coutry);
			countryResponseDto.add(cdto);
		});
		return countryResponseDto;
	}

	public CountryResponseDto addOrUpdateCountry(@Valid CountryRequestDto countryDto) {
		Country cntry = new Country();
		Currency currency = null;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
	

		if (countryDto.getCurrencyId() != 0) {
			currency = currencyRepository.findById(countryDto.getCurrencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (countryDto.getCntryId() != 0) {
			cntry = countryRepository.findById(countryDto.getCntryId()).get();
		}
		if (cntry != null) {
			// countryDto.setCntryStatus(cntry.getCntryStatus());
			char status = cntry.getCntryStatus();
			cntry = countryMapper.toEntity(countryDto);
			cntry.setDate(new Date());
			if (status == '1') {
				cntry.setCntryStatus('1');
			} else {
				cntry.setCntryStatus('0');
			}
			cntry.setCurrency(currency);
		//	cntry.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			cntry = countryRepository.save(cntry);
			CountryResponseDto dto = countryMapper.toDto(cntry);
			return dto;
		} else {
			Country cntry1 = new Country();
			// countryMapper = new CountryMapper();
			cntry1 = countryMapper.toEntity(countryDto);
			cntry1.setCntryStatus(StatusEnum.ENABLED.getValue());
			cntry1.setDate(new Date());
			cntry1.setCurrency(currency);
			if(userDetails!=null) {
				cntry1.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			}
			cntry1 = countryRepository.save(cntry1);
			CountryResponseDto dto = countryMapper.toDto(cntry1);
			return dto;
		}
	}

	public void deleteCountry(int id) throws Exception {
		countryRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		countryRepository.deleteById(id);
	}

	public String updateStatus(int id) {
		char enabled = countryRepository.findById(id).get().getCntryStatus();
		if (enabled == '1')
			countryRepository.UpdateStatus(id, '0');
		else {
			countryRepository.UpdateStatus(id, '1');
		}

		return "Status changed successfully";
	}

}
