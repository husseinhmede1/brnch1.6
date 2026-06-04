package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteNonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.NonActivityPackage;
import com.mdsl.model.entity.NonActivityPackageDetails;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.TerminalTypes;
import com.mdsl.model.mapper.NonActivityPackageDetailsMapper;
import com.mdsl.repository.CurrencyRepository;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.NonActivityPackageDetailsRepository;
import com.mdsl.repository.NonActivityPackageRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalTypesRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.ChargeTypemasterEnum;
import com.mdsl.utils.enumerations.CodePrefixEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NonActivityPackageDetailsService {
	private final NonActivityPackageDetailsRepository nonActivityPackageDetailsRepository;

	private final InstitutionRepository institutionRepository;

	private final NonActivityPackageRepository nonActivityPackageRepository;

	private final CurrencyRepository currencyRepository;

	private final TerminalTypesRepository terminalTypesRepository;

	private final NonActivityPackageDetailsMapper nonActivityPackageDetailsMapper;

	private final EntitiesRepository entitiesRepository;
	
	private final SystemCodeRepository systemCodeRepository;
    private final MakerCheckerEngine makerCheckerEngine;

	public List<NonActivityPackageDetailsResponseDto> getAllNonActivityPackageDetails() {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> allNonActivityPackageDetailsCodes = nonActivityPackageDetailsRepository
				.findAll(Sort.by(Sort.Direction.ASC, "packageDetailsId"));

		allNonActivityPackageDetailsCodes.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), String.valueOf(nonActivityPackageDetailsCode.getChargeMaster()),
					"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public List<NonActivityPackageDetailsResponseDto> getAllNonActivityPackageDetailsByPackageId(String id, String instId) {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> allNonActivityPackageDetailsCodes = nonActivityPackageDetailsRepository
				.findByPackageIdAndInstitution_institutionId(id, instId, Sort.by(Sort.Direction.ASC, "recordSeqId"));

		allNonActivityPackageDetailsCodes.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			if (nonActivityPackageDetailsCode.getChargeMaster() != null && !nonActivityPackageDetailsCode.getChargeMaster().isEmpty()) {
				SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), nonActivityPackageDetailsCode.getChargeMaster(),
								"SYSTEM")
						.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
				this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			}
			if (nonActivityPackageDetailsCode.getFrequency() != null && !nonActivityPackageDetailsCode.getFrequency().isEmpty()) {
				SystemCode frequency = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.FREQUENCY_MASTER.getValue(), nonActivityPackageDetailsCode.getFrequency(),
								"SYSTEM")
						.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
				this.fillFrequencyCodeInfo(frequency, nonActivityPackageDetailsCodeResponseDto);
			}
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public List<NonActivityPackageDetailsResponseDto> getActiveNonActivityPackageDetails() {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> allNonActivityPackageDetailsCodes = nonActivityPackageDetailsRepository
				.findByStatus('1', Sort.by(Sort.Direction.ASC, "packageDetailsId"));

		allNonActivityPackageDetailsCodes.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), String.valueOf(nonActivityPackageDetailsCode.getChargeMaster()),
					"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public List<NonActivityPackageDetailsResponseDto> getNonActivityPackageDetailsByInstitutionId(String instId) {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> allNonActivityPackageDetailsCodes = nonActivityPackageDetailsRepository
				.findNonActivityPackageDetailsByInstitutionId(instId, Sort.by(Sort.Direction.ASC, "packageDetailsId"));

		allNonActivityPackageDetailsCodes.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), String.valueOf(nonActivityPackageDetailsCode.getChargeMaster()),
					"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public List<NonActivityPackageDetailsResponseDto> getAllNonActivityPackageDetailsByPackageIdAndInstitutionId(
			String instId, String pkgId) {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> nonActivityPackageDetails = nonActivityPackageDetailsRepository
				.findNonActivityPackageDetailsByInstitutionIdAndPackageId(instId, pkgId,
						Sort.by(Sort.Direction.ASC, "recordSeqId"));

		nonActivityPackageDetails.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			if (nonActivityPackageDetailsCode.getChargeMaster() != null && !nonActivityPackageDetailsCode.getChargeMaster().isEmpty()) {
				SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), String.valueOf(nonActivityPackageDetailsCode.getChargeMaster()),
								"SYSTEM")
						.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
				this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			}
			if (nonActivityPackageDetailsCode.getFrequency() != null && !nonActivityPackageDetailsCode.getFrequency().isEmpty()) {
				SystemCode frequency = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.FREQUENCY_MASTER.getValue(), nonActivityPackageDetailsCode.getFrequency(),
								"SYSTEM")
						.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
				this.fillFrequencyCodeInfo(frequency, nonActivityPackageDetailsCodeResponseDto);
			}
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public List<NonActivityPackageDetailsResponseDto> getAllActiveNonActivityPackageDetailsByPackageIdAndInstitutionId(
			String instId, String pkgId) {
		List<NonActivityPackageDetailsResponseDto> allNonActivityPackageDetailsCodesDto = new ArrayList<>();
		List<NonActivityPackageDetails> nonActivityPackageDetails = nonActivityPackageDetailsRepository
				.findActiveNonActivityPackageDetailsByInstitutionIdAndPackageId(instId, pkgId, '1',
						Sort.by(Sort.Direction.ASC, "packageDetailsId"));

		nonActivityPackageDetails.forEach((nonActivityPackageDetailsCode) -> {
			NonActivityPackageDetailsResponseDto nonActivityPackageDetailsCodeResponseDto = nonActivityPackageDetailsMapper
					.toDto(nonActivityPackageDetailsCode);
			SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), String.valueOf(nonActivityPackageDetailsCode.getChargeMaster()),
					"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsCodeResponseDto);
			allNonActivityPackageDetailsCodesDto.add(nonActivityPackageDetailsCodeResponseDto);
		});

		return allNonActivityPackageDetailsCodesDto;
	}

	public NonActivityPackageDetailsResponseDto getNonActivityPackageDetailsById(int id) {
		Optional<NonActivityPackageDetails> nonActivityPackageDetails = nonActivityPackageDetailsRepository
				.findById(id);
		NonActivityPackageDetails nonActivityPkg = nonActivityPackageDetails
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
		NonActivityPackageDetailsResponseDto nonActivityPackageDetailsResponseDto = nonActivityPackageDetailsMapper.toDto(nonActivityPkg);
		if(nonActivityPkg.getChargeMaster()!=null){
			SystemCode chargeTypeMaster = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.CHARGE_TYPE.getValue(), nonActivityPkg.getChargeMaster(),
					"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillChargeTypeInfo(chargeTypeMaster, nonActivityPackageDetailsResponseDto);

		}
		if(nonActivityPkg.getFrequency()!=null) {
			SystemCode frequency = systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId(CodePrefixEnum.FREQUENCY_MASTER.getValue(), nonActivityPkg.getFrequency(),
							"SYSTEM")
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
			this.fillFrequencyCodeInfo(frequency, nonActivityPackageDetailsResponseDto);
		}
		nonActivityPackageDetailsResponseDto.setPackageDetailsId(nonActivityPackageDetailsResponseDto.getRecordSeqId());
		return nonActivityPackageDetailsResponseDto;
	}

	public NonActivityPackageDetailsResponseDto saveOrUpdateNonActivityPackageDetails(
			NonActivityPackageDetailsRequestDto nonActivityPackageDetailsRequestDto) {

		nonActivityPackageDetailsRequestDto.setPackageId(nonActivityPackageDetailsRequestDto.getPackageId().trim().toUpperCase());

		SystemCode frequencyMaster =null;
		NonActivityPackageDetails nonActivityPackageDetails;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		SystemCode chargeTypeMaster = systemCodeRepository.findById(nonActivityPackageDetailsRequestDto.getChargeMasterId())
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));

		validateChargeType(nonActivityPackageDetailsRequestDto, chargeTypeMaster);

		Institution institution = institutionRepository.findById(nonActivityPackageDetailsRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST_CODE, HttpStatus.NOT_FOUND));

		NonActivityPackage nonActivityPackage = nonActivityPackageRepository
				.findByPackageIdAndInstitution_institutionId(nonActivityPackageDetailsRequestDto.getPackageId(), institution.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

		Currency currency = currencyRepository.findById(nonActivityPackageDetailsRequestDto.getCurrencyId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));
		if(!nonActivityPackageDetailsRequestDto.getFrequencyId().equals(0)) {
			frequencyMaster = systemCodeRepository.findById(nonActivityPackageDetailsRequestDto.getFrequencyId())
					.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		}

		TerminalTypes terminalTypes = terminalTypesRepository
				.findById(nonActivityPackageDetailsRequestDto.getTerminalTypeId()).orElseThrow(
						() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_TYPE_ID, HttpStatus.NOT_FOUND));


		if (nonActivityPackageDetailsRequestDto.getPackageDetailsId() == 0) {

			nonActivityPackageDetails = nonActivityPackageDetailsMapper.toEntity(nonActivityPackageDetailsRequestDto);
			Integer nextVal = nonActivityPackageDetailsRepository.findNonActivityPackageDetailsSeqNextValue();
//			nonActivityPackageDetails.setRecordSeqId(0);
			nonActivityPackageDetails.setRecordSeqId(nextVal);
			nonActivityPackageDetails.setUserCreate(userDetails.getUsername());
			nonActivityPackageDetails.setDateCreate(new Date());
			nonActivityPackageDetails.setStatus(Objects.nonNull(nonActivityPackageDetailsRequestDto.getStatus())?String.valueOf(nonActivityPackageDetailsRequestDto.getStatus()).equalsIgnoreCase("") ? '0' : nonActivityPackageDetailsRequestDto.getStatus():'0');

			nonActivityPackageDetails.setInstitution(institution);
			nonActivityPackageDetails.setNonActivityPackageEntity(nonActivityPackage);
			nonActivityPackageDetails.setNonActivityPackage(nonActivityPackage.getPackageId());

			nonActivityPackageDetails.setCurrency(currency);
			nonActivityPackageDetails.setFrequency(frequencyMaster!=null?frequencyMaster.getCodeValue():null);
			nonActivityPackageDetails.setChargeMaster(chargeTypeMaster.getCodeValue());
//			nonActivityPackageDetails.setCardScheme();
			nonActivityPackageDetails.setTerminalTypes(terminalTypes);
			nonActivityPackageDetails.setAssignedTransaction(null);
            nonActivityPackageDetails.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
		}

		else {
			nonActivityPackageDetails = nonActivityPackageDetailsRepository
					.findById(nonActivityPackageDetailsRequestDto.getPackageDetailsId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

			nonActivityPackageDetails.setAmount(nonActivityPackageDetailsRequestDto.getAmount());
			nonActivityPackageDetails.setChargeCount(nonActivityPackageDetailsRequestDto.getChargeCount());
			nonActivityPackageDetails.setChargeFirstTransaction(nonActivityPackageDetailsRequestDto.getChargeFirstTransaction());
			nonActivityPackageDetails.setEndDate(nonActivityPackageDetailsRequestDto.getEndDate());
			nonActivityPackageDetails.setStartDate(nonActivityPackageDetailsRequestDto.getStartDate());
			nonActivityPackageDetails.setMaxAmount(nonActivityPackageDetailsRequestDto.getMaxAmount());
			nonActivityPackageDetails.setNumberOfInstallments(nonActivityPackageDetailsRequestDto.getNumberOfInstallments());
			nonActivityPackageDetails.setStatus(nonActivityPackageDetailsRequestDto.getStatus());
			nonActivityPackageDetails.setInstitution(institution);
			nonActivityPackageDetails.setNonActivityPackageEntity(nonActivityPackage);
			nonActivityPackageDetails.setNonActivityPackage(nonActivityPackage.getPackageId());
			nonActivityPackageDetails.setCurrency(currency);
			nonActivityPackageDetails.setFrequency(frequencyMaster!=null?frequencyMaster.getCodeValue():null);
			nonActivityPackageDetails.setChargeMaster(chargeTypeMaster.getCodeValue());
			nonActivityPackageDetails.setTerminalTypes(terminalTypes);
			nonActivityPackageDetails.setAssignedTransaction(null);
		}

		if (makerCheckerEngine.processIfRequired(nonActivityPackageDetailsRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		nonActivityPackageDetails = nonActivityPackageDetailsRepository.save(nonActivityPackageDetails);

		return nonActivityPackageDetailsMapper.toDto(nonActivityPackageDetails);
	}

	private static void validateChargeType(NonActivityPackageDetailsRequestDto nonActivityPackageDetailsRequestDto, SystemCode chargeTypeMaster) {

		if(chargeTypeMaster.getCodeValue().equals(ChargeTypemasterEnum.ONE_TIME.getCode())){
			if ( !nonActivityPackageDetailsRequestDto.getFrequencyId().equals(0) ){
				throw new BusinessException(ResponseCode.INVALID_FREQUENCY_ID, HttpStatus.NOT_FOUND);
			}
			else if(nonActivityPackageDetailsRequestDto.getNumberOfInstallments() != 0){
				throw new BusinessException(ResponseCode.INVALID_INSTALLMENTS, HttpStatus.NOT_FOUND);
			}
		}
	}

	public String deleteNonActivityPackageDetails(DeleteNonActivityPackageDetailsRequestDto deleteNonActivityPackageDetailsRequestDto) {

		NonActivityPackageDetails nonActivityPackageDetails = nonActivityPackageDetailsRepository.findById(deleteNonActivityPackageDetailsRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

		List<Entities> entities = entitiesRepository
				.findByNonactivityFeePKGEntity_PackageIdAndInstitution_institutionId(nonActivityPackageDetails.getNonActivityPackageEntity().getPackageId(),deleteNonActivityPackageDetailsRequestDto.getInstId(),Sort.by(Sort.Direction.ASC, "entityId"));
		if (entities.isEmpty()) {
	   		if (makerCheckerEngine.processIfRequired(deleteNonActivityPackageDetailsRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
				return null;
			}
			nonActivityPackageDetailsRepository.deleteById(deleteNonActivityPackageDetailsRequestDto.getId());
			return "Non Activity Fee Package Detail Deleted Successfully";
		} else {
			throw new BusinessException(ResponseCode.REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
		}

	}

	public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		NonActivityPackageDetails nonActivityPackageDetails = nonActivityPackageDetailsRepository
				.findById(changeStatusRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));
		nonActivityPackageDetails.setStatus(changeStatusRequestDto.getStatus().charAt(0));
   		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		nonActivityPackageDetailsRepository.save(nonActivityPackageDetails);
	}

	public static boolean isValidIndex(String[] arr, int index) {
		return index >= 0 && index < arr.length;
	}

	 
	 public void fillChargeTypeInfo(SystemCode chargeType, NonActivityPackageDetailsResponseDto nonActivityPackageDetailsResponseDto) {
		 nonActivityPackageDetailsResponseDto.setChargeTypeCodeDescription(chargeType.getDescription());
		 nonActivityPackageDetailsResponseDto.setChargeTypeCodePrefix(chargeType.getCodePrefix());
		 nonActivityPackageDetailsResponseDto.setChargeTypeCodeSuffix(chargeType.getCodeSuffix());
		 nonActivityPackageDetailsResponseDto.setChargeTypeCodeValue(chargeType.getCodeValue());
		 nonActivityPackageDetailsResponseDto.setChargeTypeSystemCodeId(chargeType.getSystemCodeId());
	 }

	private void fillFrequencyCodeInfo(SystemCode frequency, NonActivityPackageDetailsResponseDto nonActivityPackageDetailsResponseDto) {
		nonActivityPackageDetailsResponseDto.setFrequencyCodeSuffix(frequency.getCodeSuffix());
		nonActivityPackageDetailsResponseDto.setFrequencyCodePrefix(frequency.getCodePrefix());
		nonActivityPackageDetailsResponseDto.setFrequencyCodeValue(frequency.getCodeValue());
		nonActivityPackageDetailsResponseDto.setFrequencySystemCodeId(frequency.getSystemCodeId());
		nonActivityPackageDetailsResponseDto.setFrequencyCodeDescription(frequency.getDescription());
	}
}
