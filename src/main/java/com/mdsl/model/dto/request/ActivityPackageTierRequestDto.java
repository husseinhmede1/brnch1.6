package com.mdsl.model.dto.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

import com.mdsl.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ActivityPackageTierRequestDto {

	@Min(value = 1, message = ResponseCode.INVALID_ACT_PKG_TIER_ID)
	private int activityPackageTierId;

	@PositiveOrZero(message = ResponseCode.CFG_INVALID_ACT_PKG_DETAILS_ID)
	private int activityPackageDetailId;

	@Min(value = 1, message = ResponseCode.INVALID_FREQUENCY_ID)
	private int frequencyId;

	@DecimalMin(value = "0.000", inclusive = false, message = ResponseCode.CFG_INVALID_PERCENTAGE_AMOUNT)
	@Digits(integer = 3, fraction = 3, message = ResponseCode.CFG_INVALID_PERCENTAGE_AMOUNT)
	private float percentageAmount;

	@DecimalMin(value = "0.000", inclusive = false, message = ResponseCode.CFG_INVALID_FIX_AMOUNT)
	@Digits(integer = 12, fraction = 3, message = ResponseCode.CFG_INVALID_FIX_AMOUNT)
	private float fixAmount;

	@DecimalMin(value = "0.000", inclusive = false, message = ResponseCode.CFG_INVALID_START_AMOUNT)
	@Digits(integer = 12, fraction = 3, message = ResponseCode.CFG_INVALID_START_AMOUNT)
	private float startAmount;

	@DecimalMin(value = "0.000", inclusive = false, message = ResponseCode.CFG_INVALID_UPTO_AMOUNT)
	@Digits(integer = 12, fraction = 3, message = ResponseCode.CFG_INVALID_UPTO_AMOUNT)
	private float uptoAmount;
}
