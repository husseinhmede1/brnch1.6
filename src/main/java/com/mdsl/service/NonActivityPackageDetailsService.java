package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.NonActivityPackageDetailsRequestDto;
import com.mdsl.model.dto.response.NonActivityPackageDetailsResponseDto;
import com.mdsl.model.entity.*;
import com.mdsl.model.entity.Currency;
import com.mdsl.model.mapper.NonActivityPackageDetailsMapper;
import com.mdsl.repository.*;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.ChargeTypemasterEnum;
import com.mdsl.utils.enumerations.CodePrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

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


		NonActivityPackageDetails pkg;
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
            nonActivityPackageDetails = nonActivityPackageDetailsRepository.save(nonActivityPackageDetails);
		}

		else {
			pkg = nonActivityPackageDetailsRepository
					.findById(nonActivityPackageDetailsRequestDto.getPackageDetailsId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

			pkg.setAmount(nonActivityPackageDetailsRequestDto.getAmount());
			pkg.setChargeCount(nonActivityPackageDetailsRequestDto.getChargeCount());
			pkg.setChargeFirstTransaction(nonActivityPackageDetailsRequestDto.getChargeFirstTransaction());
			pkg.setEndDate(nonActivityPackageDetailsRequestDto.getEndDate());
			pkg.setStartDate(nonActivityPackageDetailsRequestDto.getStartDate());
			pkg.setMaxAmount(nonActivityPackageDetailsRequestDto.getMaxAmount());
			pkg.setNumberOfInstallments(nonActivityPackageDetailsRequestDto.getNumberOfInstallments());
//			pkg.setPackageLine(nonActivityPackageDetailsRequestDto.getPackageLine());
			pkg.setStatus(nonActivityPackageDetailsRequestDto.getStatus());
			pkg.setInstitution(institution);
			pkg.setNonActivityPackageEntity(nonActivityPackage);
			pkg.setNonActivityPackage(nonActivityPackage.getPackageId());
			pkg.setCurrency(currency);
			pkg.setFrequency(frequencyMaster!=null?frequencyMaster.getCodeValue():null);
			pkg.setChargeMaster(chargeTypeMaster.getCodeValue());
//			pkg.setCardScheme(cardScheme);
			pkg.setTerminalTypes(terminalTypes);
			pkg.setAssignedTransaction(null);
		//	pkg.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
			nonActivityPackageDetails = nonActivityPackageDetailsRepository.save(pkg);
		}

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

	public String deleteNonActivityPackageDetails(int id, String instId) {

		NonActivityPackageDetails nonActivityPackageDetails = nonActivityPackageDetailsRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ACTIVITY, HttpStatus.NOT_FOUND));

		List<Entities> entities = entitiesRepository
				.findByNonactivityFeePKGEntity_PackageIdAndInstitution_institutionId(nonActivityPackageDetails.getNonActivityPackageEntity().getPackageId(),instId,Sort.by(Sort.Direction.ASC, "entityId"));
		if (entities.isEmpty()) {
			nonActivityPackageDetailsRepository.deleteById(id);
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
