package com.mdsl.swtch.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ACQ_ENTITY")
public class SwitchEntities  implements Cloneable{
	
	@Id
	@Column(name = "ENTITY_ID", nullable = false,columnDefinition = "varchar(30)")
	private String entityId;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "ENTITY_LEVEL")
	private Integer entityLevel;
	
	@NotNull
	@Column(name = "ENTITY_NAME",columnDefinition = "varchar(50)")
	private String entityName;
	
	@Column(name = "ENTITY_DBA_NAME",columnDefinition = "varchar(50)")
	private String entityDbaName;
	
	@Column(name = "ENTITY_ABA_NR",columnDefinition = "varchar(9)")
	private String entityAbaNr;
	
	@Column(name = "ENTITY_STATUS",columnDefinition = "CHAR(1)")
	private Character entityStatus;
	
	@Column(name = "PARENT_ENTITY_ID")
	private String parentEntityId;
	
	@Column(name="ALLW_ID")
	private Integer allwId;
	
	@Column(name = "EXTERNAL_ENTITY_ID")
	private String externalEntityId;
	
	@Column(name = "CAL_ACTIVITY_ID")
	private String calActivityId;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "EXP_START_DATE")
	private Date expStartDate;
	
	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;
	
	@Column(name = "TERMINATION_DATE")
	private Date terminationDate;
	
	@Column(name = "ALW_NON_QUAL_UPGRD")
	private Character alwNonQualUpgrd;
	
	@Column(name = "NON_QUAL_IND")
	private Character nonQualInd;
	
	@Column(name = "ALW_RECLASS_UPGRD")
	private Character alwReclassUpgrd;
	
	@Column(name = "ON_HOLD_IND")
	private Character onHoldInd;
	
	@Column(name = "ON_HOLD_SETTL_PLAN")
	private Integer onHoldSettlPlan;
	
	@Column(name = "HOT_MERCHANT_FLAG",columnDefinition = "CHAR(1)")
	private Character hotMerchantFlag;
	
	@Column(name = "TAX_EXEMPT_IND")
	private Character taxExemptInd;
	
	@Column(name = "RSRV_FUND_STATUS")
	private Character rsrvFundStatus;
	
	@Column(name = "RSRV_TO_BE_MET")
	private Double rsrvToBeMet;
	
	@Column(name = "RSRV_PERCENTAGE")
	private Double rsrvPercentage;
	
	@Column(name="COMPANY_TYPE")
	private String companyType;
	
	@Column(name="VAT_REG_NR")
	private String vatRegNr;
	
	@Column(name="SETTL_AGT_NR")
	private String settlAgtNr;
	
	@Column(name = "TZ_DIFF")
	private Character tzDiff;
	
	@Column(name="PROCESSING_TYPE")
	private String processingType;
	
	@Column(name = "BILLING_TYPE")
	private Character billingType;
	
	@Column(name="TAX_ID")
	private String taxId;
	
	@Column(name = "RECLASS_IND")
	private Character reclassInd;
	
	@Column(name = "LAST_TRANS_CL_DATE")
	private Date lastTransClDate;
	
	@Column(name = "AUTH_BY_LOC_IND")
	private Character authByLocInd;
	
	@Column(name = "DAYS_TO_HOLD")
	private Integer daysToHold;
	
	@Column(name = "PFOLIOSALE_ACQ_BIN")
	private String pfoliosaleAcqBin;
	
	@Column(name = "MERCH_REF_NBR")
	private String merchRefNbr;
	
	@Column(name = "TIN_TYPE")
	private Character tinType;
	
	@Column(name = "OOB_DATE")
	private Date oobDate;
	
	@Column(name = "OWNERSHIP_CHG_DATE")
	private Date ownershipChgDate;
	
	@Column(name = "INFO_REFUSAL_IND")
	private Character infoRefusalInd;
	
	@Column(name = "MPS_ID")
	private Integer mpsId;
	
	@Column(name = "BUSINESS_OWNERTYPE")
	private Character businessOwnerType;
	
	@Column(name = "OTHER_TYPE1")
	private Character otherType1;
	
	@Column(name = "OTHER_TYPE2")
	private Integer otherType2;
	
	@Column(name = "ALT_TAX_ID")
	private String altTaxId;
	
	@Column(name = "DUN_NBR")
	private String dunNbr;
	
	@Column(name = "MERCH_DNN_ID")
	private Integer merchDnnId;
	
	@Column(name = "MERCH_DNN_PREFIX")
	private Integer merchDnnPrefix;
	
	@Column(name = "DNN_INVOICE_NBR")
	private String dnnInvoiceNbr;
	
	@Column(name = "INVOICE_BATCH")
	private Integer invoiceBatch;
	
	@Column(name = "INVOICE_SUB_CD")
	private Integer invoiceSubCd;
	
	@Column(name = "PC_ID")
	private Character pcId;
	
	@Column(name = "CAP_NBR")
	private String capNbr;
	
	@Column(name = "ESTABLISH_ALPHA_ID")
	private String establishAlphaId;
	
	@Column(name = "RECAP_REF_NBR")
	private String recapRefNbr;
	
	@Column(name = "CHARACTER_SET_IND")
	private String characterSetInd;
	
	@Column(name = "LOC_LANG_MERCHDESC")
	private String locLangMerchDesc;
	
	@Column(name = "MERCH_VOL_IND")
	private Character merchVolInd;
	
	@Column(name = "RISK_ID_IND")
	private Character riskIdInd;
	
	@Column(name = "PRESTIGIOUS_PR_IND")
	private Character prestigiousPrInd;
	
	@Column(name = "PROGRAM_REGISTR_ID")
	private String programRegistrId;
	
	@Column(name = "IATA_CD")
	private String iataCd;
	
	@Column(name = "TRAVEL_AGENCY_CD")
	private String travelAgencyCd;
	
	@Column(name = "TRAVEL_AGENCY_NAME")
	private String travelAgencyName;
	
	@Column(name = "MERCH_VERIFI_VALUE")
	private String merchVerifiValue;
	
	@Column(name = "OIL_CO_BRAND_NAME")
	private String oilCoBrandName;
	
	@Column(name = "DEFAULT_MCC")
	private String defaultMcc;
	
	@Column(name = "LANGUAGE_CODE")
	private String languageCode;
	
	@Column(name = "COMPANY_ID")
	private String companyId;
	
	@Column(name = "ACCT_PPLAN_ID")
	private String acctPplanId;
	
	@Column(name = "DEF_POSTING_ENTITY")
	private String defPostingEntity;
	
	@Column(name = "SETUP_STAGE")
	private String setupStage;
	
	@Column(name = "SETUP_COMPLETE_IND")
	private Character setupCompleteInd;
	
	@Column(name = "SELLER_ID")
	private String sellerId;
	
	@Column(name = "DISC_QUAL_IND")
	private Character discQualInd;
	
	@Column(name = "AMMF_ID")
	private Integer ammfId;
	
	@Column(name = "DINERS_INTL_ESTAB_CODE")
	private Integer dinersIntlEstabCode;
	
	@Column(name = "DINERS_CHARGE_TYPE")
	private Integer dinersChargeType;
	
	@Column(name = "VISA_MVV_VPP")
	private String visaMvvVpp;
	
	@Column(name = "VISA_MVV_MSF")
	private String visaMvvMsf;
	
	@Column(name = "VISA_MVV_VDS")
	private String visaMvvVds;
	
	@Column(name = "CED_MAPPING")
	private String cedMapping;
	
	@Column(name = "MER_BUS_PROC_FLG")
	private Character merBusLocFlg;
	
	@Column(name = "SMALL_MERCHANT_IND")
	private String smallMerchantInd;
	
	@Column(name = "MULTI_TRAN_IND")
	private Character multiTranId;
	
	@Column(name = "MULTI_BATCH_FLAG")
	private Character multiBatchFlag;
	
	@Column(name = "ROUTING_DATA")
	private String routingData;

}
