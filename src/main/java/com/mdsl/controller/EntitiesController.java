package com.mdsl.controller;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.DeleteEntityRequestDto;
import com.mdsl.model.dto.request.EntityFilterRequestDto;
import com.mdsl.model.dto.request.EntityRequestDto;
import com.mdsl.model.dto.request.SaveCloneEntityRequestDto;
import com.mdsl.model.dto.response.EntitiesResponseDto;
import com.mdsl.model.dto.response.PaginationResponseDto;
import com.mdsl.model.dto.response.TransactionMerchantNamesListDto;
import com.mdsl.service.EntitiesServices;
import com.mdsl.service.TransactionCurrentService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/entities")
public class EntitiesController {
	private static final Logger logger = LoggerFactory.getLogger(EntitiesController.class);

	@Autowired
	EntitiesServices entitiesServices;

	@Autowired
	TransactionCurrentService transactionCurrentService;

	@GetMapping
	public ResponseEntity<List<EntitiesResponseDto>> getEntities() {
		return ResponseEntity.ok(entitiesServices.getAllEntities());
	}

	@PostMapping("/getEntities")
	public ResponseEntity<PaginationResponseDto> entitiesPagination(@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = entitiesServices.viewEntities(entityRequestDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@GetMapping("/{id}/{instId}")
	public ResponseEntity getEntitieById(@PathVariable(value = "id") String entityId,@PathVariable(value = "instId")String instId) {

		try {
			return ResponseEntity.ok(entitiesServices.getEntityById(entityId,instId));
		} catch (BusinessException e) {
			if (e.getMessage().trim().equals("ENT-001")) {
				System.out.println("Id not found");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Entity Not Found");
			}else {
				throw new BusinessException(ResponseCode.CFG_INVALID_ENTITY, HttpStatus.NOT_FOUND);
			}
		}
	}

	@PostMapping(value = "/institution")
	public ResponseEntity getEntitiesByInstitution(HttpServletRequest httpServletRequest,
			@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			if (entityRequestDto.getInstitutionId() == null) {
				String instId = httpServletRequest.getHeader("instId");
				if (instId != null) {
					entityRequestDto.setInstitutionId(instId);
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Institution Id not found");
				}
			}

			responseEntity = entitiesServices.getEntitiesByInstitution(entityRequestDto);

		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@GetMapping(value = "/institution/{id}")
	public ResponseEntity<List<EntitiesResponseDto>> getEntitiesByInstitutionGet(@PathVariable("id") String id) {
		List<EntitiesResponseDto> entities = entitiesServices.getEntitiesByInstitutionGet(id);
		return ResponseEntity.ok(entities);
	}

	@GetMapping("/clone/{id}/{instId}")
	public ResponseEntity<EntitiesResponseDto> cloneEntity(@PathVariable(value = "id") String id,@PathVariable(value = "instId") String instId ) {
		try {
			return ResponseEntity.ok(entitiesServices.cloneEntity(id, instId));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping
	public ResponseEntity saveOrUpdateEntity(@Valid @RequestBody EntityRequestDto entityRequestDto,
			BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			Validations.validate(bindingResult);

			return ResponseEntity.ok(entitiesServices.saveOrUpdateEntity(entityRequestDto));

		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@EntitiesController#saveOrUpdateEntity: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ENT_ENTITY_NO_SAVE, HttpStatus.BAD_REQUEST);
	    }
	}

	@DeleteMapping("/{id}/{instId}")
	public ResponseEntity<String> deleteEntity(@PathVariable(value = "id") String entityId,@PathVariable(value = "instId")String instId) {
		try {
			entitiesServices.deleteEntity(DeleteEntityRequestDto.builder().entityId(entityId).instId(instId).build());
			return ResponseEntity.ok(new String("Entity deleted successfully"));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@EntitiesController#deleteEntity: " + e.getMessage());
		    throw new BusinessException (ResponseCode.ENT_ENTITY_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
//		return null;

	}

	@PostMapping("/search")
	public ResponseEntity<PaginationResponseDto> getEntitieBySearch(HttpServletRequest httpServletRequest,
			@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		ResponseEntity<PaginationResponseDto> responseEntity = null;
		try {
			responseEntity = entitiesServices.getEntitiesBySearch(entityRequestDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<>(new PaginationResponseDto(false, null, null, null, null),
					HttpStatus.OK);
		}
		return responseEntity;

	}

	@PostMapping("/filter")
	public ResponseEntity<List<EntitiesResponseDto>> getEntitiesBySearchCriteria(HttpServletRequest httpServletRequest,
			@RequestBody EntityFilterRequestDto entityFilterRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(entitiesServices.getEntitiesBySearchCriteria(entityFilterRequestDto));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/status-change/{instId}")
	public ResponseEntity changeEntitiesStatus(
			@Valid @RequestBody ChangeStatusRequestDto changeInstitutionStatusRequestDTO, BindingResult bindingResult,@PathVariable(value = "instId")String instId) {
		Validations.validate(bindingResult);
		try {
			changeInstitutionStatusRequestDTO.setInstId(instId);
			entitiesServices.changeStatus(changeInstitutionStatusRequestDTO);
		} catch (BusinessException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getErrorCode());
			if (e.getMessage().trim().equals("ACT-006")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
			} else if (e.getMessage().trim().equals("ENT-111")) {
				System.out.println("can't update status");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Entity Status can not be enabled");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot enable child entity as parent entity is disabled");
			}
		}
		return null;
	}
	
	@PostMapping("/entitylevel")
	public ResponseEntity<List<EntitiesResponseDto>> getEntitiesByEntityLevel(@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(entitiesServices.getEntitiesByEntityLevel(entityRequestDto));
	}
	
	@PostMapping("/entitylevel/institution")
	public ResponseEntity<List<EntitiesResponseDto>> getEntitiesByEntityLevelAndInstitution(@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(entitiesServices.getEntitiesByEntityLevelAndInstitution(entityRequestDto));
	}
	
	@PostMapping("/outlet")
	public ResponseEntity<List<EntitiesResponseDto>> getEntitiesByEntityLevelOutlet(@RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(entitiesServices.getEntitiesByEntityLevelOutlet(entityRequestDto));
	}
	
	@GetMapping("/active-entities")
	public ResponseEntity<List<EntitiesResponseDto>> getActiveEntities() {
		return ResponseEntity.ok(entitiesServices.getActiveEntities());
	}

	@GetMapping(value = "/has-children/{id}")
	public ResponseEntity<Boolean> hasChildrenEntities(@PathVariable("id") String id) {
		Boolean hasChildren = entitiesServices.hasChildrenEntities(id);
		return ResponseEntity.ok(hasChildren);
	}

	@PostMapping("/save-clone/{id}")
	public ResponseEntity<EntitiesResponseDto> saveCloneEntity(@PathVariable("id") String id, @RequestBody EntityRequestDto entityRequestDto, BindingResult bindingResult) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(entitiesServices.saveCloneEntity(SaveCloneEntityRequestDto.builder().clonedEntityId(id).entityRequestDto(entityRequestDto).build()));
	}
	@GetMapping("/transaction-merchant-names/{id}")
	public ResponseEntity<List<String>> getTransactionMerchantNames(@PathVariable("id") String instId){
		return ResponseEntity.ok(transactionCurrentService.getTransactionMerchantNames(instId));
	}

}
