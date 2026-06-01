package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.EmployeeRequestDto;
import com.mdsl.model.dto.response.EmployeeResponseDto;
import com.mdsl.model.entity.Employee;

@Mapper
public interface EmployeeMapper {
	@Mapping(source = "employeeRole.systemCodeId", target = "employeeRoleSystemCodeId")
	@Mapping(source = "employeeRole.codeSuffix", target = "employeeRoleCodeSuffix")
	@Mapping(source = "employeeRole.codePrefix", target = "employeeRoleCodePrefix")
	@Mapping(source = "employeeRole.codeValue", target = "employeeRoleCodeValue")
	@Mapping(source = "employeeRole.description", target = "employeeRoleCodeDescription")
	EmployeeResponseDto toDto(Employee employee);
	
	Employee toEntity(EmployeeRequestDto employeeRequestDto );
}
