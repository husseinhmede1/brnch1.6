package com.mdsl.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.RestResponseEntityExceptionHandler;
import com.mdsl.model.dto.request.PaymentAccountRequestDto;
import com.mdsl.model.dto.response.PaymentAccountResponseDto;
import com.mdsl.service.ActivityPackageDetailService;
import com.mdsl.service.PaymentAccountService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
//@Transactional(rollbackOn=Exception.class)
@RequiredArgsConstructor
@RequestMapping("/payment-account")
public class PaymentAccountController {

	private final PaymentAccountService paymentAccountService;
	private final ActivityPackageDetailService activityPackageDetailService;
	private static final Logger logger = LoggerFactory.getLogger(PaymentAccountController.class);


	@GetMapping
	public ResponseEntity<List<PaymentAccountResponseDto>> getAllPaymentAccount()
	{
		return ResponseEntity.ok(paymentAccountService.fetchAllPaymentAccount());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PaymentAccountResponseDto> getPaymentAccountById(@PathVariable("id") int id)
	{
		return ResponseEntity.ok(paymentAccountService.fetchPaymentAccountById(id));
	}
	
	@GetMapping("/institution-id/{id}")
	public ResponseEntity<List<PaymentAccountResponseDto>> getPaymentAccountByInstitutionId(@PathVariable("id") String id)
	{
		return ResponseEntity.ok(paymentAccountService.fetchPaymentAccountByInstitutionId(id));
	}
	
	@PostMapping
	 public ResponseEntity saveOrUpdatePaymentAccount(@Valid @RequestBody  PaymentAccountRequestDto paymentAccountRequestDto, BindingResult bindingResult){
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(paymentAccountService.saveOrUpdatePaymentAccount(paymentAccountRequestDto));
		}catch(HttpMessageNotReadableException e) {
			e.printStackTrace();
		}
		
		catch(Exception e) {
			String exceptionResult=e.getLocalizedMessage();
			String result1[]= exceptionResult.split("is");
			String result2[]=new String[30];
			boolean indexCheckingOfresult1=activityPackageDetailService.isValidIndex(result1,1);
			if(indexCheckingOfresult1) {
				result2=result1[1].trim().split(":");
				boolean indexCheckingOfresult2=activityPackageDetailService.isValidIndex(result2,0);
				if(indexCheckingOfresult2) {
					if(result2[0].equals("org.hibernate.exception.DataException")) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("value larger than specified precision allowed for column"); 
					}
				}
			}
			
			//DataException
			System.out.println(e.getCause());
		}
		return null;
	 }
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePaymentAccount(@PathVariable("id") int id)
	{
		
		try {
			paymentAccountService.deletePaymentAccountById(id);
			String message = "An item is deleted with id : " + id;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@UserController#saveUser-business exception "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@UserController#saveUser-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/entities/{entityId}")
	public ResponseEntity<List<PaymentAccountResponseDto>> getPaymentAccountByEntityId(@PathVariable("entityId") String entityId) {
		return ResponseEntity.ok(paymentAccountService.getPaymentAccountByEntitiesId(entityId));
	}
}