package com.mdsl.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_BULK_HOLD")
public class BulkHold {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_ID")
    @SequenceGenerator(name = "ACCOUNT_ID", sequenceName = "MD_BULK_HOLD_SEQ", allocationSize = 1)
    @Column(name = "RECORD_SEQ_ID")
    private Integer recordSeqId;

    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "INSTITUTION_ID")
    private String institutionId;

    @Column(name = "OUTLET_CODE")
    private String outletCode;

    @Column(name = "TERMINAL_ID")
    private String terminalId;

    @Column(name = "MERCHANT_NAME")
    private String merchantName;

    @Column(name = "MERCHANT_COUNTRY")
    private String merchantCountry;

    @Column(name = "TRANS_ID")
    private String transId;

    @Column(name = "SOURCE_AMOUNT")
    private Integer sourceAmount;

    @Column(name = "SOURCE_CURRENCY", length = 3)
    private String sourceCurrency;

    @Column(name = "TRANSACTION_DATE")
    private Date transactionDate;

    @Column(name = "AUTHORIZATION_NUMBER")
    private String authorizationNumber;

    @Column(name = "MICROFILM_REF_NBR")
    private String microfilmRefNbr;

    @Column(name = "HOLD_TYPE")
    private String holdType;

    @Column(name = "PROCESSING_FLAG")
    private String processingFlag;

    @Column(name = "PROCESSING_DATE")
    private Date processingDate;

    @Column(name = "CREATED_DATE")
    private Date createdDate;
}
