package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import com.mdsl.utils.MakerCheckerEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.CurrencyRequestDto;
import com.mdsl.model.dto.response.CurrencyResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.mapper.CurrencyMapper;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final MakerCheckerEngine makerCheckerEngine;

    public List<CurrencyResponseDto> getAllCurrencies() {
        List<CurrencyResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyResponseDto>();
        List<Currency> allCurrencyCodes = currencyRepository.findAll(Sort.by(Sort.Direction.ASC, "currencyId"));

        allCurrencyCodes.stream().forEach((currencyCode) -> {
            CurrencyResponseDto currencyCodeResponseDto = currencyMapper.toDto(currencyCode);
            allCurrencyCodesDto.add(currencyCodeResponseDto);
        });

        return allCurrencyCodesDto;
    }

    public List<CurrencyResponseDto> getActiveCurrencies() {
        List<CurrencyResponseDto> allCurrencyCodesDto = new ArrayList<CurrencyResponseDto>();
        List<Currency> allCurrencyCodes = currencyRepository.findByStatus("1".charAt(0),
                Sort.by(Sort.Direction.ASC, "currencyName"));

        allCurrencyCodes.stream().forEach((currencyCode) -> {
            CurrencyResponseDto currencyCodeResponseDto = currencyMapper.toDto(currencyCode);
            allCurrencyCodesDto.add(currencyCodeResponseDto);
        });

        return allCurrencyCodesDto;
    }

    public CurrencyResponseDto getCurrencyById(int id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        Currency currCode = currency
                .orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
        return currencyMapper.toDto(currCode);
    }

    public CurrencyResponseDto saveOrUpdateCurrency(CurrencyRequestDto currencyRequestDto) {
        int count = 0;
        Currency curr;

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (Objects.isNull(currencyRequestDto.getCurrencyId()) || currencyRequestDto.getCurrencyId() == 0) {

            try {
                checkCurrencyCodeAndCurrencyName(currencyRequestDto);
                curr = currencyMapper.toEntity(currencyRequestDto);
                //currency.setRecordSeqId(0);
                //	currency.setUserCreate("test");
                curr.setDateCreate(new Date());
                curr.setStatus("1".charAt(0));
                if (userDetails != null) {
                    curr.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
                }
            } catch (BusinessException e) {
                throw new BusinessException(e.getMessage(), e.getHttpStatus());

//				if(e.getMessage().equals("CUR-002")) {
//					throw new BusinessException(ResponseCode.CURRENCY_CODE_ALREADY_ISSUE, HttpStatus.NOT_FOUND);
//				}else if(e.getMessage().equals("CUR-003")) {
//					throw new BusinessException(ResponseCode.CURRENCY_NAME_ALREADY_ISSUE,HttpStatus.NOT_FOUND);
//				}else if(e.getMessage().equals("CUR-004")) {
//					throw new BusinessException(ResponseCode.CURRENCY_CODE_AND_CURRENY_NAME_ALREADY_ISSUE,HttpStatus.NOT_FOUND);
//				}
            }

//			if (code == true) {
//				
//			} else {
//				throw new BusinessException(ResponseCode.CURRENCY_ALREADY_ISSUE, HttpStatus.NOT_FOUND);
//			}

        } else {

//			List<Currency> currencies = currencyRepository
//					.findByCurrencyCodeIgnoreCaseOrCurrencyNameIgnoreCaseAndCurrencyId(
//							currencyRequestDto.getCurrencyCode(), currencyRequestDto.getCurrencyName(),
//							currencyRequestDto.getCurrencyId());

//			curr = currencyRepository.findById(currencyRequestDto.getCurrencyId()).orElseThrow(
//					() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
//			List<Currency> currencies= currencyRepository.findByCurrencyCodeEqualsIgnoreCaseOrCurrencyNameEqualsIgnoreCaseAndCurrencyIdNot(currencyRequestDto.getCurrencyCode(),currencyRequestDto.getCurrencyName(),currencyRequestDto.getCurrencyId());
//			findByCurrencyCodeIgnoreCaseOrCurrencyNameIgnoreCaseAndCurrencyId(curr.get)
//			if (currencies.size() == 0) {
//				boolean inst = checkCurrencyCodeAndCurrencyName(currencyRequestDto);
//				if (inst == true) {
//					count=0;
//				} else {
//					count = 1;
//					throw new BusinessException(ResponseCode.CURRENCY_ALREADY_ISSUE, HttpStatus.NOT_FOUND);
////						ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Institution already exists!");
//				}
//			}

            List<Currency> currenciesByCurrencyCode = currencyRepository.findByCurrencyCodeEqualsIgnoreCaseAndCurrencyIdNot(currencyRequestDto.getCurrencyCode(), currencyRequestDto.getCurrencyId());
            List<Currency> currenciesByCurrencyName = currencyRepository.findByCurrencyNameEqualsIgnoreCaseAndCurrencyIdNot(currencyRequestDto.getCurrencyName(), currencyRequestDto.getCurrencyId());

            if ((currenciesByCurrencyCode.size() > 0) && (currenciesByCurrencyName.size() > 0)) {
                throw new BusinessException(ResponseCode.CUR_CURRENCY_CODE_AND_CURRENCY_NAME_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
            } else if (currenciesByCurrencyCode.size() > 0) {
                throw new BusinessException(ResponseCode.CUR_CURRENCY_CODE_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
            } else if (currenciesByCurrencyName.size() > 0) {
                throw new BusinessException(ResponseCode.CUR_CURRENCY_NAME_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
            }


            try {
                curr = currencyRepository.findById(currencyRequestDto.getCurrencyId()).orElseThrow(
                        () -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
                curr.setCurrencyCode(currencyRequestDto.getCurrencyCode());
                curr.setCurrencyName(currencyRequestDto.getCurrencyName());
                curr.setCurrCodeALPHA2(currencyRequestDto.getCurrCodeALPHA2());
                curr.setCurrCodeALPHA3(currencyRequestDto.getCurrCodeALPHA3());
                curr.setCurrExponent(currencyRequestDto.getCurrExponent());
                //	curr.setUserCreate(Integer.valueOf(userDetails.getId()).toString());

            } catch (BusinessException e) {
                throw new BusinessException(e.getMessage(), e.getHttpStatus());

//				if(e.getMessage().equals("CUR-002")) {
//					throw new BusinessException(ResponseCode.CURRENCY_CODE_ALREADY_ISSUE, HttpStatus.NOT_FOUND);
//				}else if(e.getMessage().equals("CUR-003")) {
//					throw new BusinessException(ResponseCode.CURRENCY_NAME_ALREADY_ISSUE,HttpStatus.NOT_FOUND);
//				}else if(e.getMessage().equals("CUR-004")) {
//					throw new BusinessException(ResponseCode.CURRENCY_CODE_AND_CURRENY_NAME_ALREADY_ISSUE,HttpStatus.NOT_FOUND);
//				}
            }
        }
        if (makerCheckerEngine.processIfRequired(currencyRequestDto, this.getClass().getName(), new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return null;
        }
        curr = currencyRepository.save(curr);
        return currencyMapper.toDto(curr);
    }


    public void deleteCurrency(int id) throws Exception {
        currencyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return;
        }
        currencyRepository.deleteById(id);
    }

    public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
        Currency currency = currencyRepository.findById(changeStatusRequestDto.getId())
                .orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName(), "")) {
            return null;
        }
        currency.setStatus(changeStatusRequestDto.getStatus().charAt(0));
        currencyRepository.save(currency);

        return "Status changed successfully";
    }

    public void checkCurrencyCodeAndCurrencyName(@Valid CurrencyRequestDto currencyRequestDto) {
        List<Currency> currencyByCurrencyCode = currencyRepository.findByCurrencyCodeIgnoreCase(
                currencyRequestDto.getCurrencyCode());


        List<Currency> currencyByCurrencyName = currencyRepository.findByCurrencyNameEqualsIgnoreCase(
                currencyRequestDto.getCurrencyName());


        if ((currencyByCurrencyCode.size() > 0) && (currencyByCurrencyName.size() > 0)) {
            throw new BusinessException(ResponseCode.CUR_CURRENCY_CODE_AND_CURRENCY_NAME_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
        } else if (currencyByCurrencyCode.size() > 0) {
            throw new BusinessException(ResponseCode.CUR_CURRENCY_CODE_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
        } else if (currencyByCurrencyName.size() > 0) {
            throw new BusinessException(ResponseCode.CUR_CURRENCY_NAME_ALREADY_ISSUED, HttpStatus.NOT_FOUND);
        }

    }

}
