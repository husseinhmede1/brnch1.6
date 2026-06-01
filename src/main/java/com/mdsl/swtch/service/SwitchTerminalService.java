package com.mdsl.swtch.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.swtch.model.dto.request.SwitchTerminalRequestDto;
import com.mdsl.swtch.model.entity.SwitchGlobalSeqCtrl;
import com.mdsl.swtch.model.entity.SwitchTerminal;
import com.mdsl.swtch.model.mapper.SwitchTerminalMapper;
import com.mdsl.swtch.repository.SwitchGlobalSeqCtrlRepository;
import com.mdsl.swtch.repository.SwitchTerminalRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwitchTerminalService {
	
	private final SwitchTerminalRepository terminalSwicthRepository;
	private final SwitchGlobalSeqCtrlRepository globalSeqCtrlRepository;
	
	private final SwitchTerminalMapper terminalMapper;
	
	public void saveTerminal(SwitchTerminalRequestDto terminalRequestDto) {
		SwitchTerminal terminal = this.terminalMapper.toEntity(terminalRequestDto);
		
		SwitchGlobalSeqCtrl globalSeqCtrl = this.globalSeqCtrlRepository.findBySeqName("term_record_seq")
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ID_SWITCH_GLOBAL, HttpStatus.NOT_FOUND));
		
		Optional<SwitchTerminal> optTerminal = this.terminalSwicthRepository.findByTerminalIdHigh(terminalRequestDto.getTerminalId());
		
		if(optTerminal.isEmpty()) {	//add
			globalSeqCtrl.setLastSeqNbr(globalSeqCtrl.getLastSeqNbr() + 1);

			terminal.setTermRecordSeq(globalSeqCtrl.getLastSeqNbr());
			terminal.setTerminalIdLow(terminalRequestDto.getTerminalId());
			terminal.setTerminalIdHigh(terminalRequestDto.getTerminalId());
//			terminal.setTerminalIdNr(Integer.valueOf(terminalRequestDto.getTerminalId())); REQUESTED BY ELIE 22-01-2025
			terminal.setSerialNumber(terminalRequestDto.getSerialNumber());
			terminal.setTerminalEntityId(terminalRequestDto.getTerminalEntityId());
			terminal.setPosMsgBrand("7");
			terminal.setCpsEligible('N');
			terminal.setCaptureType('T');
			
			this.terminalSwicthRepository.save(terminal);
			
			this.globalSeqCtrlRepository.save(globalSeqCtrl);
		}
		else {	//update
			terminal = optTerminal.get();
			terminal.setSerialNumber(terminalRequestDto.getSerialNumber());
			terminal.setActualStartDate(terminalRequestDto.getActualStartDate());
			terminal.setTermninationDate(terminalRequestDto.getTermninationDate());
			terminal.setDefaultCurrCd(terminalRequestDto.getDefaultCurrCd());
			terminal.setDefaultMcc(terminalRequestDto.getDefaultMcc());
			terminal.setPosTerminalType(terminalRequestDto.getPosTerminalType());
			
			this.terminalSwicthRepository.save(terminal);
		}
		
	}
	
	public void deleteTerminal(String termninalId) {
		SwitchTerminal terminal = this.terminalSwicthRepository.findByTerminalIdHigh(termninalId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_SWITCH_GLOBAL_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		this.terminalSwicthRepository.delete(terminal);
	}

}
