package com.mdsl.service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.AllTerminalsRequestDto;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteTerminalRequestDto;
import com.mdsl.model.dto.request.TerminalRequestDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.TerminalResponseDto;
import com.mdsl.model.entity.*;
import com.mdsl.model.mapper.TerminalMapper;
import com.mdsl.repository.*;
import com.mdsl.swtch.model.dto.request.SwitchTerminalRequestDto;
import com.mdsl.swtch.service.SwitchTerminalService;
import com.mdsl.utils.DateParseUtil;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.PaginationCommonCode;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.DateFormatEnum;
import com.mdsl.utils.enumerations.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TerminalService {

	@Autowired
	private TerminalRepository terminalRepository;

	@Autowired
	private TerminalMapper terminalMapper;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private MCCListRepository mccListRepository;

	@Autowired
	private TerminalTypesRepository terminalTypesRepository;
	
	@Autowired
	private SystemCodeRepository systemCodeRepository;
	
	@Autowired
	private SwitchTerminalService switchTerminalService;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	@Transactional
	public TerminalResponseDto saveOrUpdateTerminal(TerminalRequestDto terminalRequestDto) {
		// TODO Auto-generated method stub
		Terminal terminal ;
		
		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		
		if(terminalRequestDto.getInstitutionId()!=null) {
			terminalRequestDto.setInstitutionId(terminalRequestDto.getInstitutionId().trim());
		}
		
		if(terminalRequestDto.getEntityId()!=null) {
			terminalRequestDto.setEntityId(terminalRequestDto.getEntityId().trim());
		}
		
		TerminalResponseDto terminalResponseDto ;
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		Institution institution = institutionRepository.findById(terminalRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND));

		Entities entities = entitiesRepository.findByEntityIdAndInstitution(terminalRequestDto.getEntityId(),institution)
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_ENTITY_ID, HttpStatus.NOT_FOUND));

		Currency currency = currencyRepository.findById(Integer.parseInt(terminalRequestDto.getCurrencyId()))
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_CURRENCY, HttpStatus.NOT_FOUND));

		MCCList mccList = mccListRepository.findById(Integer.parseInt(terminalRequestDto.getMccId()))
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_MCC, HttpStatus.NOT_FOUND));

		TerminalTypes terminalTypes = terminalTypesRepository.findById(terminalRequestDto.getTerminalTypeId())
				.orElseThrow(
						() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_TYPE_ID, HttpStatus.NOT_FOUND));



		if (makerCheckerEngine.processIfRequired(terminalRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}

		if (institution != null && entities != null && currency != null && mccList != null) {

			
			if (String.valueOf(terminalRequestDto.getUpdateFlag()).equals("1")) {
				terminal = terminalRepository.findByTerminalIdAndInstitutionEntity_InstitutionId(terminalRequestDto.getTerminalId(), institution.getInstitutionId()).orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.NOT_FOUND));
				terminal.setInstitutionEntity(institution);
				terminal.setInstitution(institution.getInstitutionId());

				terminal.setCurrency(currency);
				terminal.setMccList(mccList.getMcc());
				terminal.setTerminalTypes(terminalTypes);
				
				terminal.setEntitiesObject(entities);
				terminal.setEntities(entities.getEntityId());

				terminal.setStatus(terminalRequestDto.getStatus().charAt(0));
				terminal.setSerialNumber(terminalRequestDto.getSerialNumber());
				terminal.setECommerceFlag(terminalRequestDto.getECommerceFlag());
				terminal.setActualStartDate(DateParseUtil.parseDate(terminalRequestDto.getActualStartDate(),DateFormatEnum.DD_MM_YYYY.getFormatter()));
				terminal.setTerminationDate(DateParseUtil.parseDate(terminalRequestDto.getTerminationDate(),DateFormatEnum.DD_MM_YYYY.getFormatter()));
		
				terminal.setRecordSequenceId(terminal.getRecordSequenceId());
				terminal.setDateCreate(new Date());
				terminal = terminalRepository.save(terminal);
				if(switchSystemCode.getCodeSuffix().equals("1")) {
					this.saveSwitchTerminal(terminal);
				}

				terminalResponseDto = terminalMapper.toResponseDto(terminal);
				terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
				terminalResponseDto.setMcc(mccList.getMcc());
				terminalResponseDto.setMccId(mccList.getMccId());
				
				return terminalResponseDto;
			} else {
				if (terminalRequestDto.getTerminalId().equals("0")) {
					throw new BusinessException(ResponseCode.CFG_INVALID_ID, HttpStatus.NOT_FOUND);
				} else {
					List<Terminal> terminal2 = terminalRepository.findByTerminalIdAndInstitutionEntity(terminalRequestDto.getTerminalId(), institution);
					
					if ( (!terminal2.isEmpty()) || String.valueOf(terminalRequestDto.getUpdateFlag()).equals("1")) {
						throw new BusinessException(ResponseCode.CFG_TERMINAL_ID_EXISTS_OR_FLAG_INVALID, HttpStatus.NOT_FOUND);
					}

					Terminal terminal1;

					terminal1 = terminalMapper.toEntity(terminalRequestDto);
					terminal1.setTerminalId(terminalRequestDto.getTerminalId());
					terminal1.setCurrency(currency);
					terminal1.setMccList(mccList.getMcc());
					terminal1.setTerminalTypes(terminalTypes);
					
					
					terminal1.setEntitiesObject(entities);
					terminal1.setEntities(entities.getEntityId());

					terminal1.setStatus(terminalRequestDto.getStatus().charAt(0));
					terminal1.setInstitutionEntity(institution);
					terminal1.setInstitution(institution.getInstitutionId());
					terminal1.setDateCreate(new Date());
					if(userDetails!=null) {
						terminal1.setUserCreate(Integer.valueOf(userDetails.getId()).toString());
					}
					terminal1 = terminalRepository.save(terminal1);
					
					if(switchSystemCode.getCodeSuffix().equals("1")) {
						this.saveSwitchTerminal(terminal1);
					}
					terminalResponseDto = terminalMapper.toResponseDto(terminal1);
					terminalResponseDto.setRecordSeqId(terminal1.getRecordSequenceId());
					terminalResponseDto.setMcc(mccList.getMcc());
					terminalResponseDto.setMccId(mccList.getMccId());

				}
			}
		} else {
			throw new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND);
		}
		return terminalResponseDto;

	}


	public void deleteTerminal(DeleteTerminalRequestDto deleteTerminalRequestDto) {
		String terminalId = deleteTerminalRequestDto.getTerminalId();
		String instId = deleteTerminalRequestDto.getInstId();

		Terminal terminal = terminalRepository.findByTerminalIdAndInstitutionEntity_InstitutionId(terminalId, instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(deleteTerminalRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		terminalRepository.deleteById(terminal.getRecordSequenceId());
		
		SystemCode switchSystemCode = this.systemCodeRepository.findByCodePrefixAndCodeValueAndInstitution_InstitutionId("SWITCH_ENABLED", "SWITCH_ENABLED_FLAG", "SYSTEM")
				.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_SYSTEM_CODE_ID, HttpStatus.NOT_FOUND));
		
		if(switchSystemCode.getCodeSuffix().equals("1")) {
			this.switchTerminalService.deleteTerminal(terminalId);
		}

	}

	public TerminalResponseDto getTerminal(String terminalId, String instId) {
		// TODO Auto-generated method stub

		Terminal terminal = terminalRepository.findByTerminalIdAndInstitutionEntity_InstitutionId(terminalId, instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.NOT_FOUND));

		List<MCCList> mccList = mccListRepository.findByMcc(terminal.getMccList());

        TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);

        if (!(mccList.isEmpty())) {
            terminalResponseDto.setMcc(mccList.get(0).getMcc());
            terminalResponseDto.setMccId(mccList.get(0).getMccId());
            terminalResponseDto.setMccDescription(mccList.get(0).getDescription());
        }
        terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
        return terminalResponseDto;
    }

	public List<TerminalResponseDto> getTerminalByInstitutionId(String institutionId) {
		// TODO Auto-generated method stub

		List<Terminal> terminals = terminalRepository.findByInstitutionEntity(institutionId,
				Sort.by(Sort.Direction.ASC, "terminalId"));

		List<TerminalResponseDto> terminalDtos = new ArrayList<>();

		terminals.forEach((terminal) -> {
			List<MCCList> mccList = mccListRepository.findByMcc(terminal.getMccList());
			TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);
			if (!(mccList.isEmpty())) {
				terminalResponseDto.setMcc(mccList.get(0).getMcc());
				terminalResponseDto.setMccId(mccList.get(0).getMccId());

			}
			terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
			terminalDtos.add(terminalResponseDto);
		});

		return terminalDtos;
	}

	public List<TerminalResponseDto> getTerminalByEntitiesId(String entitiesId) {
		// TODO Auto-generated method stub

		List<Terminal> terminals = terminalRepository.findByEntitiesObject(entitiesId,Sort.by(Sort.Direction.ASC, "terminalId"));

		List<TerminalResponseDto> terminalDtos = new ArrayList<>();

		terminals.forEach((terminal) -> {

			List<MCCList> mccList = mccListRepository.findByMcc(terminal.getMccList());
			TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);
			if (!(mccList.isEmpty())) {
				terminalResponseDto.setMcc(mccList.get(0).getMcc());
				terminalResponseDto.setMccId(mccList.get(0).getMccId());

			}
			terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
			terminalDtos.add(terminalResponseDto);
		});
//		}

		return terminalDtos;
	}

	public List<TerminalResponseDto> getTerminalByEntitiesIdAndInstitutionId(String entitiesId, String institutionId) {
		// TODO Auto-generated method stub

		List<Terminal> terminals = terminalRepository.findByEntitiesObjectAndInstitutionEntity(entitiesId, institutionId,
				Sort.by(Sort.Direction.ASC, "terminalId"));

		List<TerminalResponseDto> terminalDtos = new ArrayList<>();

		if (terminals.isEmpty()) {
			throw new BusinessException(ResponseCode.CFG_INVALID_INST, HttpStatus.NOT_FOUND);
		} else {
			terminals.forEach((terminal) -> {

				List<MCCList> mccList = mccListRepository.findByMcc(terminal.getMccList());
				TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);
				if (!(mccList.isEmpty())) {
					terminalResponseDto.setMcc(mccList.get(0).getMcc());
					terminalResponseDto.setMccId(mccList.get(0).getMccId());

				}
				terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
				terminalDtos.add(terminalResponseDto);
			});
		}

		return terminalDtos;
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		Terminal terminal = terminalRepository.findByTerminalIdAndInstitutionEntity_InstitutionId(String.valueOf(changeStatusRequestDto.getId()),changeStatusRequestDto.getInstId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_ID, HttpStatus.NOT_FOUND));
		terminal.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		terminalRepository.save(terminal);

		return "Status changed successfully";
	}

	public List<TerminalResponseDto> getActiveTerminal() {
		List<Terminal> activeTerminal = terminalRepository.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "terminalId"));
		List<TerminalResponseDto> dto = new ArrayList<>();

		for (Terminal temp : activeTerminal) {

			List<MCCList> mccList = mccListRepository.findByMcc(temp.getMccList());
			TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(temp);
			if (!(mccList.isEmpty())) {
				terminalResponseDto.setMcc(mccList.get(0).getMcc());
				terminalResponseDto.setMccId(mccList.get(0).getMccId());

			}

			// TerminalResponseDto dtoTemp = terminalMapper.toResponseDto(temp);
			terminalResponseDto.setRecordSeqId(temp.getRecordSequenceId());
			dto.add(terminalResponseDto);
		}
		return dto;
	}

	public ResponseEntity<PaginationResponseDto> viewTerminal(AllTerminalsRequestDto terminalRequestDto) {
		// TODO Auto-generated method stub

		Page<Terminal> page;

		PaginationCommonCode paginationCommonCode = new PaginationCommonCode();

		PageRequest pageRequest = paginationCommonCode.getPageRequestForPagination(terminalRequestDto.getSort(),
				"terminalId", terminalRequestDto.getPageNo(), terminalRequestDto.getPageSize());

		if (terminalRequestDto.getEntityId() != null) {
			page = terminalRepository.findAllByEntitiesObject(terminalRequestDto.getEntityId(), pageRequest);
		} else {
			page = terminalRepository.findAll(pageRequest);
		}

		List<TerminalResponseDto> terminalDtos = new ArrayList<>();

		page.getContent().forEach((terminal) -> {

			List<MCCList> mccList = mccListRepository.findByMcc(terminal.getMccList());

			TerminalResponseDto terminalResponseDto = terminalMapper.toResponseDto(terminal);
			terminalResponseDto.setPageNo(terminalRequestDto.getPageNo());
			terminalResponseDto.setPageSize(terminalRequestDto.getPageSize());
			if (!(mccList.isEmpty())) {
				terminalResponseDto.setMcc(mccList.get(0).getMcc());
				terminalResponseDto.setMccId(mccList.get(0).getMccId());
				terminalResponseDto.setMccDescription(mccList.get(0).getDescription());
			}
			terminalResponseDto.setRecordSeqId(terminal.getRecordSequenceId());
			terminalDtos.add(terminalResponseDto);
		});

		return new ResponseEntity<>(
				new PaginationResponseDto(true, null, terminalDtos, page.getTotalPages(), page.getTotalElements()),
				HttpStatus.OK);

	}
	
	public void saveSwitchTerminal(Terminal terminal) {
		SwitchTerminalRequestDto switchTerminalRequestDto = new SwitchTerminalRequestDto();
		
		if(!Objects.isNull(terminal.getEntities())) {
			switchTerminalRequestDto.setOwnerEntityId(terminal.getEntitiesObject().getEntityId());
			switchTerminalRequestDto.setTerminalEntityId(terminal.getEntitiesObject().getEntityId());
			switchTerminalRequestDto.setDefaultMcc(terminal.getMccList());
		}
		
		if(!Objects.isNull(terminal.getCurrency())) {
			switchTerminalRequestDto.setDefaultCurrCd(terminal.getCurrency().getCurrencyCode());
		}
		
		if(!Objects.isNull(terminal.getTerminalTypes())) {
			switchTerminalRequestDto.setPosTerminalType(terminal.getTerminalTypes().getTerminalType());
		}
		
		switchTerminalRequestDto.setActualStartDate(terminal.getActualStartDate());
		switchTerminalRequestDto.setInstitutionId(terminal.getInstitutionEntity().getInstitutionId());
		switchTerminalRequestDto.setTermninationDate(terminal.getTerminationDate());
		switchTerminalRequestDto.setTerminalId(terminal.getTerminalId());
		switchTerminalRequestDto.setSerialNumber(terminal.getSerialNumber());
		switchTerminalRequestDto.setPosTerminalType(terminal.getTerminalTypes().getTerminalType());
		
		this.switchTerminalService.saveTerminal(switchTerminalRequestDto);
	}

}