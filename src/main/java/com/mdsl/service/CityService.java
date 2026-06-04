package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mdsl.utils.MakerCheckerEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.CityResquestDto;
import com.mdsl.model.dto.response.CityResponseDto;
import com.mdsl.model.entity.City;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Province;
import com.mdsl.model.mapper.CityMapper;
import com.mdsl.model.mapper.CurrencyMapper;
import com.mdsl.repository.AddressRepository;
import com.mdsl.repository.CityRepository;
import com.mdsl.repository.CountryRepository;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.ProvinceRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ProvinceRepository provinceRepository;
    private final MakerCheckerEngine makerCheckerEngine;
    private final CityMapper cityMapper;
    private final AddressRepository addressRepository;

    public List<CityResponseDto> getAllCities() {

        return cityRepository.findAll().stream()
                .map(cityMapper::toDto)
                .collect(Collectors.toList());
    }

    public CityResponseDto getCityById(int cityId) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CITY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        return cityMapper.toDto(city);
    }

    public List<CityResponseDto> getCityByCountryId(int countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<City> cities = cityRepository.findByCntryCode(country);

        if (cities == null || cities.isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_CITY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return cities.stream()
                .map(cityMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CityResponseDto> getCityByProvinceId(int provinceId) {
        List<CityResponseDto> cityResponseDto = new ArrayList<>();

        Province province = provinceRepository.findById(provinceId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_PROVINCE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<City> cities = cityRepository.findByProvStateAbbrev(province);

        if (cities == null || cities.isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_CITY_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        cities.forEach(cty -> {
            if (cty != null) {
                CityResponseDto dto = cityMapper.toDto(cty);
                cityResponseDto.add(dto);
            }
        });

        return cityResponseDto;
    }

    public CityResponseDto saveOrUpdateCity(CityResquestDto cityResquestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        City saveCity;
        Country country = null;
        Province province = null;
        if (Objects.nonNull(cityResquestDto.getCntryCode()) && !cityResquestDto.getCntryCode().equals("")) {
            country = countryRepository.findByCntryCode(cityResquestDto.getCntryCode())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_COUNTRY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        }
        if (Objects.nonNull(cityResquestDto.getProvStateAbbrev()) && !cityResquestDto.getProvStateAbbrev().equals("")) {
            province = provinceRepository.findByProvStateAbbrev(cityResquestDto.getProvStateAbbrev())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_PROVINCE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        }
        Optional<City> existingCityAbbrev = cityRepository.findByCityAbbrev(cityResquestDto.getCityAbbrev());

        if (cityResquestDto.getCityId() == 0) {
            // Create new city
            if (existingCityAbbrev.isPresent()) {
                throw new BusinessException(ResponseCode.CITY_ABBREV_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            saveCity = cityMapper.toEntity(cityResquestDto);
            saveCity.setProvStateAbbrev(province);
            saveCity.setCntryCode(country);
            saveCity.setDate(new Date());
            saveCity.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
        } else {
            City city = cityRepository.findById(cityResquestDto.getCityId())
                    .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CITY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
            Integer addressLinkedCount = addressRepository.findByCityAbbrev(city.getCityAbbrev());
            if (existingCityAbbrev.isPresent() && city.getCityId() != existingCityAbbrev.get().getCityId()) {
                throw new BusinessException(ResponseCode.CITY_ABBREV_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            if (addressLinkedCount > 0 && !city.getCityAbbrev().equals(cityResquestDto.getCityAbbrev())) {
                throw new BusinessException(ResponseCode.INVALID_UPDATE_CITY_CODE, HttpStatus.BAD_REQUEST);
            }
            saveCity = cityMapper.toEntity(cityResquestDto);
            saveCity.setProvStateAbbrev(province);
            saveCity.setCntryCode(country);
            saveCity.setDate(new Date());
            saveCity.setUserCreate(Integer.valueOf(userDetails.getId()).toString());

        }
        if (makerCheckerEngine.processIfRequired(cityResquestDto, this.getClass().getName(), new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return null;
        }
        saveCity = cityRepository.save(saveCity);
        return cityMapper.toDto(saveCity);
    }

    public void deleteProvince(int cityId) throws BusinessException {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_CITY_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        Integer addressLinkedCount = addressRepository.findByCityAbbrev(city.getCityAbbrev());
        if (addressLinkedCount > 0) {
            throw new BusinessException(ResponseCode.INVALID_UPDATE_CITY_CODE, HttpStatus.BAD_REQUEST);
        }
        if (makerCheckerEngine.processIfRequired(cityId, this.getClass().getName(), new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return;
        }
        cityRepository.deleteById(cityId);
    }
}
