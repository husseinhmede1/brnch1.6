package com.mdsl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DefaultTransactionIdRequestDto;

import com.mdsl.model.dto.request.TransactionGroupRequestDto;
import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.TransactionChargeDetails;
import com.mdsl.service.DefaultTransactionIdService;
import com.mdsl.service.TransactionChargesDetailsService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/transactionchargesdetails")
@RequiredArgsConstructor
public class TransactionChargesDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionChargesDetailsController.class);

	private final TransactionChargesDetailsService transactionChargesDetailsService;

	@PostMapping
	public ResponseEntity<TransactionGroupRequestDto> saveOrUpdateTransactionGroups(@Valid @RequestBody TransactionGroupRequestDto transactionGroupRequestDto,BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(transactionChargesDetailsService.saveOrUpdateTransactionGroup(transactionGroupRequestDto));
	}

	@GetMapping
	public ResponseEntity viewTransactionChargesDetails() {
		return ResponseEntity.ok(transactionChargesDetailsService.viewTransactionChargesDetails());
	}

	@GetMapping("/{transactiongroupid}")
	public ResponseEntity getTransactionGroupDetails(@PathVariable("transactiongroupid") int transactionGroupId,HttpServletRequest httpServletRequest) {
		String instId = httpServletRequest.getHeader("instId");
		return ResponseEntity.ok(transactionChargesDetailsService.getTransactionChargesDetails(transactionGroupId,instId));
	}
//
	@DeleteMapping("/{transactiongroupid}")
	public String deleteTransactionChargesDetails(@PathVariable("transactiongroupid") int transactionGroupId) {
		return transactionChargesDetailsService.deleteTransactionChargesDetails(transactionGroupId);
	}

	@PostMapping("/status-change")
	public void changeTransactionChargesDetailsStatus(@Valid @RequestBody ChangeStatusRequestDto changeTransactionChargesDetailsStatusDto,
			BindingResult bindingResult, HttpServletRequest request) {
		 Validations.validate(bindingResult);
		transactionChargesDetailsService.changeStatus(changeTransactionChargesDetailsStatusDto);
	}

	@GetMapping("/active-transactiongroup")
	public ResponseEntity getActiveTransactionGroup() {
		return ResponseEntity.ok(transactionChargesDetailsService.getActiveTransactionGroup());
	}
	
	
}
