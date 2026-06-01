package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.AccountingLedgerResponseDto;
import com.mdsl.model.dto.response.FileDirectoryResponseDto;
import com.mdsl.repository.AccountingLedgerRepository;
import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountingLedgerService {
    private final AccountingLedgerRepository accountingLedgerRepository;

    public List<AccountingLedgerResponseDto> getMerchPaymentDate(String acqInstId) {
        if(acqInstId == null || acqInstId.isEmpty() || acqInstId.length()>=10){
            throw new BusinessException(ResponseCode.CFG_INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND);
        }

        List<String> merchPaymentDates = accountingLedgerRepository.getMerchantPaymentDate(acqInstId);
        if (merchPaymentDates == null || merchPaymentDates.isEmpty()) {
            throw new BusinessException(ResponseCode.CFG_NO_MERCHANT_DATE_FOUND, HttpStatus.NO_CONTENT);
        }

        return merchPaymentDates.stream()
                .map(AccountingLedgerResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<FileDirectoryResponseDto> getDistinctFileNames(String institutionId) {
        List<String> fileNames = Optional.ofNullable(
                        accountingLedgerRepository.getDistinctFileNames(institutionId)
                )
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new BusinessException(
                        ResponseCode.NO_FILE_NAMES_FOUND, HttpStatus.NO_CONTENT
                ));

        return fileNames.stream()
                .filter(Objects::nonNull)
                .map(fileName -> new FileDirectoryResponseDto(fileName, fileName))
                .collect(Collectors.toList());
    }
}
