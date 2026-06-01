package com.mdsl.model.dto.request;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;
import lombok.Data;

@Data
public class RepresentmentRequestDto {

	@Size(max=10, message= ResponseCode.CFG_INVALID_REPRESENTMENT_ID)
	private int rePresentmentId;

	private String rePresentmentReason;
}