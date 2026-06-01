package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.mdsl.repository.OutputFileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.SystemCodeRequestDto;
import com.mdsl.model.dto.request.SystemCodeUniqueRequestDto;
import com.mdsl.model.dto.request.SystemOutputFileTemplateRequestDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.SystemCodeHeader;
import com.mdsl.model.mapper.SystemCodeMapper;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.SystemCodeHeaderRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class SystemCodeService {

	private final SystemCodeMapper systemCodeMapper;

	private final SystemCodeRepository systemCodeRepository;

	private final InstitutionRepository institutionRepository;
	
	private final SystemCodeHeaderRepository systemCodeHeaderRepository;

	private final OutputFileInfoRepository outputFileInfoRepository;

	public List<SystemCodeResponseDto> fetchAllSystemCodes() {
		List<SystemCode> systemCodes = systemCodeRepository.findAll(Sort.by(Sort.Direction.ASC, "systemCodeId"));
		List<SystemCodeResponseDto> dto = new ArrayList<SystemCodeResponseDto>();

		for (SystemCode tempSystemCode : systemCodes) {
			SystemCodeResponseDto tempDto = systemCodeMapper.toDto(tempSystemCode);
			dto.add(tempDto);
		}
		return dto;
	}
	
	public List<SystemCodeResponseDto> getSystemCodesByInstitution(String instId) {
		Institution institution1 = this.institutionRepository.findById(instId)
				.orElseThrow( () -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND) );
		Institution institution2 = this.institutionRepository.findById("SYSTEM")
				.orElseThrow( () -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND) );
		
		List<SystemCode> systemCodes = systemCodeRepository.findByInstitutionOrInstitution(institution1, institution2, Sort.by(Sort.Direction.ASC, "systemCodeId"));
		List<SystemCodeResponseDto> dto = new ArrayList<SystemCodeResponseDto>();

		for (SystemCode tempSystemCode : systemCodes) {
			SystemCodeResponseDto tempDto = systemCodeMapper.toDto(tempSystemCode);
			dto.add(tempDto);
		}
		return dto;
	}

	public SystemCodeResponseDto fetchSystemCodeById(int id) {
		SystemCode systemCode = systemCodeRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		SystemCodeResponseDto dto = systemCodeMapper.toDto(systemCode);
		return dto;
	}

	public SystemCodeResponseDto saveOrUpdateSystemCode(SystemCodeRequestDto systemCodeRequestDto) {
		SystemCodeHeader systemCodeHeader=null;
		SystemCode systemCode;
		SystemCode finalList;
		Institution institution = null;
		
		if(systemCodeRequestDto.getCodePrefix()!=null) {
			systemCodeRequestDto.setCodePrefix(systemCodeRequestDto.getCodePrefix().trim());
		}
		
		if(systemCodeRequestDto.getCodeSuffix()!=null) {
			systemCodeRequestDto.setCodeSuffix(systemCodeRequestDto.getCodeSuffix().trim());
		}
		
		if(systemCodeRequestDto.getInstitutionId()!=null) {
			systemCodeRequestDto.setInstitutionId(systemCodeRequestDto.getInstitutionId().trim());
		}
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (systemCodeRequestDto.getInstitutionId() != null) {
			institution = institutionRepository.findById(systemCodeRequestDto.getInstitutionId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}
		
		
		if (systemCodeRequestDto.getSystemCodeHeaderId() != 0) {
			systemCodeHeader = systemCodeHeaderRepository.findById(systemCodeRequestDto.getSystemCodeHeaderId())
					.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		}

		if (Objects.isNull(systemCodeRequestDto.getSystemCodeId()) || systemCodeRequestDto.getSystemCodeId() == 0) {
			systemCode = systemCodeMapper.toEntity(systemCodeRequestDto);
		//	systemCode.setRecordSequenceNumber(0);
		//	systemCode.setCodeSuffix(systemCodeRequestDto.getCodeSuffix());
			
			List<SystemCode> systemCodesByCodeSuffix= systemCodeRepository.findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(systemCodeRequestDto.getCodePrefix(),systemCodeRequestDto.getCodeSuffix(),institution.getInstitutionId());
			if(systemCodesByCodeSuffix.size()>0) {
				throw new BusinessException(ResponseCode.SYS_CODE_SUFFIX_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}
			systemCode.setCodePrefix(systemCodeHeader.getCodePrefix());
			systemCode.setSystemCodeHeader(systemCodeHeader);
			systemCode.setInstitution(institution);
			systemCode.setStatus("1".charAt(0));
			if(userDetails!=null) {
				systemCode.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
			}
			systemCode.setCreatedDate(new Date());
		} else {
			systemCode = systemCodeRepository.findById(systemCodeRequestDto.getSystemCodeId()).orElseThrow(
					() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
			
			List<SystemCode> systemCodesByCodeSuffix= systemCodeRepository.findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionIdAndSystemCodeIdNot(systemCodeRequestDto.getCodePrefix(),systemCodeRequestDto.getCodeSuffix(),institution.getInstitutionId(),systemCode.getSystemCodeId());
			if(systemCodesByCodeSuffix.size()>0) {
				throw new BusinessException(ResponseCode.SYS_CODE_SUFFIX_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}

			systemCode.setCodePrefix(systemCodeHeader.getCodePrefix());
			systemCode.setSystemCodeHeader(systemCodeHeader);
			systemCode.setCodeSuffix(systemCodeRequestDto.getCodeSuffix());
//			systemCode.setCodePattern(systemCodeRequestDto.getCodePattern());
			systemCode.setCodeValue(systemCodeRequestDto.getCodeValue());
			systemCode.setDescription(systemCodeRequestDto.getDescription());
//			systemCode.setStatus("1".charAt(0));
			systemCode.setInstitution(institution);
			//systemCode.setCreatedBy(Integer.valueOf(userDetails.getId()).toString());
		}

		finalList = systemCodeRepository.save(systemCode);
		SystemCodeResponseDto dto = systemCodeMapper.toDto(finalList);
		return dto;
	}

	public void deleteSystemCodeById(int id) {
		systemCodeRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		systemCodeRepository.deleteById(id);
	}

	public List<SystemCodeResponseDto> fetchSystemCodeByPrefixAndSuffix(
			@Valid SystemCodeRequestDto systemCodeRequestDto) {
		List<SystemCode> systemCodes = new ArrayList<>();
		if (systemCodeRequestDto.getInstitutionId() != null) {
			systemCodes = systemCodeRepository.findByCodePrefixIgnoreCaseAndInstitution_InstitutionId(
					systemCodeRequestDto.getCodePrefix(), systemCodeRequestDto.getInstitutionId(), StatusEnum.ENABLED.getValue(),
					Sort.by(Sort.Direction.ASC, "systemCodeId"));
		} else {
			systemCodes = systemCodeRepository.findByCodePrefixIgnoreCaseAndStatus(systemCodeRequestDto.getCodePrefix(), StatusEnum.ENABLED.getValue(),
					Sort.by(Sort.Direction.ASC, "systemCodeId"));
		}

		List<SystemCodeResponseDto> dto = new ArrayList<SystemCodeResponseDto>();

		for (SystemCode tempSystemCode : systemCodes) {
			SystemCodeResponseDto tempDto = systemCodeMapper.toDto(tempSystemCode);
			dto.add(tempDto);
		}
		return dto;
	}
	
	public List<SystemCodeResponseDto> fetchSystemCodeByPrefixAndInstitutionId(final String codePrefix, final String institutionId) {
        final List<SystemCodeResponseDto> allSystemCodeResponseDtos = new ArrayList<SystemCodeResponseDto>();
        final Institution institution = this.institutionRepository.findById(institutionId.trim().toUpperCase()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
        final List<SystemCode> allSystemCodes = (List<SystemCode>)this.systemCodeRepository.findByCodePrefixAndInstitutionAndStatus(codePrefix, institution, StatusEnum.ENABLED.getValue());
        allSystemCodes.stream().forEach(systemCode -> allSystemCodeResponseDtos.add(this.systemCodeMapper.toDto(systemCode)));
        return allSystemCodeResponseDtos;
    }
	
//	public List<SystemCodeResponseDto> getSystemCodesByPrefixAndValue(SystemOutputFileTemplateRequestDto systemOutputFileTemplateRequestDto) {
//		List<SystemCodeResponseDto> allSystemCodeResponseDtos = new ArrayList<SystemCodeResponseDto>();
//		String codeColumn = this.getColumnNumber(systemOutputFileTemplateRequestDto.getOutputFileType(), systemOutputFileTemplateRequestDto.getMerchantSubSummary(),
//				systemOutputFileTemplateRequestDto.getSummaryBy());
//		String codeColumn2 = this.getColumnNumber(systemOutputFileTemplateRequestDto.getOutputFileType(), systemOutputFileTemplateRequestDto.getInstSubSummary(),
//				systemOutputFileTemplateRequestDto.getSummaryBy());
//		List<SystemCode> allSystemCodes = this.systemCodeRepository.findByCodePrefixAndCodeValue(systemOutputFileTemplateRequestDto.getCodePrefix(),
//				codeColumn, codeColumn2, systemOutputFileTemplateRequestDto.getCodeResult());
//		allSystemCodes.stream().forEach(systemCode -> allSystemCodeResponseDtos.add(this.systemCodeMapper.toDto(systemCode)));
//        return allSystemCodeResponseDtos;
//	}

	public List<SystemCodeResponseDto> getSystemCodesByPrefixAndValueUpdated(SystemOutputFileTemplateRequestDto systemOutputFileTemplateRequestDto) {
		List<SystemCodeResponseDto> allSystemCodeResponseDtos = new ArrayList<>();
		String codeColumn1 = outputFileInfoRepository.findCodeColumn(systemOutputFileTemplateRequestDto.getOutputFileType(), systemOutputFileTemplateRequestDto.getMerchantSubSummary(),
				systemOutputFileTemplateRequestDto.getSummaryBy());
		String codeColumn2 = outputFileInfoRepository.findCodeColumn(systemOutputFileTemplateRequestDto.getOutputFileType(), systemOutputFileTemplateRequestDto.getInstSubSummary(),
				systemOutputFileTemplateRequestDto.getSummaryBy());
		List<SystemCode> allSystemCodes = this.systemCodeRepository.findByCodePrefixAndCodeValue(systemOutputFileTemplateRequestDto.getCodePrefix(),
				codeColumn1, codeColumn2, systemOutputFileTemplateRequestDto.getCodeResult());
		allSystemCodes.forEach(systemCode -> allSystemCodeResponseDtos.add(this.systemCodeMapper.toDto(systemCode)));
		return allSystemCodeResponseDtos;
	}

	public SystemCodeResponseDto getSystemCodesByUniqueFields(SystemCodeUniqueRequestDto systemCodeUniqueRequestDto) {
		SystemCodeResponseDto systemCodeResponseDto = new SystemCodeResponseDto();
		SystemCode systemCode = this.systemCodeRepository.findByCodePrefixAndCodeSuffixAndInstitution_InstitutionId(systemCodeUniqueRequestDto.getCodePrefix(),
				systemCodeUniqueRequestDto.getCodeSuffix(), systemCodeUniqueRequestDto.getInstitutionId())
		.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		systemCodeResponseDto = this.systemCodeMapper.toDto(systemCode);
        return systemCodeResponseDto;
	}

	public void changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		SystemCode systemCode = systemCodeRepository.findById(changeStatusRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		systemCode.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		systemCodeRepository.save(systemCode);
	}
	
	
	public List<SystemCodeResponseDto> getActiveSystemCode() {
		List<SystemCodeResponseDto> systemCodeResponseDtos = new ArrayList<SystemCodeResponseDto>();
		List<SystemCode> allSystemCodes = systemCodeRepository.findByStatus("1".charAt(0),
				Sort.by(Sort.Direction.ASC, "systemCodeId"));

		allSystemCodes.stream().forEach((systemCode) -> {
			SystemCodeResponseDto systemCodeResponseDto = systemCodeMapper.toDto(systemCode);

//			List<EntityLevels> entityLevels = entityLevelsRepository.findByHierarchyLevel(entities.getEntityLevels());
//
//			List<MCCList> mccList = mccListRepository.findByMcc(entities.getDefaultMCC());
//
//			if (!(entityLevels.isEmpty()) && !(mccList.isEmpty())) {
//				entityResponseDto.setMccName(mccList.get(0).getMcc());
//				entityResponseDto.setMccId(mccList.get(0).getMccId());
//				entityResponseDto.setEntityLevelId(entityLevels.get(0).getEntityLevelId());
//				entityResponseDto.setHierarchyLevel(entityLevels.get(0).getHierarchyLevel());
//				entityResponseDto.setTypeDescription(entityLevels.get(0).getTypeDescription());
//			}

			systemCodeResponseDtos.add(systemCodeResponseDto);
		});

		return systemCodeResponseDtos;
	}

	public String getColumnNumber(String outputFileType, String merchantOrInstSubSummary, String summaryBy) {
		String columnNumber = "1";
		
		if(outputFileType.equals("ACCOUNTING")) {
			if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("N")) {
				columnNumber = "1";
			} else if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("AS")) {
				columnNumber = "2";
			} else if(merchantOrInstSubSummary.equals("I") && summaryBy.equals("AS")) {
				columnNumber = "3";
			} else if(merchantOrInstSubSummary.equals("S") && summaryBy.equals("AS")) {
				columnNumber = "4";
			} else if(merchantOrInstSubSummary.equals("D") && summaryBy.equals("AS")) {
				columnNumber = "5";
			} else if(merchantOrInstSubSummary.equals("TB") && summaryBy.equals("AS")) {
				columnNumber = "12";
			} else if(merchantOrInstSubSummary.equals("T") && summaryBy.equals("AS")) {
				columnNumber = "13";
			} else if(merchantOrInstSubSummary.equals("IT") && summaryBy.equals("AS")) {
				columnNumber = "14";
			} else if(merchantOrInstSubSummary.equals("IB") && summaryBy.equals("AS")) {
				columnNumber = "15";
			} else if(merchantOrInstSubSummary.equals("DT") && summaryBy.equals("AS")) {
				columnNumber = "16";
			} else if(merchantOrInstSubSummary.equals("DB") && summaryBy.equals("AS")) {
				columnNumber = "17";
			} else if(merchantOrInstSubSummary.equals("ST") && summaryBy.equals("AS")) {
				columnNumber = "18";
			} else if(merchantOrInstSubSummary.equals("SB") && summaryBy.equals("AS")) {
				columnNumber = "19";
			} else if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("A")) {
				columnNumber = "20";
			} else if(merchantOrInstSubSummary.equals("I") && summaryBy.equals("A")) {
				columnNumber = "21";
			} else if(merchantOrInstSubSummary.equals("S") && summaryBy.equals("A")) {
				columnNumber = "22";
			} else if(merchantOrInstSubSummary.equals("D") && summaryBy.equals("A")) {
				columnNumber = "23";
			} else if(merchantOrInstSubSummary.equals("TB") && summaryBy.equals("A")) {
				columnNumber = "24";
			} else if(merchantOrInstSubSummary.equals("T") && summaryBy.equals("A")) {
				columnNumber = "25";
			} else if(merchantOrInstSubSummary.equals("IT") && summaryBy.equals("A")) {
				columnNumber = "26";
			} else if(merchantOrInstSubSummary.equals("IB") && summaryBy.equals("A")) {
				columnNumber = "27";
			} else if(merchantOrInstSubSummary.equals("DT") && summaryBy.equals("A")) {
				columnNumber = "28";
			} else if(merchantOrInstSubSummary.equals("DB") && summaryBy.equals("A")) {
				columnNumber = "29";
			} else if(merchantOrInstSubSummary.equals("ST") && summaryBy.equals("A")) {
				columnNumber = "30";
			} else if(merchantOrInstSubSummary.equals("SB") && summaryBy.equals("A")) {
				columnNumber = "31";
			}
		} else if(outputFileType.equals("TRANSACTIONS")) {
			if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("N")) {
				columnNumber = "6";
			} else if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("O")) {
				columnNumber = "7";
			} else if(merchantOrInstSubSummary.equals("I") && summaryBy.equals("O")) {
				columnNumber = "8";
			} else if(merchantOrInstSubSummary.equals("S") && summaryBy.equals("O")) {
				columnNumber = "9";
			} else if(merchantOrInstSubSummary.equals("D") && summaryBy.equals("O")) {
				columnNumber = "10";
			}
		} else if(outputFileType.equals("MANUAL")) {
			if(merchantOrInstSubSummary.equals("N") && summaryBy.equals("N")) {
				columnNumber = "11";
			}
		}
		
		return columnNumber;
	}

	public List<SystemCodeResponseDto> getHoldTypes() {
		List<SystemCode> systemCodes = systemCodeRepository.findByCodePrefixIgnoreCaseAndInstitution_InstitutionId("HOLD_TYPE",
				"SYSTEM", StatusEnum.ENABLED.getValue(),Sort.by(Sort.Direction.ASC, "systemCodeId"));
		List<SystemCodeResponseDto> dto = new ArrayList<>();

		for (SystemCode tempSystemCode : systemCodes) {
			SystemCodeResponseDto tempDto = systemCodeMapper.toDto(tempSystemCode);
			dto.add(tempDto);
		}
		return dto;
	}
}
