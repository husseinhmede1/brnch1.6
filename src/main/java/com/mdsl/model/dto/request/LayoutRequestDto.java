package com.mdsl.model.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.*;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutRequestDto {
	@Min(value=0, message= ResponseCode.CFG_INVALID_LAYOUT_ID)
	@Max(value=9999, message= ResponseCode.CFG_INVALID_LAYOUT_ID)
	private int layoutId;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_LAYOUT_NAME)
	@Size(min=1, max=100, message = ResponseCode.CFG_INVALID_LAYOUT_NAME)
	@Pattern(regexp="^[a-zA-Z -]*$" , message = ResponseCode.CFG_INVALID_LAYOUT_NAME)
	private String layoutName;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_LAYOUT_FORMAT)
	@Size(min=1, max=1, message = ResponseCode.CFG_INVALID_LAYOUT_FORMAT)
	@Pattern(regexp="^[1|2|3|4|5]$" , message = ResponseCode.CFG_INVALID_LAYOUT_FORMAT)
	private String layoutFormat;
	
	@Size(min=0, max=1, message = ResponseCode.CFG_INVALID_LAYOUT_SEPARATOR)
	@Pattern(regexp="(^$|[,#$%^\\|~])", message = ResponseCode.CFG_INVALID_LAYOUT_SEPARATOR)
	private String layoutSeparator;

	@NotNull
	private boolean includesHeader;
	
	@Min(value=0, message= ResponseCode.CFG_INVALID_FILE)
	@Max(value=9999, message= ResponseCode.CFG_INVALID_FILE)
	private int fileId;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_STATUS)
	@Size(min=0, max=1, message = ResponseCode.CFG_INVALID_STATUS)
	@Pattern(regexp="[0|1]", message=ResponseCode.CFG_INVALID_STATUS)
	private String status;
	
	private String instId;
	private String remoteAddress;
	
	@Valid
	private List<LayoutDetailsRequestDto> listLayoutDetailsRequest;
}