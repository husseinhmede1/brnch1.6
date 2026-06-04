package com.mdsl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.response.TerminalTypeResponseDto;
import com.mdsl.model.dto.response.TerminalTypesDto;
import com.mdsl.model.entity.NonActivityPackageDetails;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TerminalTypes;
import com.mdsl.model.mapper.TerminalTypeMapper;
import com.mdsl.repository.NonActivityPackageDetailsRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.TerminalRepository;
import com.mdsl.repository.TerminalTypesRepository;
import com.mdsl.swtch.service.SwitchPosTermTypeService;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;

@Service
public class TerminalTypesService {

	@Autowired
	private TerminalTypesRepository terminalTypesRepository;
	
	@Autowired
	private NonActivityPackageDetailsRepository nonActivityPackageDetailsRepository;
	
	@Autowired
	private TerminalRepository terminalRepository;

	@Autowired
	private TerminalTypeMapper terminalTypeMapper;
	
	@Autowired
	private SystemCodeRepository systemCodeRepository;
	
	@Autowired
	private SwitchPosTermTypeService switchPosTermTypeService;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public TerminalTypeResponseDto saveOrUpdateTerminmalType(TerminalTypesDto terminalTypesDto) {
		TerminalTypes terminalTypes = new TerminalTypes();
		
		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		if(terminalTypesDto.getTerminalType()!=null) {
			terminalTypesDto.setTerminalType(terminalTypesDto.getTerminalType().trim());
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (terminalTypesDto.getTerminalTypesId() != 0) {
			terminalTypes = terminalTypesRepository.findById(terminalTypesDto.getTerminalTypesId()).get();
		}

		if (makerCheckerEngine.processIfRequired(terminalTypesDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}

		if (terminalTypes != null && terminalTypesDto.getTerminalTypesId() != 0) {
			
			List<TerminalTypes> listOfTerminalTypes=terminalTypesRepository.findByTerminalTypeEqualsIgnoreCaseAndTerminalTypesIdNot(terminalTypesDto.getTerminalType(),terminalTypesDto.getTerminalTypesId());
			if(listOfTerminalTypes.size()>0) {
				throw new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}else {
				
				List<NonActivityPackageDetails> nonActivityPackageDetails= nonActivityPackageDetailsRepository.findByTerminalTypes_TerminalType(terminalTypes.getTerminalType());
				if(nonActivityPackageDetails.size()>0) {
					throw new BusinessException(ResponseCode.CFG_REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
				}
				
				List<Terminal> terminals=terminalRepository.findByTerminalTypes_TerminalType(terminalTypes.getTerminalType()); 
				if(terminals.size()>0) {
					throw new BusinessException(ResponseCode.CFG_REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
				}
				terminalTypes = terminalTypeMapper.toEntity(terminalTypesDto);
				
//				List<NonActivityPackageDetails> nonActivityPackageDetails=nonActivityPackageDetailsRepository.findByTerminalTypes_TerminalType(terminalTypesDto.getTerminalType());
//				for(NonActivityPackageDetails nonActivityPackageDetail:nonActivityPackageDetails) {
//					nonActivityPackageDetail.setTerminalTypes(terminalTypes);
//					nonActivityPackageDetailsRepository.save(nonActivityPackageDetail);
//				}
				
//				nonActivityPackageDetails.forEach((nonActivityPackageDetail) -> {
//					nonActivityPackageDetail.setTerminalTypes(terminalTypes);
//				
//				});
				
				terminalTypes.setStatus(terminalTypesDto.getStatus());
				terminalTypes.setDateCreate(new Date());
				terminalTypes.setUserCreate("user1");
			//	terminalTypes.setUserCreate(Integer.valueOf(userDetails.getId()).toString());

				terminalTypes = terminalTypesRepository.save(terminalTypes);
				
				//Save switch terminal type
				if(switchSystemCode.getCodeSuffix().equals("1")) {
					this.switchPosTermTypeService.savePosTermType(terminalTypes.getTerminalType(), terminalTypes.getMakeName(), terminalTypes.getMakeModel());
				}
				
				TerminalTypeResponseDto terminalTypeResponseDto = terminalTypeMapper.toResponseDto(terminalTypes);
				return terminalTypeResponseDto;
			}
			
			
		} else {
			
			Optional<TerminalTypes> optTerminalType=terminalTypesRepository.findByTerminalTypeEqualsIgnoreCase(terminalTypesDto.getTerminalType());
			if(optTerminalType.isPresent()) {
				throw new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_ALREADY_EXISTS, HttpStatus.NOT_FOUND);
			}else {
				TerminalTypes terminalTypes1 = new TerminalTypes();
				terminalTypes1 = terminalTypeMapper.toEntity(terminalTypesDto);
				terminalTypes.setStatus(terminalTypesDto.getStatus());
				terminalTypes1.setUserCreate("user1");
				terminalTypes1.setDateCreate(new Date());
				if(userDetails!=null) {
					terminalTypes.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
				}
				terminalTypes1 = terminalTypesRepository.save(terminalTypes1);
				terminalTypesDto.setTerminalTypesId(
						terminalTypes1.getTerminalTypesId() != null ? terminalTypes1.getTerminalTypesId() : 0);
				TerminalTypeResponseDto terminalTypeResponseDto = terminalTypeMapper.toResponseDto(terminalTypes1);
				
				//Save switch terminal type
				if(switchSystemCode.getCodeSuffix().equals("1")) {
					this.switchPosTermTypeService.savePosTermType(terminalTypes1.getTerminalType(), terminalTypes1.getMakeName(), terminalTypes1.getMakeModel());
				}
				
				return terminalTypeResponseDto;
			}
			
		}
	}

	public List viewTerminalTypes() {
		// TODO Auto-generated method stub

		List<TerminalTypes> listTerminalTypes = terminalTypesRepository.findAll(Sort.by(Sort.Direction.ASC, "terminalTypesId"));

		List<TerminalTypeResponseDto> lisTerminalTypes1 = new ArrayList();

		listTerminalTypes.stream().forEach((terminalTypes) -> {
			lisTerminalTypes1.add(terminalTypeMapper.toResponseDto(terminalTypes));
		});

		return lisTerminalTypes1;
	}

	public void deleteTerminalType(int terminalTypeId) throws Exception {
		TerminalTypes terminalTypes = terminalTypesRepository.findById(terminalTypeId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(terminalTypeId, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		terminalTypesRepository.delete(terminalTypes);
		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.SYS_CODE_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.switchPosTermTypeService.deletePosTermType(terminalTypes.getTerminalType());
		}	
	}

	public TerminalTypeResponseDto getTerminalTypes(int terminalTypeId) {
		TerminalTypes terminalTypes = terminalTypesRepository.findById(terminalTypeId).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

		return terminalTypeMapper.toResponseDto(terminalTypes);
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		TerminalTypes terminalTypes = terminalTypesRepository.findById(changeStatusRequestDto.getId()).orElseThrow(
				() -> new BusinessException(ResponseCode.CFG_TERMINAL_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
		terminalTypes.setStatus(changeStatusRequestDto.getStatus());
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		terminalTypesRepository.save(terminalTypes);

		return "Status changed successfully";
	}

	public List getActiveTerminalType() {

		List<TerminalTypes> listTerminalTypes = terminalTypesRepository.findByStatus("1",
				Sort.by(Sort.Direction.ASC, "terminalTypesId"));

		List<TerminalTypeResponseDto> lisTerminalTypes1 = new ArrayList();

		listTerminalTypes.stream().forEach((terminalTypes) -> {
			lisTerminalTypes1.add(terminalTypeMapper.toResponseDto(terminalTypes));
		});

		return lisTerminalTypes1;
	}

}
