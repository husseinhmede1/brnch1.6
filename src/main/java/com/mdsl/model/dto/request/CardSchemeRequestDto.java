package com.mdsl.model.dto.request;



import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardSchemeRequestDto {
	
	@NotNull(message=ResponseCode.CFG_INVALID_CARDSCHEME)
	@Min(value = 0, message = ResponseCode.CFG_INVALID_CARDSCHEME)
	@Max(value = 999999999, message = ResponseCode.CFG_INVALID_CARDSCHEME)
	private Integer recordSequenceNumber;

	@NotEmpty(message=ResponseCode.CFG_INVALID_CARDSCHEME_ID)
	@Size(max=6, message = ResponseCode.CFG_INVALID_CARDSCHEME_ID)
	private String cardSchemeId;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_CARDSCHEME_NAME)
	@NotBlank(message = ResponseCode.CFG_INVALID_CARDSCHEME_NAME)
	@Size(max=50, message = ResponseCode.CFG_INVALID_CARDSCHEME_NAME)
	private String cardSchemeName;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_CARDSCHEME_SPEC)
	@Size(max=10, message = ResponseCode.CFG_INVALID_CARDSCHEME_SPEC)
	private String cardSchemeSpecific;
	
	@NotBlank(message = ResponseCode.CFG_INVALID_CARDSCHEME_STATUS)
	@Size(min = 0, max = 1, message = ResponseCode.CFG_INVALID_CARDSCHEME_STATUS)
	@Pattern(regexp = "[0-1]", message = ResponseCode.CFG_INVALID_CARDSCHEME_STATUS)
	private String status;
	
}
