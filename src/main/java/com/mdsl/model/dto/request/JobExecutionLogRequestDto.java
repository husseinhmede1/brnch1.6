package com.mdsl.model.dto.request;


import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionLogRequestDto {

	@Min(value=0, message=ResponseCode.CFG_INVALID_JOB)
	@Max(value=9999, message=ResponseCode.CFG_INVALID_JOB)
	private Integer jobId;
	
	@Pattern(regexp="^$||(\\d{4}-\\d{2}-\\d{2})", message = ResponseCode.CFG_INVALID_DATE)
	private String fromDate;
	
	@Pattern(regexp="^$||(\\d{4}-\\d{2}-\\d{2})", message = ResponseCode.CFG_INVALID_DATE)
	private String toDate;
	
	@Valid
	private SpecificSortPaginationRequestDto paginationRequestDto;
}
