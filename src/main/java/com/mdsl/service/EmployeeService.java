package com.mdsl.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.ChangeStatusRequestDto;
import com.mdsl.model.dto.request.EmployeeRequestDto;
import com.mdsl.model.dto.response.EmployeeResponseDto;
import com.mdsl.model.entity.Employee;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.mapper.EmployeeMapper;
import com.mdsl.repository.EmployeeRepository;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.RoleMasterRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.utils.MakerCheckerEngine;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.StatusEnum;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private RoleMasterRepository roleMasterRepo;

	@Autowired
	private InstitutionRepository institutionRepository;

	@Autowired
	private SystemCodeRepository systemCodeRepository;

	@Autowired
	private MakerCheckerEngine makerCheckerEngine;

	public List<EmployeeResponseDto> getAllEmployees() {
		List<Employee> allEmployee = empRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));
		List<EmployeeResponseDto> allEmployeeResponseDto = new ArrayList<EmployeeResponseDto>();
		allEmployee.stream().forEach((employee) -> {
			EmployeeResponseDto employeeResponseDto = employeeMapper.toDto(employee);
			allEmployeeResponseDto.add(employeeResponseDto);
		});
		return allEmployeeResponseDto;
	}

	public void deleteEmployee(int id) throws Exception {
		empRepo.findById(id).orElseThrow(
				() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
		if (makerCheckerEngine.processIfRequired(id, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return;
		}
		empRepo.deleteById(id);
	}

	public EmployeeResponseDto getEmployeeById(int empId) {
		Employee employee = empRepo.findById(empId)
				.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
		EmployeeResponseDto employeeResponseDto = employeeMapper.toDto(employee);
		return employeeResponseDto;
	}

	public List<EmployeeResponseDto> getEmployeeByInstitutionId(String instId) {
		Institution institution = institutionRepository.findById(instId)
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		List<Employee> allInstitutionEmployee = empRepo.findByInstitution(institution,
				Sort.by(Sort.Direction.ASC, "employeeId"));
		List<EmployeeResponseDto> allEmployeeResponseDto = new ArrayList<EmployeeResponseDto>();
		allInstitutionEmployee.stream().forEach((employee) -> {
			EmployeeResponseDto employeeResponseDto = employeeMapper.toDto(employee);
			allEmployeeResponseDto.add(employeeResponseDto);
		});
		return allEmployeeResponseDto;
	}

	public EmployeeResponseDto saveOrUpdateEmployee(EmployeeRequestDto employeeRequestDto) {
		Employee employee = null;
		SystemCode role = systemCodeRepository.findById(employeeRequestDto.getEmployeeRoleId())
				.orElseThrow(() -> new BusinessException(ResponseCode.ROL_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));
		Institution institution = institutionRepository.findById(employeeRequestDto.getInstitutionId())
				.orElseThrow(() -> new BusinessException(ResponseCode.CFG_INSTITUTION_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (makerCheckerEngine.processIfRequired(employeeRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}

		if (employeeRequestDto.getEmployeeId() != 0) {
			employee = empRepo.findById(employeeRequestDto.getEmployeeId())
					.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
			employee.setEmployeeName(employeeRequestDto.getEmployeeName());
			employee.setEmployeePhone(employeeRequestDto.getEmployeePhone());
			employee.setEmployeeExt(employeeRequestDto.getEmployeeExt());
			employee.setEmployeeRole(role);
			employee.setInstitution(institution);
			employee.setCreatedBy(employee.getCreatedBy());
			employee.setCreatedDate(employee.getCreatedDate());
			employee.setUpdatedBy(Integer.valueOf(userDetails.getId()));
			employee.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			
			if(empRepo.existsByInstitutionAndEmployeeNameIgnoreCaseAndEmployeeIdNot(institution,employeeRequestDto.getEmployeeName(),employeeRequestDto.getEmployeeId())){
				throw new BusinessException(ResponseCode.EMP_EMPLOYEE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}

			
			employee = empRepo.save(employee);
			return employeeMapper.toDto(employee);
		} else {
			employee = employeeMapper.toEntity(employeeRequestDto);
			employee.setEmployeeRole(role);
			employee.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			employee.setStatus(StatusEnum.ENABLED.getValue());
			employee.setInstitution(institution);
			if(userDetails!=null) {
				employee.setCreatedBy(Integer.valueOf(userDetails.getId()));
			}
			if(empRepo.existsByInstitutionAndEmployeeNameIgnoreCase(institution,employeeRequestDto.getEmployeeName())) {
				throw new BusinessException(ResponseCode.EMP_EMPLOYEE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
			}
			employee = empRepo.save(employee);
			EmployeeResponseDto employeeResponseDto=employeeMapper.toDto(employee);
			return employeeResponseDto;
		}
	}

	public String changeStatus(@Valid ChangeStatusRequestDto changeStatusRequestDto) {
		Employee employee = empRepo.findById(changeStatusRequestDto.getId())
				.orElseThrow(() -> new BusinessException(ResponseCode.EMP_EMPLOYEE_NOT_FOUND, HttpStatus.NOT_FOUND));
		employee.setStatus(changeStatusRequestDto.getStatus().charAt(0));
		if (makerCheckerEngine.processIfRequired(changeStatusRequestDto, this.getClass().getName(), new Object() {}.getClass().getEnclosingMethod().getName(), "")) {
			return null;
		}
		empRepo.save(employee);

		return "Status changed successfully";
	}

	public List<EmployeeResponseDto> getActiveEmployees() {
		List<Employee> allActiveEmployee = empRepo.findByStatus(StatusEnum.ENABLED.getValue(),
				Sort.by(Sort.Direction.ASC, "employeeId"));
		List<EmployeeResponseDto> allEmployeeResponseDto = new ArrayList<EmployeeResponseDto>();
		allActiveEmployee.stream().forEach((employee) -> {
			EmployeeResponseDto employeeResponseDto = employeeMapper.toDto(employee);
			allEmployeeResponseDto.add(employeeResponseDto);
		});
		return allEmployeeResponseDto;
	}

}
