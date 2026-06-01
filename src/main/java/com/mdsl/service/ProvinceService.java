package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ProvinceResquestDto;
import com.mdsl.model.dto.response.CountryResponseDto;
import com.mdsl.model.dto.response.ProvinceResponseDto;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Province;
import com.mdsl.model.mapper.ProvinceMapper;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.ProvinceRepository;
import com.mdsl.utils.ResponseCode;


@Service
public class ProvinceService {

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private ProvinceMapper provinceMapper;
	
	public List<ProvinceResponseDto> getProvinceByCountry(int countryId) {
		// TODO Auto-generated method stub
		List<ProvinceResponseDto> provinceResponseDto = new ArrayList<ProvinceResponseDto>();
		Optional<Country> country = countryRepository.findById(countryId);
		Country cntry = country
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<Province> province = provinceRepository.findByCntryCode(cntry);
		
		province.stream().forEach((prv)->{
			ProvinceResponseDto dto = provinceMapper.toDto(prv);
			provinceResponseDto.add(dto);
		});
		
		return provinceResponseDto;
	}

	public ProvinceResponseDto saveOrUpdateProvince(ProvinceResquestDto provinceResquestDto) {
		// TODO Auto-generated method stub
		Province saveProvince;
		Country country = countryRepository.findById(provinceResquestDto.getCntryId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (Objects.isNull(provinceResquestDto.getProvinceId()) || provinceResquestDto.getProvinceId() == 0) {
			saveProvince = provinceMapper.toEntity(provinceResquestDto);
			saveProvince.setCntryCode(country);
			saveProvince.setDate(new Date());
		}else {
			Province province = provinceRepository.findById(provinceResquestDto.getProvinceId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_PROVINCE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
			saveProvince = provinceMapper.toEntity(provinceResquestDto);
			saveProvince.setCntryCode(country);
			saveProvince.setDate(province.getDate());
		}
		saveProvince = provinceRepository.save(saveProvince);
		ProvinceResponseDto pdto = provinceMapper.toDto(saveProvince);
		return pdto;
	}

	public void deleteProvince(int provinceId)throws Exception {
		// TODO Auto-generated method stub
		provinceRepository.findById(provinceId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_PROVINCE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		provinceRepository.deleteById(provinceId);
	}
	public List<ProvinceResponseDto> getAllProvince() {
		List<Province> allProvinces=provinceRepository.findAll();
		List<ProvinceResponseDto> provinceResponseDto = new ArrayList<ProvinceResponseDto>();
				allProvinces.stream().forEach((province) -> {
					ProvinceResponseDto pdto = provinceMapper.toDto(province);
					provinceResponseDto.add(pdto);
				});
		return provinceResponseDto;
	}
}
