package com.mdsl.model.dto.request;

import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessingEventsRequestDto {
	
	@NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
    private String institutionId;
	
	@NotNull(message=ResponseCode.TSK_INVALID_TASK_ID)
	@Min(value = 0, message = ResponseCode.TSK_INVALID_TASK_ID)
	@Max(value = 999999999, message = ResponseCode.TSK_INVALID_TASK_ID)
    private Integer taskId;
    
    private Map<Integer, String> taskParameters;

}
