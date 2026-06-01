package com.mdsl.swtch.service;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.swtch.model.dto.request.SwitchEntitiesRequestDto;
import com.mdsl.swtch.model.entity.SwitchEntities;
import com.mdsl.swtch.model.mapper.SwitchEntitiesMapper;
import com.mdsl.swtch.repository.SwitchEntitiesRepository;
import com.mdsl.utils.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwitchEntitiesService {
	
	private final SwitchEntitiesRepository entitiesRepository;
	
	private final SwitchEntitiesMapper entitiesMapper;
	
	public void saveEntity(SwitchEntitiesRequestDto entitiesRequestDto) {
		SwitchEntities entity = this.entitiesMapper.toEntity(entitiesRequestDto);
		
		entity.setReclassInd('S');
		entity.setLanguageCode("EN");
		entity.setMultiTranId('n');
		entity.setMultiBatchFlag('n');
		entity.setCreationDate(new Date(System.currentTimeMillis()));
		
		this.entitiesRepository.save(entity);
	}
	
	public void deleteEntity(String entityId) {
		SwitchEntities entity = this.entitiesRepository.findById(entityId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_ENTITY, HttpStatus.NOT_FOUND));
		this.entitiesRepository.delete(entity);
	}

}
