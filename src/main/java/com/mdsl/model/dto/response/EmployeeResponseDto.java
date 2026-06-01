package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
	private int employeeId;
	private String employeeName;
	private String employeePhone;
	private String employeeExt;
	private String status;
	private Integer employeeRoleSystemCodeId;
	private String employeeRoleCodeSuffix;
	private String employeeRoleCodePrefix;
	private String employeeRoleCodeValue;
	private String employeeRoleCodeDescription;
}
