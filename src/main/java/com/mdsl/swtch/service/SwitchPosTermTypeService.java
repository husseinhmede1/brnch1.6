package com.mdsl.swtch.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.swtch.model.entity.SwitchPosTermType;
import com.mdsl.swtch.repository.SwitchPosTermTypeRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwitchPosTermTypeService {
	
	private final SwitchPosTermTypeRepository posTermTypeRepository;
	
	public void savePosTermType(String posTerminalType, String posTermMake, String posTermModel) {
		SwitchPosTermType posTermType = new SwitchPosTermType();
		
		posTermType.setPosTerminalType(posTerminalType);
		posTermType.setPosTermMake(posTermMake);
		posTermType.setPosTermModel(posTermModel);
		posTermType.setCardInputCapab('F');
		posTermType.setChAuthCapab('1');
		posTermType.setCardCapCapab('0');
		posTermType.setOperEnv('1');
		posTermType.setCvvEligible('0');
		posTermType.setCardUpdate('3');
		posTermType.setTerminalOutput('4');
		posTermType.setPinCapture('4');
		posTermType.setKeysLoadType('S');
		posTermType.setSrvrEstrCd('1');
		posTermType.setCondCd('1');
		posTermType.setSendsBatchNr('Y');
		posTermType.setReversalInd('0');
		posTermType.setDeviceType('1');
		posTermType.setSends320Twice('N');
		posTermType.setCapabilities("Y");
		
		this.posTermTypeRepository.save(posTermType);
	}
	
	
	
	public void deletePosTermType(String posTerminalType) {
		SwitchPosTermType posTermType = this.posTermTypeRepository.findById(posTerminalType)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_TERMINAL_TYPE, HttpStatus.NOT_FOUND));
		this.posTermTypeRepository.delete(posTermType);
	}

}
