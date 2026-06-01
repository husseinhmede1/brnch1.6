package com.mdsl.model.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcquiringTransactionAdjustmentRequestDto {
	private List<Integer> transactions;
	private boolean isHold;
}
