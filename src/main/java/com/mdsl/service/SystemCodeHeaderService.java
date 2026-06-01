package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.SystemCodeHeaderResponseDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.SystemCodeHeader;
import com.mdsl.model.mapper.SystemCodeHeaderMapper;
import com.mdsl.repository.SystemCodeHeaderRepository;

@Service
@Transactional(rollbackOn = Exception.class)
public class SystemCodeHeaderService {

	@Autowired
	private SystemCodeHeaderRepository systemCodeHeaderRepository;
	
	
	@Autowired
	private SystemCodeHeaderMapper systemCodeHeaderMapper;

	public List<SystemCodeHeaderResponseDto> fetchAllSystemCodesHeader() {
		List<SystemCodeHeader> systemCodeHeaders = systemCodeHeaderRepository.findAll(Sort.by(Sort.Direction.ASC, "systemCodeHeaderId"));
		List<SystemCodeHeaderResponseDto> dto = new ArrayList<SystemCodeHeaderResponseDto>();

		for (SystemCodeHeader tempSystemCode : systemCodeHeaders) {
			SystemCodeHeaderResponseDto tempDto = systemCodeHeaderMapper.toDto(tempSystemCode);
			dto.add(tempDto);
		}
		return dto;
	}
}
