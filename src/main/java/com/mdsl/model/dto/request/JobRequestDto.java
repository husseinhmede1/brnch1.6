package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDto {
	@NotNull
	@Min(value=0, message=ResponseCode.CFG_INVALID_JOB)
	@Max(value=9999, message=ResponseCode.CFG_INVALID_JOB)
	private Integer jobId;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_JOB_NAME)
	@Size(min=1, max=100, message=ResponseCode.CFG_INVALID_JOB_NAME)
	@Pattern(regexp="^[a-zA-Z0-9 -_,]*$", message=ResponseCode.CFG_INVALID_JOB_NAME)
	private String jobName;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_JOB_DESCRIPTION)
	@Size(min=1, max=500, message=ResponseCode.CFG_INVALID_JOB_DESCRIPTION)
	@Pattern(regexp="^[a-zA-Z0-9 -_,]*$", message=ResponseCode.CFG_INVALID_JOB_DESCRIPTION)
	private String jobDescription;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_ALERT_SUCCESS)
	@Pattern(regexp = "[0|1]", message = ResponseCode.CFG_INVALID_ALERT_SUCCESS)
	private String alertSuccess;
	
	@Pattern(regexp = "^$||^([a-z][a-z0-9_.]+@([a-z0-9-]+\\.)+[a-z]{2,6}(;)*)+$", message = ResponseCode.CFG_INVALID_EMAIL_SUCCESS)
	@Size(min=0, max=500, message=ResponseCode.CFG_INVALID_EMAIL_SUCCESS)
	private String successEmail;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_ALERT_FAILURE)
	@Pattern(regexp = "[0|1]", message = ResponseCode.CFG_INVALID_ALERT_FAILURE)
	private String alertFailure;
	
	@Pattern(regexp = "^$||^([a-z][a-z0-9_.]+@([a-z0-9-]+\\.)+[a-z]{2,6}(;)*)+$", message = ResponseCode.CFG_INVALID_EMAIL_FAILURE)
	@Size(min=0, max=500, message=ResponseCode.CFG_INVALID_EMAIL_FAILURE)
	private String failEmail;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_ENABLED)
	@Pattern(regexp = "[0|1]", message = ResponseCode.CFG_INVALID_ENABLED)
	private String status;
	
	private String instId;
	private String remoteAddress;
	
	@Valid
	private List<JobDefinitionTaskRequestDto> jobDefinitionTask;
}