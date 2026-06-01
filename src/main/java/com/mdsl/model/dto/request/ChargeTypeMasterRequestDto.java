package com.mdsl.model.dto.request;

import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeTypeMasterRequestDto {

	private int chargeTypeMasterId;
	
	@Size(min=1,max=50,message=ResponseCode.CFG_INVALID_CHARGE_TYPE_MASTER_NAME)
	private String chargeTypeMasterName;
}
