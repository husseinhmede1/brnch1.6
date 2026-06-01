package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BkdServiceRequestDto {
	@NotNull(message = ResponseCode.CFG_INVALID_SERVICE_ID)
	@Min(value=0, message=ResponseCode.CFG_INVALID_SERVICE_ID)
	@Max(value=99999, message=ResponseCode.CFG_INVALID_SERVICE_ID)
	private Integer serviceId;
	
	@NotNull(message = ResponseCode.CFG_INVALID_BATCH_SIZE)
	@Min(value=1, message=ResponseCode.CFG_INVALID_BATCH_SIZE)
	@Max(value=999999999, message=ResponseCode.CFG_INVALID_BATCH_SIZE)
	private Integer batchSize;
}
