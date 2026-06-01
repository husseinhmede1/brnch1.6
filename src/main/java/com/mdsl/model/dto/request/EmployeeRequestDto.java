package com.mdsl.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDto {
	
	@Size(max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;

    @NotNull(message = ResponseCode.EMP_INVALID_EMPLOYEE_ID)
    private int employeeId;

    @NotNull(message = ResponseCode.EMP_INVALID_EMPLOYEE_NAME)
    @Size(min = 1, max = 50, message = ResponseCode.EMP_INVALID_EMPLOYEE_NAME)
    private String employeeName;

    private int employeeRoleId;

    @Size(max = 255, message = ResponseCode.EMP_INVALID_EMPLOYEE_PHONE)
    private String employeePhone;

    @Size(max = 10, message = ResponseCode.EMP_INVALID_EMPLOYEE_EXT)
    private String employeeExt;
}
