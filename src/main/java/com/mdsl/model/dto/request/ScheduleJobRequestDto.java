package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJobRequestDto {
	@Min(value=1, message=ResponseCode.CFG_INVALID_JOB)
	@Max(value=9999, message=ResponseCode.CFG_INVALID_JOB)
	private Integer jobId; 
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_FREQUENCY)
	@Pattern(regexp="^[0|1|2|3]$", message=ResponseCode.CFG_INVALID_FREQUENCY)
	private String frequency;
	
//	@NotBlank(message = ResponseCode.CFG_INVALID_DATE)
	@Pattern(regexp="^$|(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})", message = ResponseCode.CFG_INVALID_DATE) //yyyy-mm-dd hh:mm:ss
	private String startDateTime;
	
	@Pattern(regexp="^$|(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})", message = ResponseCode.CFG_INVALID_DATE) //yyyy-mm-dd hh:mm:ss
	private String endDateTime;
	
	@Pattern(regexp="^[0|1]$", message=ResponseCode.CFG_INVALID_RECURRING)
	private String repeatTaskFlag;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_REPEAT_TASK_HOUR)
	@Max(value=24, message=ResponseCode.CFG_INVALID_REPEAT_TASK_HOUR)
	private Integer repeatTaskHour; 
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_REPEAT_TASK_MINUTE)
	@Max(value=60, message=ResponseCode.CFG_INVALID_REPEAT_TASK_MINUTE)
	private Integer repeatTaskMinute;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_REPEAT_TASK_SECOND)
	@Max(value=60, message=ResponseCode.CFG_INVALID_REPEAT_TASK_SECOND)
	private Integer repeatTaskSecond;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_MONTH_DAY)
	@Max(value=31, message=ResponseCode.CFG_INVALID_MONTH_DAY)
	private Integer monthDay; 
	
	@Pattern(regexp="^[0|1]$", message=ResponseCode.CFG_INVALID_STOP_TASK_FLAG)
	private String stopTaskFlag;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_STOP_TASK_HOUR)
	@Max(value=24, message=ResponseCode.CFG_INVALID_STOP_TASK_HOUR)
	private Integer stopTaskHour; 
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_STOP_TASK_MINUTE)
	@Max(value=60, message=ResponseCode.CFG_INVALID_STOP_TASK_MINUTE)
	private Integer stopTaskMinute;
	
	private String instId;
	private String remoteAddress;
}