package com.mdsl.model.dto.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcquiringTransactionResponseDto {

	private int acquiringTransactionId;
	private String institutionId;
	private String institutionName;
	private Integer transactionCurrencyId;
	private String transactionCurrencyCode;
	private String transactionCurrencyName;
	private String entitiesId;
	private String entityName;
	private int terminalId;
	private Character manualEntry;
	private String processingDate;
	private String cardNumber;
	private String merchantName;
	private String transactionId;
	private String transactionDescription;
	private String transactionDate;
	private Float transactionAmount;
	private String authorizationCode;
	private Float settlementAmount;
	private String microfilmRefNbr;
	private Integer refNumberSequence;
	private String pan;
	private String cardSchemeId;
	private String cardSchemeName;
	private String linkupCode;
	private Integer processingCode;
	private Character reversal;
	private Float billingAmount;
	private Integer billingCurrencyId;
	private String billingCurrencyCode;
	private String billingCurrencyName;
	private Float localAmount;
	private Integer localCurrencyId;
	private String localCurrencyCode;
	private String localCurrencyName;
	private Float tipsAmount;
	private Integer tipsCurrencyId;
	private String tipsCurrencyCode;
	private String tipsCurrencyName;
	private Float dccAmount;
	private Integer dccCurrencyId;
	private String dccCurrencyCode;
	private String dccCurrencyName;
	private Float dccMerchantAmount;
	private Float dccMerchantSettlAmount;
	private Integer dccMerchantSettlAmountCurrencyId;
	private String dccMerchantSettlAmountCurrencyCode;
	private String dccMerchantSettlAmountCurrencyName;
	private Float merchantCommisionNumber;
	private Float merchantMarkUpNumber;
	private Float chMarkUpNumber;
	private Float feeAmount1;

	private Integer feeAmount1CurrencyId;

	private String feeAmount1CurrencyCode;

	private String feeAmount1CurrencyName;

	private Float feeAmount2;

	private Integer feeAmount2CurrencyId;

	private String feeAmount2CurrencyCode;

	private String feeAmount2CurrencyName;

	private String transactionTime;

	private String maskPan;

	private Character billingProcessingFlag;

	private Character settlProcessingFlag;

	private Integer settlProcessingNbr;

	private Character settlMerchHalt;

	private String merchantCountry;

	private String acquirerId;

	private String issuerId;

	private String issuerRefNum;

	private String outletCode;

	private String merchantAccountNumber;

	private Integer merchantAccountCurrId;

	private String merchantAccountCurrCode;

	private String merchantAccountCurrName;

	private String merchantCategory;

	private String terminalLocation;

	private String acquirerData;

	private String issuerData;

	private String terminalData;

	private Character eCommerceFlag;

	private String originNetwork;

	private String destinationNetwork;

	private String merchantSettlDate;
	
	private String merchantPaymentDate;

	private Integer settlementCurrencyId;

	private String settlementCurrencyCode;

	private String settlementCurrencyName;

	private String reversalReason;

	private String reversalComment;

	private String manualComment;

	private String chergebackReason;

	private String chargebackComment;

	private String payHaltComment;

	private Character accquierRecordToAppear;

	private Character issueRecordToAppear;

	private String processingRefNbr1;

	private String processingRefNbr2;

	private String processingRefNbr3;

	private String processingRefNbr4;

	private String processingRefNbr5;

	private String representmentReason;

	private String representmentComment;

	private Character toBePaidToMerchant;

	private Integer usageCode;

	private String payHaltStatus;

	private Character confirmStoppingPayment;

	private Integer pageNo;

	private Integer pageSize;

	private Integer systemCodeId;

	private String codeSuffix;

	private String codePrefix;

	private Character codePattern;

	private String codeValue;

	private String codeDescription;

	private String merchantBank;

	private String merchantIban;

	public void setMerchantPaymentDate(Date merchantPaymentDate) {
		if (merchantPaymentDate != null) {
			Date date = merchantPaymentDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.merchantPaymentDate = strDate;
		} else {
			this.merchantPaymentDate = "";
		}
		
	}


	public void setManualEntry(Character manualEntry) {
		if (manualEntry == "1".charAt(0)) {
			this.manualEntry = "Y".charAt(0);
		} else if (manualEntry == "0".charAt(0)) {
			this.manualEntry = "N".charAt(0);
		}
	}
	public void setReversal(Character reversal) {
		if (reversal == "1".charAt(0)) {
			this.reversal = "Y".charAt(0);
		} else if (reversal == "0".charAt(0)) {
			this.reversal = "N".charAt(0);
		}
	}

	public void setBillingProcessingFlag(Character billingProcessingFlag) {

		if (null != billingProcessingFlag) {
			if (billingProcessingFlag == "1".charAt(0)) {
				this.billingProcessingFlag = "Y".charAt(0);
			} else if (billingProcessingFlag == "0".charAt(0)) {
				this.billingProcessingFlag = "N".charAt(0);
			}
		}
	}

	public void setSettlProcessingFlag(Character settlProcessingFlag) {

		if (null != settlProcessingFlag) {
			if (settlProcessingFlag == "1".charAt(0)) {
				this.settlProcessingFlag = "Y".charAt(0);
			} else if (settlProcessingFlag == "0".charAt(0)) {
				this.settlProcessingFlag = "N".charAt(0);
			}
		}

	}
	public void setSettlMerchHalt(Character settlMerchHalt) {

		if (null != settlMerchHalt) {
			if (settlMerchHalt == "1".charAt(0)) {
				this.settlMerchHalt = "Y".charAt(0);
			} else if (settlMerchHalt == "0".charAt(0)) {
				this.settlMerchHalt = "N".charAt(0);
			}
		}
	}
	public void setECommerceFlag(Character eCommerceFlag) {

		if (null != eCommerceFlag) {
			if (eCommerceFlag == "1".charAt(0)) {
				this.eCommerceFlag = "Y".charAt(0);
			} else if (eCommerceFlag == "0".charAt(0)) {
				this.eCommerceFlag = "N".charAt(0);
			}
		}

	}

	public void setAccquierRecordToAppear(Character accquierRecordToAppear) {

		if (null != accquierRecordToAppear) {
			if (accquierRecordToAppear == "1".charAt(0)) {
				this.accquierRecordToAppear = "Y".charAt(0);
			} else if (accquierRecordToAppear == "0".charAt(0)) {
				this.accquierRecordToAppear = "N".charAt(0);
			} else {

			}
		}

	}

	public Character getIssueRecordToAppear() {
		return issueRecordToAppear;
	}

	public void setIssueRecordToAppear(Character issueRecordToAppear) {
		
		if(null != issueRecordToAppear) {
			if (issueRecordToAppear == "1".charAt(0)) {
				this.issueRecordToAppear = "Y".charAt(0);
			} else if (issueRecordToAppear == "0".charAt(0)) {
				this.issueRecordToAppear = "N".charAt(0);
			}
		}
		
	}
	public void setToBePaidToMerchant(Character toBePaidToMerchant) {
		
		if(null != toBePaidToMerchant) {
			if (toBePaidToMerchant == "1".charAt(0)) {
				this.toBePaidToMerchant = "Y".charAt(0);
			} else if (toBePaidToMerchant == "0".charAt(0)) {
				this.toBePaidToMerchant = "N".charAt(0);
			}
		}
		
	}
	public void setConfirmStoppingPayment(Character confirmStoppingPayment) {
		if(confirmStoppingPayment != null) {
			if (confirmStoppingPayment == "1".charAt(0)) {
				this.confirmStoppingPayment = "Y".charAt(0);
			} else if (confirmStoppingPayment == "0".charAt(0)) {
				this.confirmStoppingPayment = "N".charAt(0);
			}else if (confirmStoppingPayment == "Y".charAt(0)) {
				this.confirmStoppingPayment = "Y".charAt(0);
			}else if (confirmStoppingPayment == "N".charAt(0)) {
				this.confirmStoppingPayment = "N".charAt(0);
			}
		}
		
	}
	public void setProcessingDate(Date processingDate) {
		if (processingDate != null) {
			Date date = processingDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.processingDate = strDate;
		} else {
			this.processingDate = "";
		}
	}

	public void setTransactionDate(Date transactionDate) {
		if (transactionDate != null) {
			Date date = transactionDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.transactionDate = strDate;
		} else {
			this.transactionDate = "";
		}
	}
	public void setTransactionTime(Date transactionTime) {
		if (transactionTime != null) {
			Date date = transactionTime;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.transactionTime = strDate;
		} else {
			this.transactionTime = "";
		}
	}
	public void setMerchantSettlDate(Date merchantSettlDate) {
		if (merchantSettlDate != null) {
			Date date = merchantSettlDate;
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = formatter.format(date);
			this.merchantSettlDate = strDate;
		} else {
			this.merchantSettlDate = "";
		}
	}
}
