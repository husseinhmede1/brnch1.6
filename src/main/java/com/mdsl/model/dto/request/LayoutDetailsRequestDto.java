package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutDetailsRequestDto {
	@Min(value=0, message= ResponseCode.CFG_INVALID_DETAILS_ID)
	@Max(value=999999999, message= ResponseCode.CFG_INVALID_DETAILS_ID)
	private int detailsId;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_DETAILS_FLAG)
	@Pattern(regexp="[A|U|D]", message = ResponseCode.CFG_INVALID_DETAILS_FLAG)
	private String detailsFlag;
	
	@Min(value=0, message= ResponseCode.CFG_INVALID_ELEMENT_ID)
	@Max(value=99999, message= ResponseCode.CFG_INVALID_ELEMENT_ID)
	private int elementId;
	
	@Min(value=0, message= ResponseCode.CFG_INVALID_ELEMENT_LENGTH)
	@Max(value=999, message= ResponseCode.CFG_INVALID_ELEMENT_LENGTH)
	private int elementLength;
	
	@Size(min=0, max=1, message=ResponseCode.CFG_INVALID_ELEMENT_PADDING_TYPE)
	@Pattern(regexp="[1|2|3]?", message=ResponseCode.CFG_INVALID_ELEMENT_PADDING_TYPE)
	private String paddingType;
	
	@Size(min=0, max=100, message = ResponseCode.CFG_INVALID_ELEMENT_PADDING_VALUE)
	@Pattern(regexp="^[a-zA-Z0-9 -//]*$", message = ResponseCode.CFG_INVALID_ELEMENT_PADDING_VALUE)
	private String paddingValue;
	
	@NotEmpty(message= ResponseCode.CFG_INVALID_ELEMENT_SECTION)
	@Size(min=1, max=1, message = ResponseCode.CFG_INVALID_ELEMENT_SECTION)
	@Pattern(regexp="[H|B|F]", message = ResponseCode.CFG_INVALID_ELEMENT_SECTION)
	private String elementSection;
	
	@Min(value=1, message = ResponseCode.CFG_INVALID_ELEMENT_ORDER)
	private int elementOrder;
	
	@Min(value=0, message= ResponseCode.CFG_INVALID_ELEMENT_ID)
	@Max(value=9999, message= ResponseCode.CFG_INVALID_ELEMENT_ID)
	private Integer elementParentId;
}