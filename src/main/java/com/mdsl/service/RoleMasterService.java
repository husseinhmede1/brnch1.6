package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdsl.model.dto.response.RoleMasterResponseDto;
import com.mdsl.model.entity.RoleMaster;
import com.mdsl.model.mapper.RoleMasterMapper;
import com.mdsl.repository.RoleMasterRepository;
import com.mdsl.utils.MakerCheckerEngine;

@Service
public class RoleMasterService {
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	@Autowired
	private RoleMasterMapper roleMasterMapper;
	@Autowired
	private MakerCheckerEngine makerCheckerEngine;
	
	public List<RoleMasterResponseDto> getAllRole() {
		List<RoleMaster> allRoleMaster = roleMasterRepository.findAll();
		List<RoleMasterResponseDto> allRoleMasterResponseDto = new ArrayList<RoleMasterResponseDto>();
		for (RoleMaster r : allRoleMaster) {
			RoleMasterResponseDto roleMasterResponseDto = roleMasterMapper.toDto(r);
			allRoleMasterResponseDto.add(roleMasterResponseDto);
		}

		return allRoleMasterResponseDto;
	}
	
	public RoleMasterResponseDto saveRole(RoleMaster roleMaster) {
		if (makerCheckerEngine.processIfRequired(roleMaster, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		roleMasterRepository.save(roleMaster);
		return roleMasterMapper.toDto(roleMaster);
	}
	
}
