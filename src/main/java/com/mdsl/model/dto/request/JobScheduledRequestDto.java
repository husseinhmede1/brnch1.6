package com.mdsl.model.dto.request;


import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobScheduledRequestDto {
	@NotNull(message=ResponseCode.CFG_INVALID_JOB)
	@Min(value=1, message=ResponseCode.CFG_INVALID_JOB)
	@Max(value=9999, message=ResponseCode.CFG_INVALID_JOB)
	private Integer jobId;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_FREQUENCY)
	@Pattern(regexp="[0|1|2]", message=ResponseCode.CFG_INVALID_FREQUENCY)
	private String frequency;
	
	private Date startDate;
	
	private Date endDate;
	
	@Pattern(regexp = "[0|1]", message = ResponseCode.CFG_INVALID_RECURRING)
	private String recurring;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_RECURRING_FREQUENCY)
	@Max(value=999, message=ResponseCode.CFG_INVALID_RECURRING_FREQUENCY)
	private Integer recurringFreq;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_MONTH_DAY)
	@Max(value=99, message=ResponseCode.CFG_INVALID_MONTH_DAY)
	private Integer monthDay;

	@Min(value=0, message=ResponseCode.CFG_INVALID_MAX_EXEC_TIME)
	@Max(value=999999, message=ResponseCode.CFG_INVALID_MAX_EXEC_TIME)
	private Integer maxExceTime;
}