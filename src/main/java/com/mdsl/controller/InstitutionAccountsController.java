package com.mdsl.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.InstitutionAccountsAccountTypeRequestDto;
import com.mdsl.model.dto.request.InstitutionAccountsGetRequestDto;
import com.mdsl.model.dto.request.InstitutionAccountsRequestDto;
import com.mdsl.model.dto.response.InstitutionAccountsResponseDto;
import com.mdsl.service.InstitutionAccountsService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/institution-accounts")
public class InstitutionAccountsController
{
  private final InstitutionAccountsService institutionAccountsService;
  private static final Logger logger = LoggerFactory.getLogger(InstitutionAccountsController.class);

  @Autowired
  public InstitutionAccountsController(InstitutionAccountsService institutionAccountsService)
  {
    this.institutionAccountsService = institutionAccountsService;
  }

  @GetMapping({"/inst/{instId}"})
  public ResponseEntity<List<InstitutionAccountsResponseDto>> getAllInstitutionAccountsByInstitution(@PathVariable("instId") String instId) {
    return ResponseEntity.ok(this.institutionAccountsService.getAllInstitutionAccountsByInstitution(instId));
  }

  @GetMapping({"/{id}"})
  public ResponseEntity<InstitutionAccountsResponseDto> getInstitutionAccountsById(@PathVariable("id") int id) {
    return ResponseEntity.ok(this.institutionAccountsService.getInstitutionAccountsById(id));
  }
  
  @PostMapping("/charging")
  public ResponseEntity<List<InstitutionAccountsResponseDto>> getAllInstitutionAccountsByInstitutionChargingInstitutionAndBankCode(@Valid @RequestBody InstitutionAccountsGetRequestDto institutionAccountsGetRequestDto, BindingResult bindingResult) {
    Validations.validate(bindingResult);
    return ResponseEntity.ok(this.institutionAccountsService.getAllInstitutionAccountsByInstitutionChargingInstitutionAndBankCode(institutionAccountsGetRequestDto));
  }
  
  @PostMapping("/account-types")
  public ResponseEntity<List<String>> getDistinctAccountTypes(@Valid @RequestBody InstitutionAccountsAccountTypeRequestDto institutionAccountsAccountTypeRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
	    return ResponseEntity.ok(this.institutionAccountsService.getDistinctAccountTypes(institutionAccountsAccountTypeRequestDto));
   }

  @PostMapping
  public ResponseEntity<InstitutionAccountsResponseDto> saveInstitutionAccounts(@Valid @RequestBody InstitutionAccountsRequestDto institutionAccountsRequestDto, BindingResult bindingResult) {
      Validations.validate(bindingResult);
      try {
         return ResponseEntity.ok(this.institutionAccountsService.saveInstitutionAccounts(institutionAccountsRequestDto));
	  } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	  } catch (Exception e) {
	      logger.error("@InstitutionAccountsController#saveInstitutionAccounts: " + e.getMessage());
	      throw new BusinessException (ResponseCode.INT_INSTITION_ACCOUNT_NO_SAVE, HttpStatus.BAD_REQUEST);
	  }
  }

  @DeleteMapping({"/{id}"})
  public void deleteInstitutionAccounts(@PathVariable("id") int id) {
	  try {
		  this.institutionAccountsService.deleteInstitutionAccounts(id);
	  } catch (BusinessException e) {
		  throw new BusinessException (e.getMessage(), e.getHttpStatus());
	  } catch (Exception e) {
	      logger.error("@InstitutionAccountsController#deleteInstitutionAccounts: " + e.getMessage());
	      throw new BusinessException (ResponseCode.INT_INSTITION_ACCOUNT_NO_DELETE, HttpStatus.BAD_REQUEST);
	  }
  }
}