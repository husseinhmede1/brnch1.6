package com.mdsl.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

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
import com.mdsl.model.dto.request.EmployeeRequestDto;
import com.mdsl.model.dto.response.EmployeeResponseDto;
import com.mdsl.service.EmployeeService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService employeeServices;
	
	@GetMapping
	public ResponseEntity<List<EmployeeResponseDto>> getEmployees() {
		return ResponseEntity.ok(employeeServices.getAllEmployees());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable(value = "id") int instId) {
		return ResponseEntity.ok(employeeServices.getEmployeeById(instId));
	}

	@GetMapping("/institution/{id}")
	public ResponseEntity<List<EmployeeResponseDto>> getEmployeeByInstitutionId(@PathVariable(value = "id") String instId) {
		return ResponseEntity.ok(employeeServices.getEmployeeByInstitutionId(instId));
	}

	@PostMapping
	public ResponseEntity<EmployeeResponseDto> saveOrUpdateEmployee(
			@Valid @RequestBody EmployeeRequestDto employeeRequestDto, BindingResult bindingResult) {
			Validations.validate(bindingResult);
			try{
				return ResponseEntity.ok(employeeServices.saveOrUpdateEmployee(employeeRequestDto));
			} catch (BusinessException e) {
			    throw new BusinessException (e.getMessage(), e.getHttpStatus());
			} catch (Exception e) {
			    logger.error("@EmployeeController#saveOrUpdateEmployee: " + e.getMessage());
			    throw new BusinessException (ResponseCode.EMP_EMPLOYEE_NO_SAVE, HttpStatus.BAD_REQUEST);
		    }
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") int id) {
		try {
			employeeServices.deleteEmployee(id);
			return ResponseEntity.ok(new String ("Employee deleted successfully"));
		} catch (BusinessException e) {
		    throw new BusinessException (e.getMessage(), e.getHttpStatus());
		} catch (Exception e) {
		    logger.error("@EmployeeController#deleteEmployee: " + e.getMessage());
		    throw new BusinessException (ResponseCode.EMP_EMPLOYEE_NO_DELETE, HttpStatus.BAD_REQUEST);
	    }
	}

	@PostMapping("/status-change")
	public ResponseEntity<String> changeEmployeeStatus(@Valid @RequestBody ChangeStatusRequestDto changeEmployeeStatusRequestDTO,BindingResult bindingResult, HttpServletRequest request) {
		Validations.validate(bindingResult);
		return ResponseEntity.ok(employeeServices.changeStatus(changeEmployeeStatusRequestDTO));
	}
	
	@GetMapping("/active-employee")
	public ResponseEntity<List<EmployeeResponseDto>> getActiveEmployees(){
		return ResponseEntity.ok(employeeServices.getActiveEmployees());
	}
}
