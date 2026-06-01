package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.EntityFilterRequestDto;
import com.mdsl.model.dto.request.EntityRequestDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.entity.*;
import com.mdsl.model.mapper.EntityMapper;
import com.mdsl.repository.*;
import com.mdsl.swtch.model.dto.request.SwitchEntitiesRequestDto;
import com.mdsl.swtch.service.SwitchEntitiesService;
import com.mdsl.swtch.service.SwitchEntityAddressService;
import com.mdsl.swtch.service.SwitchTerminalService;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.EntityLevelEnum;
import com.mdsl.utils.enumerations.StatusEnum;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntitiesServices {

	private final EntitiesRepository entityRepo;

	private final EntityMapper entityMapper;

	private final InstitutionRepository institutionRepository;

	private final MCCListRepository mccListRepository;

	private final EntityLevelsRepository entityLevelsRepository;

	private final ActivityPackageRepository activityPackageRepository;

	private final CurrencyRepository currencyRepository;

	private final NonActivityPackageRepository nonactivityFeePKGRepository;

	private final BankCodeRepository bankCodeRepository;

	private final EmployeeRepository employeeRepository;

	private final AddressRepository addressRepository;

	private final TerminalRepository terminalRepository;

	private final PaymentAccountRepository paymentAccountRepository;

	private final ContactRepository contactRepository;
	
	private final SystemCodeRepository systemCodeRepository;
	
	private final SwitchEntitiesService switchEntitiesService;
	
	private final SwitchEntityAddressService switchEntityAddressService;
	
	private final SwitchTerminalService switchTerminalService;

	private static final Logger logger = LoggerFactory.getLogger(EntitiesServices.class);

	public List<EntitiesResponseDto> getAllEntities() {
		List<Entities> allEntities = entityRepo.findAll(Sort.by(Sort.Direction.ASC, "entityId"));
		List<EntitiesResponseDto> allEntitiesResponseDto = new ArrayList<EntitiesResponseDto>();

		allEntities.forEach((entities) -> {
			EntitiesResponseDto entityResponseDto = entityMapper.toDto(entities);
//			if (!entityResponseDto.getParentId().equals("")) {
//				entityResponseDto.setParentName(getParentEntityName(entityResponseDto.getParentId()));
//			} else {
//				entityResponseDto.setParentName("");
//			}

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entities.getEntityLevels(), entities.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				entityResponseDto.setMccName(mccList.get(0).getMcc());
				entityResponseDto.setMccId(mccList.get(0).getMccId());
				entityResponseDto.setMccDescription(mccList.get(0).getDescription());
				entityResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				entityResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				entityResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}

			allEntitiesResponseDto.add(entityResponseDto);
		});
		return allEntitiesResponseDto;
	}

	public ResponseEntity<PaginationResponseDto> viewEntities(EntityRequestDto entityRequestDto) {

		Page<Entities> page = null;
//
//		Sort sort = null;
//
//		if (terminalRequestDto.getSort().isEmpty()) {
//			sort = Sort.by("terminalId").ascending();
//		} else {
//			for (SortDTO sortDto : terminalRequestDto.getSort()) {
//				if (null == sort) {
//					sort = Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
//							TerminalSortColumnEnum.valueOf(sortDto.getColumn()).getValue());
//				} else {
//					sort = sort.and(Sort.by(Direction.fromString(sortDto.getSortOrder().name()),
//							TerminalSortColumnEnum.valueOf(sortDto.getColumn()).getValue()));
//				}
//			}
//		}
//
//		PageRequest pageRequest = PageRequest.of(terminalRequestDto.getPageNo(), terminalRequestDto.getPageSize(),
//				sort);

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(entityRequestDto.getSort(),
				"entityId", entityRequestDto.getPageNo(), entityRequestDto.getPageSize());

		page = entityRepo.findAll(pageRequest);

		// List<Entities> entities = new ArrayList<Entities>();

		List<EntitiesResponseDto> allEntitiesResponseDto = new ArrayList<EntitiesResponseDto>();

		page.getContent().forEach((entities) -> {

			EntitiesResponseDto entityResponseDto = entityMapper.toDto(entities);
//			if (!entityResponseDto.getParentId().equals("")) {
//				entityResponseDto.setParentName(getParentEntityName(entityResponseDto.getParentId()));
//			} else {
//				entityResponseDto.setParentName("");
//			}

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entities.getEntityLevels(), entities.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				entityResponseDto.setMccName(mccList.get(0).getMcc());
				entityResponseDto.setMccId(mccList.get(0).getMccId());
				entityResponseDto.setMccDescription(mccList.get(0).getDescription());
				entityResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				entityResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				entityResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}

			allEntitiesResponseDto.add(entityResponseDto);
		});
//			TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);
//			terminalResponseDto.setPageNo(terminalRequestDto.getPageNo());
//			terminalResponseDto.setPageSize(terminalRequestDto.getPageSize());
//			 
//			terminalDtos.add(terminalResponseDto);

		return new ResponseEntity<PaginationResponseDto>(new PaginationResponseDto(true, null, allEntitiesResponseDto,
				page.getTotalPages(), page.getTotalElements()), HttpStatus.OK);

//		System.out.println(page.getTotalPages()+"---------------" + page.getTotalElements());

//		return terminalDtos;
	}

	private String getParentEntityName(String parentId) {
		Entities parentEntity = entityRepo.findById(String.valueOf(parentId)).orElse(null);
		if (parentEntity != null) {
			return parentEntity.getEntityName();
		}
		return "";
	}

	@Transactional
	public EntitiesResponseDto getEntityById(String entityId,String instId) {
		try {
		Entities entity = entityRepo.findByEntityIdAndInstitution_InstitutionId(entityId,instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		EntitiesResponseDto entitiesResponseDto = entityMapper.toDto(entity);
		List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
				entity.getEntityLevels(), entity.getInstitution().getInstitutionId());
		
		List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());
		if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
			entitiesResponseDto.setMccName(mccList.get(0).getMcc());
			entitiesResponseDto.setMccId(mccList.get(0).getMccId());
			entitiesResponseDto.setMccDescription(mccList.get(0).getDescription());
			entitiesResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
			entitiesResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
			entitiesResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
		}
		if(entity.getActivityFeePKG() != null){
			entitiesResponseDto.setActivityPackageIdRecordSeqId(entity.getActivityFeePKGEntity().getRecordSeqId());
		}
		return entitiesResponseDto;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public EntitiesResponseDto saveOrUpdateEntity(EntityRequestDto entityRequestDto) {
		Entities entities;
		Entities parentEntity = null;
		ActivityPackage activityFeePKG = null;
		NonActivityPackage nonactivityFeePKG = null;
		EntitiesResponseDto entitiesResponseDto;
		Currency currency = null;
		BankCode bankCode = null;
		List<Entities> entitiesCount = new ArrayList<>();
		
		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Institution institution = institutionRepository.findById(entityRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		MCCList mccList = mccListRepository.findById(entityRequestDto.getMccId())
				.orElseThrow(() -> new BusinessException(ResponseCode.MCC_NOT_FOUND, HttpStatus.NOT_FOUND));


		SystemCode businessType = systemCodeRepository.findById(entityRequestDto.getBusinessTypeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		EntityLevels entityLevels = entityLevelsRepository.findById(entityRequestDto.getEntityLevelId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (entityRequestDto.getActivityPackageId() != null) {
			activityFeePKG = activityPackageRepository.findById(entityRequestDto.getActivityPackageId()).orElseThrow(
					() -> new BusinessException(ResponseCode.CFG_ACT_FEE_PKG_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		if (!entityRequestDto.getNonActivityPackageId().isEmpty()) {
			nonactivityFeePKG = nonactivityFeePKGRepository.findByPackageIdAndInstitution_institutionId(entityRequestDto.getNonActivityPackageId(),institution.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACTIVITY_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (entityRequestDto.getBankCodeId() != 0) {
			bankCode = bankCodeRepository.findById(entityRequestDto.getBankCodeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		Employee salesman = employeeRepository.findById(entityRequestDto.getSalesmanId())
				.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
		Employee employeeIncharge = employeeRepository.findById(entityRequestDto.getEmployeeInchargeId())
				.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (!entityRequestDto.getParentId().isEmpty()) {
			parentEntity = entityRepo.findByEntityIdAndInstitution(String.valueOf(entityRequestDto.getParentId()),institution).orElseThrow(
					() -> new BusinessException(ResponseCode.CFG_PARENT_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (entityRequestDto.getDefSettlementCurrency() != 0) {
			currency = currencyRepository.findById(entityRequestDto.getDefSettlementCurrency())
					.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		Optional<Entities> entity = entityRepo.findByEntityIdAndInstitution(entityRequestDto.getEntityId(),institution);

		if (entity.isPresent() && (String.valueOf(entityRequestDto.getUpdateFlag()).equals("1"))) {
			if (entityRequestDto.getStatus() != '0') {

				List<Entities> tempCount = entityRepo
						.findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatusAndEntityId(
								entityRequestDto.getInstitutionId(), entityRequestDto.getEntityName(),
								entityRequestDto.getEntityStatus().charAt(0), entityRequestDto.getStatus(),
								entityRequestDto.getEntityId());
				if (tempCount.size() != 1) {
					entitiesCount = entityRepo
							.findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatus(
									entityRequestDto.getInstitutionId(), entityRequestDto.getEntityName(),
									entityRequestDto.getEntityStatus().charAt(0), entityRequestDto.getStatus());
				}
			}

			if (entitiesCount.isEmpty()) {
				entities = entityRepo.findByEntityIdAndInstitution(entityRequestDto.getEntityId(),institution).orElseThrow(
						() -> new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
				entities.setInstitution(institution);
				entities.setEntityName(entityRequestDto.getEntityName());
				entities.setEntityNameAlt(entityRequestDto.getEntityNameAlt());
				entities.setDbaName(entityRequestDto.getDbaName());
				entities.setDbaNameAlt(entityRequestDto.getDbaNameAlt());
				entities.setDefaultMCC(mccList.getMcc());
				entities.setActualStartDate(entityRequestDto.getActualStartDate());
				entities.setCompanyRegisterNBR(entityRequestDto.getCompanyRegisterNBR());
				entities.setCompanyVatNBR(entityRequestDto.getCompanyVatNBR());
				entities.setContractDate(entityRequestDto.getContractDate());
				entities.setDefAccountNumber(entityRequestDto.getDefAccountNumber());
				entities.setDefIBAN(entityRequestDto.getDefIBAN());
				entities.setExpStartDate(entityRequestDto.getExpStartDate());
				entities.setLastTransDate(entities.getLastTransDate());
				entities.setTerminationDate(entityRequestDto.getTerminationDate());
				
                if (parentEntity != null) {
                    entities.setParentId(parentEntity.getEntityId());
                    entities.setParentIdEntity(parentEntity);
                } else {
                    entities.setParentId(null);
                    entities.setParentIdEntity(null);
                }
				entities.setBusinessType(businessType);
				entities.setEntityLevels(entityLevels.getHierarchyLevel());
				
				entities.setActivityFeePKGEntity(activityFeePKG);
				if(Objects.nonNull(activityFeePKG)) {
					entities.setActivityFeePKG(activityFeePKG.getPackageId());
				}else {
					entities.setActivityFeePKG(null);
				}
				
				entities.setNonactivityFeePKGEntity(nonactivityFeePKG);
				if(Objects.nonNull(nonactivityFeePKG)) {
					entities.setNonactivityFeePKG(nonactivityFeePKG.getPackageId());
				}else {
					entities.setNonactivityFeePKG(null);
				}
								
				entities.setDefBankCodeEntity(bankCode);
				if(Objects.nonNull(bankCode)) {
				entities.setDefBankCode(bankCode.getBankCode());
				}else {
					entities.setDefBankCode(null);
				}
				entities.setSalesman(salesman);
				entities.setEmployeeIncharge(employeeIncharge);
				entities.setDefaultSettlementCurrency(currency);
				entities.setAddValueDateDays(entityRequestDto.getAddValueDateDays());
				entities.setPaymentFrequency(entityRequestDto.getPaymentFrequency());
				entities.setPaymentMethod(entityRequestDto.getPaymentMethod());
				entities.setOnHoldInd(entityRequestDto.getOnHoldInd().charAt(0));
				entities.setHotMerchantFlag(entityRequestDto.getHotMerchantFlag().charAt(0));
				entities.setEntityStatus(entityRequestDto.getEntityStatus().charAt(0));
				entities.setAssociatedPayment(entityRequestDto.getAssociatedPayment().charAt(0));
				entities.setStatementType(entityRequestDto.getStatementType().charAt(0));
				entities.setEStatementToEntity(entityRequestDto.getEStatementToEntity().charAt(0));
                entities.setStatus(entityRequestDto.getStatus());
				if(entityRequestDto.getAcctTemplateHdrId() == 0) {
					entities.setAcctTemplateHdrId(null);
				}
				else {
					entities.setAcctTemplateHdrId(entityRequestDto.getAcctTemplateHdrId());
				}
				
				entities = entityRepo.save(entities);
				
				if(switchSystemCode.getCodeSuffix().equals("1")) {
					this.saveSwitchEntity(entities);
				}
				
				entitiesResponseDto = entityMapper.toDto(entities);

				entitiesResponseDto.setMccName(mccList.getMcc());
				entitiesResponseDto.setMccId(mccList.getMccId());
				entitiesResponseDto.setMccDescription(mccList.getDescription());
				entitiesResponseDto.setEntityLevelId(entityLevels.getEntityLevelId());
				entitiesResponseDto.setHierarchyLevel(entityLevels.getHierarchyLevel());
				entitiesResponseDto.setTypeDescription(entityLevels.getTypeDescription());

			} else {
				throw new BusinessException(ResponseCode.CFG_ENTITY_NAME_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}

			return entitiesResponseDto;
		} else {
			entityRequestDto.setAcctTemplateHdrId(null);
			if (entity.isPresent() || (String.valueOf(entityRequestDto.getUpdateFlag()).equals("1"))) {
				throw new BusinessException(ResponseCode.CFG_ENTITY_ID_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}

			if (entityRequestDto.getEntityId().equals("0")) {
				throw new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
			} else {
				if (entityRequestDto.getStatus() == '1') {


					entitiesCount = entityRepo
							.findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatus(
									entityRequestDto.getInstitutionId(), entityRequestDto.getEntityName(),
									entityRequestDto.getEntityStatus().charAt(0), '1');
				}
				if (entitiesCount.isEmpty()) {


					entities = entityMapper.toEntity(entityRequestDto);
					entities.setDateCreate(new java.sql.Date(new java.util.Date().getTime()));
					entities.setInstitution(institution);
					entities.setDefaultMCC(mccList.getMcc());
					
					entities.setActivityFeePKGEntity(activityFeePKG);
					if(Objects.nonNull(activityFeePKG)) {
						entities.setActivityFeePKG(activityFeePKG.getPackageId());
					}else {
						entities.setActivityFeePKG(null);
					}
					entities.setNonactivityFeePKGEntity(nonactivityFeePKG);
					if(Objects.nonNull(nonactivityFeePKG)) {
						entities.setNonactivityFeePKG(nonactivityFeePKG.getPackageId());
					}else {
						entities.setNonactivityFeePKG(null);
					}					entities.setBusinessType(businessType);
					entities.setEntityLevels(entityLevels.getHierarchyLevel());
					
					entities.setDefBankCodeEntity(bankCode);
					if(Objects.nonNull(bankCode)) {
					entities.setDefBankCode(bankCode.getBankCode());
					}else {
						entities.setDefBankCode(null);
					}
					entities.setSalesman(salesman);
					
	                if (parentEntity != null) {
	                    entities.setParentId(parentEntity.getEntityId());
	                    entities.setParentIdEntity(parentEntity);
	                } else {
	                    entities.setParentId(null);
	                    entities.setParentIdEntity(null);
	                }

					entities.setEmployeeIncharge(employeeIncharge);
					entities.setDefaultSettlementCurrency(currency);
					entities.setAddValueDateDays(entityRequestDto.getAddValueDateDays());
					entities.setOnHoldInd(entityRequestDto.getOnHoldInd().charAt(0));
					entities.setHotMerchantFlag(entityRequestDto.getHotMerchantFlag().charAt(0));
					entities.setEntityStatus(entityRequestDto.getEntityStatus().charAt(0));
					entities.setAssociatedPayment(entityRequestDto.getAssociatedPayment().charAt(0));
					entities.setStatementType(entityRequestDto.getStatementType().charAt(0));
					entities.setEStatementToEntity(entityRequestDto.getEStatementToEntity().charAt(0));
                    entities.setStatus(entityRequestDto.getStatus());
                    entities.setIsCloned((byte) 0);
					int nextValRecordSeqId = entityRepo.findEntitySeqNextValue();
					entities.setRecord_seq_id(nextValRecordSeqId);
					if(userDetails!=null) {
						entities.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
					}
					
					entities = entityRepo.save(entities);
					
					if(switchSystemCode.getCodeSuffix().equals("1")) {
						this.saveSwitchEntity(entities);
					}
					entitiesResponseDto = entityMapper.toDto(entities);

					entitiesResponseDto.setMccName(mccList.getMcc());
					entitiesResponseDto.setMccId(mccList.getMccId());
					entitiesResponseDto.setMccDescription(mccList.getDescription());
					entitiesResponseDto.setEntityLevelId(entityLevels.getEntityLevelId());
					entitiesResponseDto.setHierarchyLevel(entityLevels.getHierarchyLevel());
					entitiesResponseDto.setTypeDescription(entityLevels.getTypeDescription());
				} else {
					throw new BusinessException(ResponseCode.CFG_ENTITY_NAME_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
				}

				return entitiesResponseDto;
			}
		}
	}

	public void deleteEntity(@RequestParam String entityId,String instId) throws Exception {
		entityRepo.findByEntityIdAndInstitution_InstitutionId(entityId,instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITY_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_INSTITUTION_ID, HttpStatus.NOT_FOUND));

		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<Contact> contacts = contactRepository.findContactsByEntityAndInstitution(entityId,institution,
				Sort.by(Sort.Direction.ASC, "contactId"));
		contacts.forEach((contact1) -> {
			contactRepository.delete(contact1);
		});

		try {
			List<Address> addresses = addressRepository.findAddressByEntityId(entityId,
					Sort.by(Sort.Direction.ASC, "addressId"));
			addresses.forEach((newAddress) -> {
				addressRepository.delete(newAddress);
				if (switchSystemCode.getCodeSuffix().equals("1")) {
					this.switchEntityAddressService.deleteEntityAddress(entityId);
				}
			});
		} catch (Exception e) {
			System.out.println(e);
            throw new RuntimeException(e);
        }


        List<Terminal> terminals = terminalRepository.findByEntitiesObject(entityId,
				Sort.by(Sort.Direction.ASC, "terminalId"));
		terminals.forEach((terminal1) -> {
			terminalRepository.delete(terminal1);
			if(switchSystemCode.getCodeSuffix().equals("1")) {
				this.switchTerminalService.deleteTerminal(terminal1.getTerminalId());
			}
		});

		List<PaymentAccount> paymentAccounts = paymentAccountRepository.findPaymentAccountsByEntityId(entityId);
		paymentAccounts.forEach((paymentAccount1) -> {
			paymentAccountRepository.delete(paymentAccount1);
		});
		entityRepo.deleteById(entityId);
		
		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.switchEntitiesService.deleteEntity(entityId);
		}
	}

	public ResponseEntity<PaginationResponseDto> getEntitiesByInstitution(EntityRequestDto entitiesRequestDto) {
//		List<Entities> allInstitutionEntities = entityRepo.findByInstitution(institution);
//		List<EntitiesResponseDto> allEntitiesResponseDto = new ArrayList<EntitiesResponseDto>();
//		allInstitutionEntities.stream().forEach((entities) -> {
//			EntitiesResponseDto entitiesResponseDto = entityMapper.toDto(entities);
//			if (entitiesResponseDto.getParentId() > 0) {
//				entitiesResponseDto.setParentName(getParentEntityName(entitiesResponseDto.getParentId()));
//			} else {
//				entitiesResponseDto.setParentName("");
//			}
//			allEntitiesResponseDto.add(entitiesResponseDto);
//		});
//		return allEntitiesResponseDto;

		Page<Entities> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(entitiesRequestDto.getSort(),
				"entityId", entitiesRequestDto.getPageNo(), entitiesRequestDto.getPageSize());

		page = entityRepo.findByInstitution_InstitutionId(pageRequest, entitiesRequestDto.getInstitutionId());

		List<EntitiesResponseDto> allEntitiesDto = new ArrayList<EntitiesResponseDto>();

		page.getContent().forEach((entities) -> {

			EntitiesResponseDto entitiesResponseDto = entityMapper.toDto(entities);

//			if (!entitiesResponseDto.getParentId().equals("")) {
//				entitiesResponseDto.setParentName(getParentEntityName(entitiesResponseDto.getParentId()));
//			} else {
//				entitiesResponseDto.setParentName("");
//			}

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entities.getEntityLevels(), entities.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				entitiesResponseDto.setMccName(mccList.get(0).getMcc());
				entitiesResponseDto.setMccId(mccList.get(0).getMccId());
				entitiesResponseDto.setMccDescription(mccList.get(0).getDescription());
				entitiesResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				entitiesResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				entitiesResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}
			entitiesResponseDto.setPageNo(entitiesRequestDto.getPageNo());
			entitiesResponseDto.setPageSize(entitiesRequestDto.getPageSize());
			allEntitiesDto.add(entitiesResponseDto);
		});

		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, allEntitiesDto, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

	@Transactional(rollbackOn = Exception.class)
	public EntitiesResponseDto cloneEntity(String id, String instId) throws Exception {

		Entities entity12 = null;
	    List<Entities> entities = new ArrayList<>();
	    int max = 0;
	    int count = 1;
	    Entities entity1 = entityRepo.findByEntityIdAndInstitution_InstitutionId(id, instId)
	            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
	    if (id.contains("_C")) {
	        entity12 = entityRepo.findByEntityIdAndInstitution_InstitutionId(id,instId).get();
	    } else {
	        entities = entityRepo.findByEntityIdContaining((id + "_C"), Sort.by(Sort.Direction.DESC, "entityId"));

	        for (Entities e : entities) {
	            String[] temp = e.getEntityId().split("_C");
	            if (Integer.parseInt(temp[1]) > max) {
	                max = Integer.parseInt(temp[1]);
	            }
	        }
	    }

	    Entities entity = (Entities) entity1.clone();

	    entity.setStatus('0');
	    entity.setIsCloned((byte) 1);

	    if ((entity12 == null) && (entities.isEmpty())) {
	        entity.setEntityId(entity.getEntityId() + "_C" + count);
	    } else {
	        if (entity12 == null) {
	            entity.setEntityId(entity.getEntityId() + "_C" + (max + 1));
	        } else {
	            String[] tempId = id.split("_C");
	            entities = entityRepo.findByEntityIdContaining(tempId[0] + "_C",
	                    Sort.by(Sort.Direction.DESC, "entityId"));

	            for (Entities e : entities) {
	                String[] temp = e.getEntityId().split("_C");
	                if (Integer.parseInt(temp[1]) > max) {
	                    max = Integer.parseInt(temp[1]);
	                }
	            }
	            String[] cloneCount = entity12.getEntityId().split("_C");
	            entity.setEntityId(cloneCount[0] + "_C" + (max + 1));
	        }
	    }

		EntitiesResponseDto entitiesResponseDto = entityMapper.toDto(entity);

		List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
				entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

		List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

		if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {

			entitiesResponseDto.setMccName(mccList.get(0).getMcc());
			entitiesResponseDto.setMccId(mccList.get(0).getMccId());
			entitiesResponseDto.setMccDescription(mccList.get(0).getDescription());
			entitiesResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
			entitiesResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
			entitiesResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
		}
		return entitiesResponseDto;
	}

	public ResponseEntity<PaginationResponseDto> getEntitiesBySearch(EntityRequestDto entitiesRequestDto) {
		Page<Entities> page = null;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(entitiesRequestDto.getSort(),
				"entityId", entitiesRequestDto.getPageNo(), entitiesRequestDto.getPageSize());

		page = entityRepo.searchEntities(
		        pageRequest,
		        entitiesRequestDto.getInstitutionId(),
		        entitiesRequestDto.getSearch(),
		        entitiesRequestDto.getParentId(),
		        (entitiesRequestDto.getBusinessTypeId()==0) ? null : String.valueOf(entitiesRequestDto.getBusinessTypeId()) ,
		        (entitiesRequestDto.getMccId()==0) ? null : String.valueOf(entitiesRequestDto.getMccId()),
		        entitiesRequestDto.getEntityStatus(),
		        entitiesRequestDto.getEntityId(),
		        entitiesRequestDto.getFromDate(),
		        entitiesRequestDto.getToDate(),
		        (entitiesRequestDto.getEntityLevelId()==0) ? null :  String.valueOf(entitiesRequestDto.getEntityLevelId()),
		        entitiesRequestDto.getEntityName(),
		        entitiesRequestDto.getHotMerchantFlag()
		);
		List<EntitiesResponseDto> allEntitiesDto = new ArrayList<EntitiesResponseDto>();

		page.getContent().forEach((entity) -> {

			EntitiesResponseDto dto = entityMapper.toDto(entity);

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

			if (!mccList.isEmpty() && !entityLevels.isEmpty()) {
				dto.setMccName(mccList.get(0).getMcc());
				dto.setMccId(mccList.get(0).getMccId());
				dto.setMccDescription(mccList.get(0).getDescription());
				dto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				dto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				dto.setTypeDescription(entityLevels.get(0).getTypeDescription());

				dto.setPageNo(entitiesRequestDto.getPageNo());
				dto.setPageSize(entitiesRequestDto.getPageSize());
			}

			allEntitiesDto.add(dto);
		});

		return new ResponseEntity<PaginationResponseDto>(
				new PaginationResponseDto(true, null, allEntitiesDto, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);
	}

	public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto,String instId) {
	    // Fetch the root entity or throw an exception if not found
	    Entities rootEntity = entityRepo.findByEntityIdAndInstitution_InstitutionId(changeStatusRequestDto.getIdString(),instId)
	            .orElseThrow(() -> new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));

	    // Validate the root entity before changing the status
	    if (!this.validateEntityForChangeStatus(rootEntity)) {
	        throw new BusinessException(ResponseCode.CFG_CANNOT_ENABLE_CHILD_ENTITY, HttpStatus.BAD_REQUEST);
	    }

	    // Call the recursive method to set the status for the entire hierarchy
	    char status = changeStatusRequestDto.getStatus().charAt(0);
	    updateEntityStatusRecursively(rootEntity, status, changeStatusRequestDto.getChangeAllFlag());
	}

	private void updateEntityStatusRecursively(Entities entity, char status, String changeAllFlag) {
	    // Update the current entity's status
	    entity.setStatus(status);
	    entityRepo.save(entity);

	    if(changeAllFlag.equals("Y")) {
	    	// Fetch child entities of the current entity
		    List<Entities> childEntities = entityRepo.findByParentIdEntity_EntityId(entity.getEntityId());

		    // Recursively update the status of each child entity
		    for (Entities childEntity : childEntities) {
		        updateEntityStatusRecursively(childEntity, status, changeAllFlag);
		    }
	    }
	}
	
	public boolean validateEntityForChangeStatus(Entities entity) {
		if(Objects.nonNull(entity.getParentId()) && entity.getParentIdEntity().getStatus() == StatusEnum.DISABLED.getValue()) {
			return false;
		}
		return true;
	}

	public Boolean hasChildrenEntities(String entityId) {
		List<Entities> childEntities = entityRepo.findByParentIdEntity_EntityId(entityId);
		if(childEntities.isEmpty()) {
            return false;
        }
		return true;
	}

	public List<EntitiesResponseDto> getActiveEntities() {
		List<EntitiesResponseDto> allEntitiesCodesDto = new ArrayList<EntitiesResponseDto>();
		List<Entities> allEntitiesCodes = entityRepo.findByStatus('1',
				Sort.by(Sort.Direction.ASC, "entityId"));

		allEntitiesCodes.forEach((entities) -> {
			EntitiesResponseDto entityResponseDto = entityMapper.toDto(entities);

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entities.getEntityLevels(), entities.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				entityResponseDto.setMccName(mccList.get(0).getMcc());
				entityResponseDto.setMccId(mccList.get(0).getMccId());
				entityResponseDto.setMccDescription(mccList.get(0).getDescription());
				entityResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				entityResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				entityResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}

			allEntitiesCodesDto.add(entityResponseDto);
		});

		return allEntitiesCodesDto;
	}

	public List<EntitiesResponseDto> getEntitiesByInstitutionGet(String id) {
		List<EntitiesResponseDto> entitiesResponseDtos = new ArrayList<EntitiesResponseDto>();

		Institution institution = institutionRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		List<Entities> entities = entityRepo.findByInstitution_InstitutionId(institution.getInstitutionId(),
				Sort.by(Sort.Direction.ASC, "entityId"));

		entities.stream().forEach((entity) -> {
			EntitiesResponseDto dto = entityMapper.toDto(entity);

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				dto.setMccName(mccList.get(0).getMcc());
				dto.setMccId(mccList.get(0).getMccId());
				dto.setMccDescription(mccList.get(0).getDescription());
				dto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				dto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				dto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}

			entitiesResponseDtos.add(dto);
		});

		return entitiesResponseDtos;
	}

	public List<EntitiesResponseDto> getEntitiesByEntityLevel(EntityRequestDto entitiesRequestDto) {

		List<EntitiesResponseDto> entitiesResponseDtos = new ArrayList<EntitiesResponseDto>();

		List<Entities> entities = new ArrayList<Entities>();

		EntityLevels entityLevels = entityLevelsRepository.findById(entitiesRequestDto.getEntityLevelId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("CHAIN").getValue())) {

		} else if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("MERCHANT").getValue())) {
			List<EntityLevels> entityLevel = entityLevelsRepository
					.findByTypeDescription(EntityLevelEnum.valueOf("CHAIN").getValue());

			for (EntityLevels ent : entityLevel) {

				List<Entities> ent1 = entityRepo.findByEntityLevels(ent.getHierarchyLevel());

				entities.addAll(ent1);
			}

		} else if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("OUTLET").getValue())) {
			List<EntityLevels> entityLevel = entityLevelsRepository
					.findByTypeDescription(EntityLevelEnum.valueOf("MERCHANT").getValue());

			for (EntityLevels ent : entityLevel) {

				List<Entities> ent1 = entityRepo.findByEntityLevels(ent.getHierarchyLevel());

				entities.addAll(ent1);
			}
		}

		entities.stream().forEach((entity) -> {
			EntitiesResponseDto dto = entityMapper.toDto(entity);

			List<EntityLevels> entityLevels1 = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

			if (!(entityLevels1.isEmpty()) && !(mccList.isEmpty())) {
				dto.setMccName(mccList.get(0).getMcc());
				dto.setMccId(mccList.get(0).getMccId());
				dto.setMccDescription(mccList.get(0).getDescription());
				dto.setEntityLevelId(entityLevels1.get(0).getEntityLevelId());
				dto.setHierarchyLevel(entityLevels1.get(0).getHierarchyLevel());
				dto.setTypeDescription(entityLevels1.get(0).getTypeDescription());
			}

			entitiesResponseDtos.add(dto);
		});

		return entitiesResponseDtos;

	}

	public List<EntitiesResponseDto> getEntitiesByEntityLevelAndInstitution(EntityRequestDto entitiesRequestDto) {

		List<EntitiesResponseDto> entitiesResponseDtos = new ArrayList<EntitiesResponseDto>();

		List<Entities> entities = new ArrayList<Entities>();

		EntityLevels entityLevels = entityLevelsRepository.findById(entitiesRequestDto.getEntityLevelId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("CHAIN").getValue())) {

		} else if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("MERCHANT").getValue())) {
			EntityLevels entityLevel = entityLevelsRepository
					.findByTypeDescriptionAndInstitution_InstitutionId(EntityLevelEnum.valueOf("CHAIN").getValue(),
							entitiesRequestDto.getInstitutionId())
					.orElseThrow(
							() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

			entities = entityRepo.findByEntityLevelsAndInstitution_InstitutionId(entityLevel.getHierarchyLevel(),
					entitiesRequestDto.getInstitutionId());

		} else if (entityLevels.getTypeDescription().equalsIgnoreCase(EntityLevelEnum.valueOf("OUTLET").getValue())) {
			EntityLevels entityLevel = entityLevelsRepository
					.findByTypeDescriptionAndInstitution_InstitutionId(EntityLevelEnum.valueOf("MERCHANT").getValue(),
							entitiesRequestDto.getInstitutionId())
					.orElseThrow(
							() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));

			entities = entityRepo.findByEntityLevelsAndInstitution_InstitutionId(entityLevel.getHierarchyLevel(),
					entitiesRequestDto.getInstitutionId());
		}

		entities.stream().forEach((entity) -> {
			EntitiesResponseDto dto = entityMapper.toDto(entity);

			List<EntityLevels> entityLevels1 = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

			if (!(entityLevels1.isEmpty()) && !(mccList.isEmpty())) {
				dto.setMccName(mccList.get(0).getMcc());
				dto.setMccId(mccList.get(0).getMccId());
				dto.setMccDescription(mccList.get(0).getDescription());
				dto.setEntityLevelId(entityLevels1.get(0).getEntityLevelId());
				dto.setHierarchyLevel(entityLevels1.get(0).getHierarchyLevel());
				dto.setTypeDescription(entityLevels1.get(0).getTypeDescription());
			}

			entitiesResponseDtos.add(dto);
		});

		return entitiesResponseDtos;

	}

	public List<EntitiesResponseDto> getEntitiesByEntityLevelOutlet(EntityRequestDto entitiesRequestDto) {

		List<EntitiesResponseDto> entitiesResponseDtos = new ArrayList<EntitiesResponseDto>();

		List<Entities> entities = new ArrayList<Entities>();

		Optional<EntityLevels> entityLevels = entityLevelsRepository.findByTypeDescriptionAndInstitution_InstitutionId(
				entitiesRequestDto.getEntityLevel(), entitiesRequestDto.getInstitutionId());
//				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ENTITYLEVELS_CODE, HttpStatus.NOT_FOUND));

		List<Entities> entityList = entityRepo.findByEntityLevelsAndInstitution_InstitutionId(entityLevels.get().getHierarchyLevel(), entitiesRequestDto.getInstitutionId());

		entityList.stream().forEach((entity) -> {
			EntitiesResponseDto dto = entityMapper.toDto(entity);

			List<EntityLevels> entityLevels1 = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entity.getEntityLevels(), entity.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entity.getDefaultMCC());

			if (!(entityLevels1.isEmpty()) && !(mccList.isEmpty())) {
				dto.setMccName(mccList.get(0).getMcc());
				dto.setMccId(mccList.get(0).getMccId());
				dto.setMccDescription(mccList.get(0).getDescription());
				dto.setEntityLevelId(entityLevels1.get(0).getEntityLevelId());
				dto.setHierarchyLevel(entityLevels1.get(0).getHierarchyLevel());
				dto.setTypeDescription(entityLevels1.get(0).getTypeDescription());
			}

			entitiesResponseDtos.add(dto);
		});

		return entitiesResponseDtos;

	}
	
	public List<EntitiesResponseDto> getEntitiesBySearchCriteria(EntityFilterRequestDto entityFilterRequestDto) {
		Institution instituion = this.institutionRepository.findById(entityFilterRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<Entities> allEntities = entityRepo.searchByFilters(instituion.getInstitutionId(), entityFilterRequestDto.getEntityId(),
				entityFilterRequestDto.getParentId(), entityFilterRequestDto.getMcc(), entityFilterRequestDto.getEntityName(),
				Sort.by(Sort.Direction.ASC, "entityId"));
		List<EntitiesResponseDto> allEntitiesResponseDto = new ArrayList<EntitiesResponseDto>();
		
		allEntities.stream().forEach((entities) -> {
			EntitiesResponseDto entityResponseDto = entityMapper.toDto(entities);

			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevelAndInstitution_InstitutionId(
					entities.getEntityLevels(), entities.getInstitution().getInstitutionId());

			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());

			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
				entityResponseDto.setMccName(mccList.get(0).getMcc());
				entityResponseDto.setMccId(mccList.get(0).getMccId());
				entityResponseDto.setMccDescription(mccList.get(0).getDescription());
				entityResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
				entityResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
				entityResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
			}

			allEntitiesResponseDto.add(entityResponseDto);
		});
		return allEntitiesResponseDto;
	}
	
	public void saveSwitchEntity(Entities entity) {
		SwitchEntitiesRequestDto switchEntitiesRequestDto = new SwitchEntitiesRequestDto();
		switchEntitiesRequestDto.setActualStartDate(entity.getActualStartDate());
		switchEntitiesRequestDto.setCreationCreate(entity.getDateCreate());
		switchEntitiesRequestDto.setDefaultMcc(entity.getDefaultMCC());
		
		if(Objects.nonNull(entity.getDbaName())) {
			switchEntitiesRequestDto.setEntityDbaName(entity.getDbaName());
		}
		
		switchEntitiesRequestDto.setEntityId(entity.getEntityId());
		switchEntitiesRequestDto.setEntityLevel(entity.getEntityLevels());
		switchEntitiesRequestDto.setEntityName(entity.getEntityName());
		switchEntitiesRequestDto.setEntityStatus(entity.getEntityStatus());
		switchEntitiesRequestDto.setInstitutionId(entity.getInstitution().getInstitutionId());
		
		if(!Objects.isNull(entity.getParentId())) {
			switchEntitiesRequestDto.setParentEntityId(entity.getParentIdEntity().getEntityId());
		}
		
		if(!Objects.isNull(entity.getTerminationDate())) {
			switchEntitiesRequestDto.setTerminationDate(entity.getTerminationDate());
		}
		
		this.switchEntitiesService.saveEntity(switchEntitiesRequestDto);
	}

	public Entities createClonedEntity(EntityRequestDto entityRequestDto) {
		UserDetailsImpl userDetails = getCurrentUser();
		Institution institution = getInstitution(entityRequestDto.getInstitutionId());
		MCCList mccList = getMCCList(entityRequestDto.getMccId());
		SystemCode businessType = getBusinessType(entityRequestDto.getBusinessTypeId());
		EntityLevels entityLevels = getEntityLevels(entityRequestDto.getEntityLevelId());
		ActivityPackage activityFeePKG = getActivityPackage(entityRequestDto.getActivityPackageId());
		NonActivityPackage nonActivityFeePKG = getNonActivityPackage(entityRequestDto.getNonActivityPackageId(), institution);
		BankCode bankCode = getBankCode(entityRequestDto.getBankCodeId());
		Employee salesman = getEmployee(entityRequestDto.getSalesmanId());
		Employee employeeIncharge = getEmployee(entityRequestDto.getEmployeeInchargeId());
		Entities parentEntity = getParentEntity(entityRequestDto.getParentId(),institution);
		Currency currency = getCurrency(entityRequestDto.getDefSettlementCurrency());

		validateEntityCreation(entityRequestDto);

		List<Entities> existingEntities = entityRepo
				.findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatusAndEntityId(
						entityRequestDto.getInstitutionId(), entityRequestDto.getEntityName(),
						entityRequestDto.getEntityStatus().charAt(0), '1',entityRequestDto.getEntityId());

		if (!existingEntities.isEmpty()) {
			throw new BusinessException(ResponseCode.CFG_ENTITY_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
		}

		Entities entities = entityMapper.toEntity(entityRequestDto);
		populateEntityFields(entities, entityRequestDto, institution, mccList, activityFeePKG,
				nonActivityFeePKG, businessType, entityLevels, bankCode,
				salesman, parentEntity, employeeIncharge, currency, userDetails);

		return entities;
	}

	// Helper Methods
	private UserDetailsImpl getCurrentUser() {
		return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private Institution getInstitution(String institutionId) {
		return institutionRepository.findById(institutionId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private MCCList getMCCList(int mccId) {
		return mccListRepository.findById(mccId)
				.orElseThrow(() -> new BusinessException(ResponseCode.MCC_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private SystemCode getBusinessType(int businessTypeId) {
		return systemCodeRepository.findById(businessTypeId)
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private EntityLevels getEntityLevels(int entityLevelId) {
		return entityLevelsRepository.findById(entityLevelId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private ActivityPackage getActivityPackage(Integer activityPackageId) {
		if (activityPackageId == null) return null;
		return activityPackageRepository.findById(activityPackageId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACT_FEE_PKG_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private NonActivityPackage getNonActivityPackage(String nonActivityPackageId, Institution institution) {
		if (nonActivityPackageId.isEmpty()) return null;
		return nonactivityFeePKGRepository.findByPackageIdAndInstitution_institutionId(nonActivityPackageId, institution.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_ACTIVITY_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private BankCode getBankCode(int bankCodeId) {
		if (bankCodeId == 0) return null;
		return bankCodeRepository.findById(bankCodeId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_BANK_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private Employee getEmployee(int employeeId) {
		return employeeRepository.findById(employeeId)
				.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private Entities getParentEntity(String parentId,Institution institution) {
		if (parentId.isEmpty()) return null;
		return entityRepo.findByEntityIdAndInstitution(parentId,institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_PARENT_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private Currency getCurrency(int currencyId) {
		if (currencyId == 0) return null;
		return currencyRepository.findById(currencyId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CUR_CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private void validateEntityCreation(EntityRequestDto entityRequestDto) {
		Optional<Entities> entity = entityRepo.findByEntityIdAndInstitution_InstitutionId(entityRequestDto.getEntityId(),entityRequestDto.getInstitutionId());

		if (entity.isPresent() && "1".equals(String.valueOf(entityRequestDto.getUpdateFlag()))) {
			throw new BusinessException(ResponseCode.CFG_ENTITY_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
		}

		if (entity.isPresent() || "1".equals(String.valueOf(entityRequestDto.getUpdateFlag()))) {
			throw new BusinessException(ResponseCode.CFG_ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}

		if ("0".equals(entityRequestDto.getEntityId())) {
			throw new BusinessException(ResponseCode.CFG_INVALID_ENTITY_ID, HttpStatus.NOT_FOUND);
		}
	}

	private void populateEntityFields(Entities entities, EntityRequestDto entityRequestDto,
									  Institution institution, MCCList mccList, ActivityPackage activityFeePKG,
									  NonActivityPackage nonActivityFeePKG, SystemCode businessType,
									  EntityLevels entityLevels, BankCode bankCode, Employee salesman,
									  Entities parentEntity, Employee employeeInCharge, Currency currency,
									  UserDetailsImpl userDetails) {

		entities.setDateCreate(new java.sql.Date(System.currentTimeMillis()));
		entities.setInstitution(institution);
		entities.setDefaultMCC(mccList.getMcc());
		
		entities.setActivityFeePKGEntity(activityFeePKG);
		if(Objects.nonNull(activityFeePKG)) {
			entities.setActivityFeePKG(activityFeePKG.getPackageId());
		}else {
			entities.setActivityFeePKG(null);
		}
		
		entities.setNonactivityFeePKGEntity(nonActivityFeePKG);
		if(Objects.nonNull(nonActivityFeePKG)) {
			entities.setNonactivityFeePKG(nonActivityFeePKG.getPackageId());
		}else {
			entities.setNonactivityFeePKG(null);
		}		entities.setBusinessType(businessType);
		
		entities.setEntityLevels(entityLevels.getHierarchyLevel());
		
		entities.setDefBankCodeEntity(bankCode);
		if(Objects.nonNull(bankCode)) {
		entities.setDefBankCode(bankCode.getBankCode());
		}else {
			entities.setDefBankCode(null);
		}
		entities.setSalesman(salesman);
		
		entities.setParentIdEntity(parentEntity);
		if(Objects.nonNull(parentEntity)) {
		entities.setParentId(parentEntity.getEntityId());
		}else {
			entities.setParentId(null);
		}
		entities.setEmployeeIncharge(employeeInCharge);
		entities.setAcctTemplateHdrId(entityRequestDto.getAcctTemplateHdrId()==0 ? null : entityRequestDto.getAcctTemplateHdrId());
		entities.setDefaultSettlementCurrency(currency);
		entities.setAddValueDateDays(entityRequestDto.getAddValueDateDays());
		entities.setOnHoldInd(entityRequestDto.getOnHoldInd().charAt(0));
		entities.setHotMerchantFlag(entityRequestDto.getHotMerchantFlag().charAt(0));
		entities.setEntityStatus(entityRequestDto.getEntityStatus().charAt(0));
		entities.setAssociatedPayment(entityRequestDto.getAssociatedPayment().charAt(0));
		entities.setStatementType(entityRequestDto.getStatementType().charAt(0));
		entities.setEStatementToEntity(entityRequestDto.getEStatementToEntity().charAt(0));
		entities.setStatus(entityRequestDto.getStatus());
		entities.setIsCloned((byte) 1);
		entities.setRecord_seq_id(entityRepo.findEntitySeqNextValue());

		if (userDetails != null) {
			entities.setUserCreate(String.valueOf(userDetails.getId()));
		}
	}


	public EntitiesResponseDto saveCloneEntity(String clonedEntityId, EntityRequestDto entityRequestDto) {
		Entities entity = createClonedEntity(entityRequestDto);

		List<PaymentAccount> paymentAccounts = paymentAccountRepository.findPaymentAccountsByEntityId(clonedEntityId
				);
		System.out.println("Entity Id>>>>"+entity.getEntityId());
		entityRepo.save(entity);
		for(PaymentAccount paymentAccount: paymentAccounts){
			PaymentAccount clonedPaymentAccount = new PaymentAccount(paymentAccount);
			clonedPaymentAccount.setEntityObject(entity);
			clonedPaymentAccount.setEntity(entity.getEntityId());
            paymentAccountRepository.save(clonedPaymentAccount);
		}

		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.saveSwitchEntity(entity);
		}
		return entityMapper.toDto(entity);
	}
}
