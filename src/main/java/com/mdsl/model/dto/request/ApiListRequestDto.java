package com.mdsl.model.dto.request;

import com.mdsl.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiListRequestDto {
	@NotNull(message=ResponseCode.CFG_INVALID_API_LIST)
	@Min(value=0, message=ResponseCode.CFG_INVALID_API_LIST)
	@Max(value=99999999, message=ResponseCode.CFG_INVALID_API_LIST)
	private Integer apiId;
	
	@NotBlank(message=ResponseCode.CFG_INVALID_API_LIST_STP) 
	@Size(min=0, max=1, message = ResponseCode.CFG_INVALID_API_LIST_STP)
	@Pattern(regexp="[0|1]", message=ResponseCode.CFG_INVALID_API_LIST_STP)
	private String stp;
}
