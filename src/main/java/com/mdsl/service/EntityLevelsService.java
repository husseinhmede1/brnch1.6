package com.mdsl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.EntityLevelsRequestDto;
import com.mdsl.model.dto.response.EntityLevelsResponseDto;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.EntityLevels;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.mapper.EntityLevelsMapper;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.EntityLevelsRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.utils.ResponseCode;

@Service
public class EntityLevelsService {
	@Autowired
	private EntityLevelsRepository entityLevelsRepository;
	@Autowired
	private EntitiesRepository entitiesRepository;
	@Autowired
	private EntityLevelsMapper entityLevelsMapper;
	@Autowired
	private InstitutionRepository institutionRepository;

	public EntityLevelsResponseDto saveEntityLevel(EntityLevelsRequestDto entityLevelsRequestDto) {
		Institution institution = institutionRepository.findById(entityLevelsRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		EntityLevels entityLevels = entityLevelsMapper.toEntity(entityLevelsRequestDto);
		entityLevels.setInstitution(institution);
		entityLevels.setDateCreate(new java.sql.Date(new java.util.Date().getTime()));
		entityLevels = entityLevelsRepository.save(entityLevels);
		return entityLevelsMapper.toDto(entityLevels);
	}

	public List<EntityLevelsResponseDto> getAllEntityLevels() {
		List<EntityLevels> allEntityLevels = entityLevelsRepository.findAll();
		List<EntityLevelsResponseDto> allEntityLevelsResponseDto = new ArrayList<EntityLevelsResponseDto>();
		EntityLevelsResponseDto entityLevelsResponseDto = null;
		for (EntityLevels entityLevel : allEntityLevels) {
			entityLevelsResponseDto = entityLevelsMapper.toDto(entityLevel);
			allEntityLevelsResponseDto.add(entityLevelsResponseDto);
		}
		return allEntityLevelsResponseDto;
	}

	public void deleteEntityLevel(@RequestParam int entityLevelId) throws Exception {
		EntityLevels entityLevel = entityLevelsRepository.findById(entityLevelId)
				.orElseThrow(() -> new BusinessException(ResponseCode.ENT_ENTITYLEVELS_CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<Entities> entities = entitiesRepository.findByEntityLevelsAndInstitution(entityLevel.getHierarchyLevel(),entityLevel.getInstitution());
		if ((entities.isEmpty())) {
			entityLevelsRepository.deleteById(entityLevelId);
		}
		else {
			throw new BusinessException(ResponseCode.CFG_REFERENCE_EXISTS, HttpStatus.NOT_FOUND);
		}
	}

	public List<EntityLevelsResponseDto> getAllEntityLevelsByInstitutionId(String institutionId) {
		List<EntityLevels> allEntityLevels = entityLevelsRepository.findByInstitution_InstitutionId(institutionId,
				Sort.by(Sort.Direction.ASC, "entityLevelId"));
		List<EntityLevelsResponseDto> allEntityLevelsResponseDto = new ArrayList<EntityLevelsResponseDto>();
		EntityLevelsResponseDto entityLevelsResponseDto = null;
		for (EntityLevels entityLevel : allEntityLevels) {
			entityLevelsResponseDto = entityLevelsMapper.toDto(entityLevel);
			allEntityLevelsResponseDto.add(entityLevelsResponseDto);
		}
		return allEntityLevelsResponseDto;
	}

}
