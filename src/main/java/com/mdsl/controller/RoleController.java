package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.mdsl.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.RoleRequestDto;
import com.mdsl.model.dto.response.RoleResponseDto;
import com.mdsl.service.RoleService;
//import com.mdsl.model.dto.request.ChangeStatusRequestDto;
//import com.mdsl.model.dto.request.RoleRequestDto;
//import com.mdsl.model.dto.response.RoleResponseDto;
//import com.mdsl.service.RoleService;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@Transactional(rollbackOn = Exception.class)
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);


	@GetMapping
	public ResponseEntity<List<RoleResponseDto>> getAllRoles(){
		return ResponseEntity.ok(roleService.getAllRoles());
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable("id") int roleId) {
		return ResponseEntity.ok(roleService.getRoleById(roleId));
	}

	
	@GetMapping("/active")
	public ResponseEntity<List<RoleResponseDto>> getActiveRoles() {
		return ResponseEntity.ok(roleService.getActiveRoles());
	}

	@PostMapping
	public ResponseEntity saveRole(@Valid @RequestBody RoleRequestDto role, BindingResult bindingResult,HttpServletRequest request) {
		Validations.validate(bindingResult);
		try {
			return ResponseEntity.ok(roleService.saveRole(role));
		} catch(BusinessException ex){
			logger.error("@RoleController#saveRole-business exception "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@RoleController#saveRole-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.VAL_ERROR_OCCURRED,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@DeleteMapping("/{id}")
//	public void deleteRole(@PathVariable(value = "id") int roleId, HttpServletRequest request) {
//		roleService.deleteRole(roleId, request.getHeader("instId"), request.getRemoteAddr());
//	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable(value = "id") int roleId, HttpServletRequest request) {
		
		try {
			roleService.deleteRole(roleId);
			String message = "An item is deleted with id : " + roleId;
			return ResponseEntity.ok(message);
		} catch(BusinessException ex){
			logger.error("@RoleController#deleteRole-business exception "+ex.toString());
			throw new BusinessException(ex.getMessage(),ex.getHttpStatus());
		} catch(Exception ex){
			logger.error("@RoleController#deleteRole-generic exception "+ex.toString());
			throw new BusinessException(ResponseCode.CFG_ROLE_NO_DELETE,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeRoleStatus(@Valid @RequestBody ChangeStatusRequestDto changeRoleStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(roleService.changeRoleStatus(changeRoleStatusRequestDTO));
	}
}