package com.mdsl.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.response.TransactionCurrentResponseDto;
import com.mdsl.service.TransactionCurrentService;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/transaction-current")
@RequiredArgsConstructor
public class TransactionCurrentController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionCurrentController.class);
	private final TransactionCurrentService transactionCurrentService;
	  
	  @GetMapping("/merch-setl-date/{acqInstId}")
	  public ResponseEntity<List<TransactionCurrentResponseDto>> getMerchSettlDate(@PathVariable("acqInstId") String acqInstId) {
		  return ResponseEntity.ok(this.transactionCurrentService.getMerchSettlDate(acqInstId));
	  }
	  
	  @GetMapping("/merch-payment-date/{acqInstId}")
	  public ResponseEntity<List<TransactionCurrentResponseDto>> getMerchPaymentDate(@PathVariable("acqInstId") String acqInstId) {
		  return ResponseEntity.ok(this.transactionCurrentService.getMerchPaymentDate(acqInstId));
	  }

	@GetMapping("/merch-setl-date-revert/{acqInstId}")
	public ResponseEntity<List<TransactionCurrentResponseDto>> getMerchSettlDateRevert(@PathVariable("acqInstId") String acqInstId) {
		return ResponseEntity.ok(this.transactionCurrentService.getMerchSettlDateRevert(acqInstId));
	}

}
