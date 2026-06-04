package com.mdsl.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAqcuiringTransactionDto {
    Character settlProcessingFlag;
    Character haltMerchPay;
    String processingRefNbr5;
    String microfilmRefNbr;
    String outletCode;
    String terminalId;
    String batchId;
    String linkupCode;
}
