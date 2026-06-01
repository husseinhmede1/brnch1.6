package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.response.TransactionMerchantNamesListDto;
import com.mdsl.utils.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.TransactionCurrentResponseDto;
import com.mdsl.repository.TransactionCurrentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionCurrentService {
	
	private final TransactionCurrentRepository transactionCurrentRepository;
	
	public List<TransactionCurrentResponseDto> getMerchSettlDate(String acqInstId) {
		List<String> transactionCurrents = this.transactionCurrentRepository.findMerchSettlementDatesNotInAccountingLedger(acqInstId);
		List<TransactionCurrentResponseDto> transactionCurrentResponseDtos = new ArrayList<TransactionCurrentResponseDto>();
		transactionCurrents.stream().forEach((transactionCurrent) -> {
			TransactionCurrentResponseDto transactionCurrentResponseDto = new TransactionCurrentResponseDto();
			transactionCurrentResponseDto.setMerchSettlementDate(transactionCurrent);
			transactionCurrentResponseDtos.add(transactionCurrentResponseDto);
		});
		return transactionCurrentResponseDtos;
	}
	
	public List<TransactionCurrentResponseDto> getMerchPaymentDate(String acqInstId) {
		List<String> transactionCurrents = this.transactionCurrentRepository.findMerchPaymentDatesNotInAccountingLedger(acqInstId);
		List<TransactionCurrentResponseDto> transactionCurrentResponseDtos = new ArrayList<TransactionCurrentResponseDto>();
		transactionCurrents.stream().forEach((transactionCurrent) -> {
			TransactionCurrentResponseDto transactionCurrentResponseDto = new TransactionCurrentResponseDto();
			transactionCurrentResponseDto.setMerchSettlementDate(transactionCurrent);
			transactionCurrentResponseDtos.add(transactionCurrentResponseDto);
		});
		return transactionCurrentResponseDtos;
	}

	public List<TransactionCurrentResponseDto> getMerchSettlDateRevert(String acqInstId) {
		if(acqInstId == null || acqInstId.isEmpty() || acqInstId.length()>=10){
			throw new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		List<String> transactionCurrents = this.transactionCurrentRepository.getMerchSettlDateRevert(acqInstId);
		List<TransactionCurrentResponseDto> transactionCurrentResponseDtos = new ArrayList<TransactionCurrentResponseDto>();
		transactionCurrents.forEach((transactionCurrent) -> {
			TransactionCurrentResponseDto transactionCurrentResponseDto = new TransactionCurrentResponseDto(transactionCurrent);
			transactionCurrentResponseDtos.add(transactionCurrentResponseDto);
		});
		return transactionCurrentResponseDtos;
	}

	public List<String> getTransactionMerchantNames(String instId){
		return transactionCurrentRepository.getTransactionMerchantNames(instId);
	}
}
