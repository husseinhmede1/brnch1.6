package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
//import com.mdsl.model.dto.request.AccountTypeRequestDto;
//import com.mdsl.model.dto.request.ChangeStatusRequestDto;
//import com.mdsl.model.dto.response.AccountTypeResponseDto;
//import com.mdsl.model.entity.AccountType;
//import com.mdsl.model.entity.Institution;
//import com.mdsl.model.mapper.AccountTypeMapper;
//import com.mdsl.repository.AccountRepository;
//import com.mdsl.repository.AccountTypeRepository;
//import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import com.mdsl.utils.enumerations.LoggingCategoriesEnum;
import com.mdsl.utils.enumerations.StatusEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountTypeService {
//	public final AccountTypeRepository accountTypeRepository;
//	public final AccountRepository accountRepository;
//	public final InstitutionRepository institutionRepository;
//	public final UserService userService;
//	private final BackEndLogService backEndLogService;
//	public final AccountTypeMapper accountTypeMapper;  
//	
//	/*
//	 * Returns an account type from table MD_CFG_ACCOUNT_TYPE based on the account type id 
//	 */
//	public AccountTypeResponseDto getAccountTypeById(int accountTypeId){
//		AccountType accountType = accountTypeRepository.findById(accountTypeId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCTTYPE, HttpStatus.NOT_FOUND)); 
//		return accountTypeMapper.toDto(accountType); 
//	}
//	
//	/*
//	 * Returns a list of all account types from table MD_CFG_ACCOUNT_TYPE 
//	 */
//	public List<AccountTypeResponseDto> getAllAccountTypes(){
//		List<AccountTypeResponseDto> listOfAccountTypeResponseDTO = new ArrayList<AccountTypeResponseDto>(); 
//		List<AccountType> accountTypes = accountTypeRepository.findAll(); 
//		
//		accountTypes.stream().forEach((accountType) -> {
//			listOfAccountTypeResponseDTO.add(accountTypeMapper.toDto(accountType));
//		});
//		Validations.isEmpty(listOfAccountTypeResponseDTO);
//		return listOfAccountTypeResponseDTO; 
//	}
//	
//	/*
//	 * Returns a list of all active account types (status = 1) from table MD_CFG_ACCOUNT_TYPE 
//	 */
//	public List<AccountTypeResponseDto> getActiveAccountTypes(){
//		List<AccountTypeResponseDto> listOfAccountTypeResponseDTO = new ArrayList<AccountTypeResponseDto>(); 
//		List<AccountType> accountTypes = accountTypeRepository.findByStatusOrderByAccountType(StatusEnum.ENABLED.getValue()); 
//		
//		accountTypes.stream().forEach((accountType) -> {
//			listOfAccountTypeResponseDTO.add(accountTypeMapper.toDto(accountType));
//		});
//		Validations.isEmpty(listOfAccountTypeResponseDTO);
//		return listOfAccountTypeResponseDTO; 
//	}
//	
//	/*
//	 * Returns a list of all active account types (status = 1) from table MD_CFG_ACCOUNT_TYPE 
//	 * for a specific institution based on institution id 
//	 */
//	public List<AccountTypeResponseDto> getActiveAccountTypesByInst(int instId){
//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		
//		List<AccountTypeResponseDto> listOfAccountTypeResponseDTO = new ArrayList<AccountTypeResponseDto>(); 
//		List<AccountType> accountTypes = accountTypeRepository.findByStatusAndInstitutionOrderByAccountType(StatusEnum.ENABLED.getValue(), institution); 
//		
//		accountTypes.stream().forEach((accountType) -> {
//			listOfAccountTypeResponseDTO.add(accountTypeMapper.toDto(accountType));
//		});
//		Validations.isEmpty(listOfAccountTypeResponseDTO);
//		return listOfAccountTypeResponseDTO; 
//	}
//	
//	/*
//	 * Returns a list of all account types from table MD_CFG_ACCOUNT_TYPE 
//	 * for a specific institution based on institution id 
//	 */
//	public List<AccountTypeResponseDto> getAccountTypeByInstId(int instId){
//		List<AccountTypeResponseDto> listOfAccountTypeResponseDTO = new ArrayList<AccountTypeResponseDto>(); 
//		Institution institution = institutionRepository.findById(instId).orElseThrow(() -> new BusinessException (ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		
//		List<AccountType> accountTypes = accountTypeRepository.findByInstitutionOrderByAccountType(institution);
//		
//		accountTypes.stream().forEach((accountType) -> {
//			listOfAccountTypeResponseDTO.add(accountTypeMapper.toDto(accountType));
//		});
//		Validations.isEmpty(listOfAccountTypeResponseDTO);
//		return listOfAccountTypeResponseDTO; 
//	}
//	
//	/*
//	 * Saves or created an account type based on the account type id
//	 * If account type id is not available or is equal to 0, we create
//	 * If account type id is available we update 
//	 * The account type code is unique
//	 * The transactions are logged in table MD_ADT_BKD_LOG  
//	 */
//	public AccountTypeResponseDto saveAccountType(AccountTypeRequestDto accountTypeRequestDto, String remoteAddress){
//		String action = ""; 
//		String description = ""; 
//		AccountType saveAccountType; 
//		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Institution institution = institutionRepository.findById(accountTypeRequestDto.getInstId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND)); 
//		
//		if (Objects.isNull(accountTypeRequestDto.getAccountTypeId())) {//create account type 
//			if (existsByAccountTypeInstitution(accountTypeRequestDto.getAccountType(), institution)){ 
//				throw new BusinessException(ResponseCode.CFG_ACCTTYPE_CODE_ALREADY_EXIST, HttpStatus.CONFLICT);
//			}
//			
//			saveAccountType = accountTypeMapper.toEntity(accountTypeRequestDto); 
//			saveAccountType.setInstitution(institution);
//			saveAccountType.setCreatedBy(userService.getEntityUserById(userDetails.getId()));
//			saveAccountType.setCreatedDate(new Timestamp(System.currentTimeMillis())); 
//			action = "create"; 
//			description = "Created Account Type [" + accountTypeRequestDto.getAccountType() + "]";
//		}else {//update account type 
//			AccountType accountType = accountTypeRepository.findById(accountTypeRequestDto.getAccountTypeId()).orElseThrow(()-> new BusinessException(ResponseCode.CFG_INVALID_ACCTTYPE, HttpStatus.NOT_FOUND)); 
//			
//			saveAccountType = accountTypeMapper.toEntity(accountTypeRequestDto); 
//			saveAccountType.setInstitution(institution);
//			saveAccountType.setCreatedBy(accountType.getCreatedBy());
//			saveAccountType.setCreatedDate(accountType.getCreatedDate()); 
//			saveAccountType.setUpdatedBy(userService.getEntityUserById(userDetails.getId()));
//			saveAccountType.setUpdatedDate(new Timestamp(System.currentTimeMillis())); 
//			action = "update"; 
//			description = "Update Account Type [" + accountTypeRequestDto.getAccountType() + "]";
//		}
//		
//		saveAccountType = accountTypeRepository.save(saveAccountType);
//		backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.CONFIGURATION.getValue(), ("AccountType-" + action), description, remoteAddress, accountTypeRequestDto.toString());
//		
//		return  accountTypeMapper.toDto(saveAccountType); 
//	}
//
//	/*
//	 * Deletes an account type from table MD_CFG_ACCOUNT_TYPE based on the account type id
//	 * Logs the transaction in table MD_ADT_BKD_LOG
//	 */
//	public void deleteAccountType(int accountTypeId, String remoteAddress) {
//		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		AccountType accountType = accountTypeRepository.findById(accountTypeId).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCTTYPE, HttpStatus.NOT_FOUND)); 
//		
//		if(accountRepository.existsByAccountType(accountType)) {
//			throw new BusinessException(ResponseCode.CFG_ACCTTYPE_NO_DELETE, HttpStatus.CONFLICT); 
//		}
//	
//		accountTypeRepository.delete(accountType);
//		backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.CONFIGURATION.getValue(), ("AccountType-delete"), "Deleted account type [" + accountType.getAccountType() + "]", remoteAddress, "");
//	}
//
//	/*
//	 * Enables/disables an account type by changing the status
//	 * Logs the transaction in table MD_ADT_BKD_LOG
//	 */	
//	public void changeAccountTypeStatus (ChangeStatusRequestDto changeAccountTypeStatus, String remoteAddress) {
//		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		AccountType accountType = accountTypeRepository.findById(changeAccountTypeStatus.getId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACCTTYPE, HttpStatus.NOT_FOUND));
//		
//		if (Character.compare(changeAccountTypeStatus.getStatus().charAt(0), accountType.getStatus().charAt(0)) == 0) {
//			throw new BusinessException(ResponseCode.CFG_ACCTTYPE_STATUS_NOT_CHANGED, HttpStatus.CONFLICT);
//		}
//		
//		accountTypeRepository.updateAccountTypeStatus(changeAccountTypeStatus.getId(), changeAccountTypeStatus.getStatus().charAt(0));
//		backEndLogService.logBackEndActivity(userService.getEntityUserById(userDetails.getId()), LoggingCategoriesEnum.CONFIGURATION.getValue(), ("AccountType-status-change"), "Status changed for account type  [" + accountType.getAccountType() + "]", remoteAddress, changeAccountTypeStatus.toString());
//	}
//	
//	public boolean existsByAccountTypeInstitution (String accountType, Institution institution) {
//		return accountTypeRepository.existsByAccountType(accountType); 
//	}
}