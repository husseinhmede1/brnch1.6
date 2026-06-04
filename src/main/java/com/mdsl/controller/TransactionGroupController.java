package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.SaveOrUpdateTransactionGroupRequestDto;
import com.mdsl.model.dto.request.TransactionGroupRequestDto;
import com.mdsl.model.dto.response.TransactionGroupDto;
import com.mdsl.service.DefaultTransactionIdService;
import com.mdsl.service.TransactionChargesDetailsService;
import com.mdsl.service.TransactionGroupService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactiongroup")
public class TransactionGroupController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionGroupController.class);

	private final TransactionGroupService transactionGroupService;

	private final TransactionChargesDetailsService transactionChargesDetailsService;

	@PostMapping
	public ResponseEntity<TransactionGroupRequestDto> saveOrUpdateTransactionGroups(@Valid @RequestBody TransactionGroupRequestDto transactionGroupRequestDto,BindingResult bindingResult,HttpServletRequest httpServletRequest) {
		Validations.validate(bindingResult);
		try {
			SaveOrUpdateTransactionGroupRequestDto saveOrUpdateTransactionGroupRequestDto = SaveOrUpdateTransactionGroupRequestDto.builder()
					.transactionGroupRequestDto(transactionGroupRequestDto)
					.institutionId(httpServletRequest.getHeader("instId"))
					.build();
			return ResponseEntity.ok(transactionGroupService.saveOrUpdateTransactionGroup(saveOrUpdateTransactionGroupRequestDto));
		}catch(BusinessException ex) {
			logger.error("@TransactionGroupController#saveOrUpdateTransactionGroups-business exception "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		}catch(Exception ex){
			logger.error("@TransactionGroupController#saveOrUpdateTransactionGroups-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity viewTransactionGroup(HttpServletRequest httpServletRequest) {
		String instId = httpServletRequest.getHeader("instId");
		if (instId != null) {		
			return ResponseEntity.ok(transactionGroupService.viewTransactionGroup(instId));
		}
		else {
			logger.error("@TransactionGroupController#viewTransactionGroup - institution id not found");
			throw new BusinessException(ResponseCode.INVALID_INSTITUTION_ID,HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping("/{transactiongroupid}")
	public ResponseEntity getTransactionGroup(@PathVariable("transactiongroupid") int transactionGroupId,HttpServletRequest httpServletRequest) {
		String instId = httpServletRequest.getHeader("instId");
		return ResponseEntity.ok(transactionChargesDetailsService.getTransactionChargesDetails(transactionGroupId,instId));
	}

	@DeleteMapping("/{transactiongroupid}")
	public ResponseEntity<String> deleteTransactionGroup(@PathVariable("transactiongroupid") int transactionGroupId) {

		try {
			transactionGroupService.deleteTransactionGroup(transactionGroupId);
			String message = "An item is deleted with id : " + transactionGroupId;
			return ResponseEntity.ok(message);
		} catch (BusinessException ex) {
			logger.error("@TransactionGroupController#deleteTransactionGroup - BusinessException -"+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		}
	}

	@DeleteMapping("/deletetransactionchargedetail/{transactionchargedetailid}")
	public String deleteTransactionChargeDetail(
			@PathVariable("transactionchargedetailid") int transactionchargedetailid) {
		return transactionChargesDetailsService.deleteTransactionChargesDetails(transactionchargedetailid);
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeTransactionGroupStatus(@Valid @RequestBody ChangeStatusRequestDto changeTransactionChargesDetailsStatusDto,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(transactionChargesDetailsService.changeStatus(changeTransactionChargesDetailsStatusDto));
	}

	@GetMapping("/active-transactiongroup")
	public ResponseEntity getActiveTransactionGroup() {
		return ResponseEntity.ok(transactionChargesDetailsService.getActiveTransactionGroup());
	}

	@PostMapping("/institution")
	public ResponseEntity getTransactionGroupsByInstitutionId(
			HttpServletRequest httpServletRequest, @RequestBody TransactionGroupDto requestDto) {
		
		String instId = httpServletRequest.getHeader("instId");
		if (instId != null) {
			requestDto.setInstitutionId(instId);
			List<TransactionGroupDto> groups = transactionGroupService
					.getTransactionGroupsByInstitutionId(requestDto);
			return ResponseEntity.ok(groups);
		}
		else {
			logger.info("@TransactionGroupController#getTransactionGroupsByInstitutionId - institution id not found");
			throw new BusinessException(ResponseCode.INVALID_INSTITUTION_ID,HttpStatus.NOT_FOUND);
		}
		
	}

}
