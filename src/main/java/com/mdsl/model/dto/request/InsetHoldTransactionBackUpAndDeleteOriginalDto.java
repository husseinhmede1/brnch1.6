package com.mdsl.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsetHoldTransactionBackUpAndDeleteOriginalDto {
    String microfilmRefNbr;
    String entityId;
    String terminalId;
    String batchId;
    String linkupCode;
    String processingRefNbr5;
    Character settleProcessionFlag;
    Character haltMerchPay;
}
