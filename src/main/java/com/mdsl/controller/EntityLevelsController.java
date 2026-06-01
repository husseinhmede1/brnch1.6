package com.mdsl.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.EntityLevelsRequestDto;
import com.mdsl.model.dto.response.EntityLevelsResponseDto;
import com.mdsl.service.EntityLevelsService;
import com.mdsl.utils.ResponseCode;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/entitylevels")
public class EntityLevelsController {
	private EntityLevelsService entityLevelsService;
	private static final Logger logger = LoggerFactory.getLogger(EntityLevelsController.class);

	EntityLevelsController(EntityLevelsService entityLevelsService) {
		this.entityLevelsService = entityLevelsService;
	}

	@GetMapping
	public ResponseEntity<List<EntityLevelsResponseDto>> getAllEntityLevels() {
		return ResponseEntity.ok(entityLevelsService.getAllEntityLevels());
	}
	
	@GetMapping("/institution/{id}")
	public ResponseEntity<List<EntityLevelsResponseDto>> getAllEntityLevelsByInstitutionId(@PathVariable(value="id") String institutionId) {
		return ResponseEntity.ok(entityLevelsService.getAllEntityLevelsByInstitutionId(institutionId));
	}

	@PostMapping
	public ResponseEntity<EntityLevelsResponseDto> saveEntityLevel(@Valid @RequestBody EntityLevelsRequestDto entityLevelsRequestDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(entityLevelsService.saveEntityLevel(entityLevelsRequestDto));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@EntityLevelsController#saveEntityLevel: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ENT_ENTITY_LEVEL_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEntityLevel(@PathVariable(value = "id") int entityLevelId) {
		try {
			entityLevelsService.deleteEntityLevel(entityLevelId);
			return ResponseEntity.ok(new String("Entity Level deleted successfully"));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@EntityLevelsController#deleteEntityLevel: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ENT_ENTITY_LEVEL_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}
}