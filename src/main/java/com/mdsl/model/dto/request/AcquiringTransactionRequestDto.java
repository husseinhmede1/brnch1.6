package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;
import com.sun.istack.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquiringTransactionRequestDto {

	@Min(value=0, message=ResponseCode.CFG_INVALID_ACQUIRER_TRANSACTION_ID)
	@Max(value=99, message=ResponseCode.CFG_INVALID_ACQUIRER_TRANSACTION_ID)
	private Integer acquiringTransactionId;

	@Valid
	private List<Integer> acquiringTransactionIds;

	@Size(min=0, max=10, message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@Pattern(regexp="^[1-9]*[0-9]*$", message=ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;

	@Min(value = 1, message = ResponseCode.CFG_INVALID_TRANSACTION_CURRENCY_ID)
	@Max(value = 9999999999L, message = ResponseCode.CFG_INVALID_TRANSACTION_CURRENCY_ID)
	private Integer transactionCurrencyId; 
	
	@Size(min = 1, max = 30, message=ResponseCode.CFG_INVALID_ENTITY_ID)
	@Pattern(regexp="^[a-zA-Z0-9*]+$", message=ResponseCode.CFG_INVALID_ENTITY_ID)
	private String entitiesId;

	@Size(min = 1, max = 50, message=ResponseCode.CFG_INVALID_ENTITY_NAME)
	@Pattern(regexp="^[a-zA-Z0-9*]+$", message=ResponseCode.CFG_INVALID_ENTITY_NAME)
	private String entityName;

	@Size(min = 1, max = 12, message=ResponseCode.CFG_INVALID_TERMINAL)
	@Pattern(regexp="^[a-zA-Z0-9*]+$", message=ResponseCode.CFG_INVALID_TERMINAL)
	private String terminalId;

	@Nullable
	@Size(max = 12, message = ResponseCode.CFG_INVALID_BATCH)
	@Pattern(regexp = "^[0-9*]+$", message = ResponseCode.CFG_INVALID_BATCH)
	private String batchId;

	@Size(max = 6, message = ResponseCode.CFG_INVALID_AUTHORIZATION_NUMBER)
	@Pattern(regexp = "^[A-Za-z0-9]*$", message = ResponseCode.CFG_INVALID_AUTHORIZATION_NUMBER)
	private String authorizationNumber;

	@Pattern(regexp = "^[YN]$", message = ResponseCode.CFG_INVALID_MANUAL_ENTRY)
	@Size(max = 1, message = ResponseCode.CFG_INVALID_MANUAL_ENTRY)
	private String manualEntry;

	@PastOrPresent(message = ResponseCode.INVALID_PROCESSING_DATE)
	private Date processingDate;

	@Size(max = 255, message = ResponseCode.INVALID_CARD_NUMBER)
	@Pattern(regexp = "^$|^[0-9*]{0,19}$", message = ResponseCode.INVALID_CARD_NUMBER)
	private String cardNumber;

	@Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = ResponseCode.MRC_INVALID_MERCHANT_NAME)
	@Size(min = 0, max = 50, message = ResponseCode.MRC_INVALID_MERCHANT_NAME)
	private String merchantName;

	@Size(max = 12, message = ResponseCode.CFG_INVALID_TRANSACTION_ID)
	@Pattern(regexp = "^$|^[A-Za-z0-9_-]{1,12}$", message = ResponseCode.CFG_INVALID_TRANSACTION_ID)
	private String transactionId;

	@PastOrPresent(message = ResponseCode.INVALID_TRANSACTION_DATE)
	private Date transactionDate;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.INVALID_TRANSACTION_AMOUNT)
	@DecimalMin(value = "0.00", inclusive = false, message = ResponseCode.INVALID_TRANSACTION_AMOUNT)
	@DecimalMax(value = "999999999999999999.99999999", message = ResponseCode.INVALID_TRANSACTION_AMOUNT)
	private Float transactionAmount;

	private String authorizationCode;

	private Float settlementAmount;

	private String microfilmRefNbr;

	private Integer refNumberSequence;

	@Size(min = 0, max = 19, message = ResponseCode.CFG_INVALID_PAN)
	@Size(max = 19, message = ResponseCode.INVALID_PAN_NUMBER)
	@Pattern(regexp = "^[0-9*]+$", message = ResponseCode.INVALID_PAN_NUMBER)
	private String pan;

	private String cardSchemeId;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_LINKUPCODE)
	@Size(max = 30, message = ResponseCode.CFG_INVALID_LINKUP_CODE)
	@Pattern(regexp = "^[A-Za-z0-9_\\-]*$", message = ResponseCode.CFG_INVALID_LINKUP_CODE)
	private String linkupCode;

	@Digits(integer = 10, fraction = 0, message = ResponseCode.CFG_INVALID_PROCESSING_CODE)
	@Positive(message = ResponseCode.CFG_INVALID_PROCESSING_CODE)
	private Integer processingCode;

	private Character reversal;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.CFG_INVALID_BILLING_AMOUNT)
	@PositiveOrZero(message = ResponseCode.CFG_INVALID_BILLING_AMOUNT)
	private Float billingAmount;

	private Integer billingCurrencyId;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.CFG_INVALID_LOCAL_AMOUNT)
	@PositiveOrZero(message = ResponseCode.CFG_INVALID_LOCAL_AMOUNT)
	private Float localAmount;

	private Integer localCurrencyId;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.CFG_INVALID_TIPS_AMOUNT)
	@PositiveOrZero(message = ResponseCode.CFG_INVALID_TIPS_AMOUNT)
	private Float tipsAmount;

	private Integer tipsCurrencyId;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.CFG_INVALID_DCC_AMOUNT)
	@PositiveOrZero(message = ResponseCode.CFG_INVALID_DCC_AMOUNT)
	private Float dccAmount;

	private Integer dccCurrencyId;

	@Digits(integer = 18, fraction = 8, message = ResponseCode.CFG_INVALID_DCCMERCHANT_AMOUNT)
	@PositiveOrZero(message = ResponseCode.CFG_INVALID_DCCMERCHANT_AMOUNT)
	private Float dccMerchantAmount;

	private Float dccMerchantSettlAmount;

	private Integer dccMerchantSettlAmountCurrencyId;

	private Float merchantCommisionNumber;

	private Float merchantMarkUpNumber;

	private Float chMarkUpNumber;

	private Float feeAmount1;

	private Integer feeAmount1CurrencyId;

	private Float feeAmount2;

	private Integer feeAmount2CurrencyId;

	private Date transactionTime;

	@Size(min = 0, max = 19, message = ResponseCode.ACQ_INVALID_MASK_PAN)
	private String maskPan;

	private Character billingProcessingFlag;

	private Character settlProcessingFlag;

	private Integer settlProcessingNbr;

	private Character settlMerchHalt;

	@Size(min = 0, max = 3, message = ResponseCode.CFG_INVALID_MERCHANTCOUNTRY)
	private String merchantCountry;

	@Size(min = 0, max = 12, message = ResponseCode.ACQ_INVALID_ACQUIRER_ID)
	private String acquirerId;

	private Integer issuerId;

	@Size(min = 0, max = 20, message = ResponseCode.CFG_INVALID_ISSUERREFNUM)
	private String issuerRefNum;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_OUTLETCODE)
	private String outletCode;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_MERCHANT_ACCOUNT_NUMBER)
	private String merchantAccountNumber;

	private Integer merchantAccountCurrId;

	@Size(min = 0, max = 4, message = ResponseCode.CFG_INVALID_MERCHANTCATEGORY)
	private String merchantCategory;

	@Size(min = 0, max = 50, message = ResponseCode.CFG_INVALID_TERMINALLOCATION)
	private String terminalLocation;

	@Size(min = 0, max = 128, message = ResponseCode.CFG_INVALID_ACQUIRERDATA)
	private String acquirerData;

	@Size(min = 0, max = 128, message = ResponseCode.CFG_INVALID_ISSUERDATA)
	private String issuerData;

	@Size(min = 0, max = 128, message = ResponseCode.CFG_INVALID_TERMINALDATA)
	private String terminalData;

	private Character eCommerceFlag;

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_ORIGINNETWORK)
	private String originNetwork;

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_DESTINATIONNETWORK)
	private String destinationNetwork;

	private Date merchantSettlDate;

	private Integer settlementCurrencyId;

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_REVERSALREASON)
	private String reversalReason;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_REVERSALCOMMENT)
	private String reversalComment;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_MANNUALCOMMENT)
	private String manualComment;

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_CHERGEBACKREASON)
	private String chergebackReason;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_CHERGEBACKCOMMENT)
	private String chargebackComment;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_PAYHALTCOMMENT)
	private String payHaltComment;

	private Character accquierRecordToAppear;

	private Character issueRecordToAppear;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_PROCESSING_REF_NBR1)
	private String processingRefNbr1;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_PROCESSING_REF_NBR2)
	private String processingRefNbr2;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_PROCESSING_REF_NBR3)
	private String processingRefNbr3;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_PROCESSING_REF_NBR4)
	private String processingRefNbr4;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_PROCESSING_REF_NBR5)
	private String processingRefNbr5;

	private String representmentReason;

	private String representmentComment;

	private Character toBePaidToMerchant;

	@Max(value = 99, message = ResponseCode.CFG_INVALID_USAGE_CODE)
	private Integer usageCode;

	private String payHaltStatus;

	private Character confirmStoppingPayment;

	private List<SortDTO> sort;

	private String sortOrder = "";

	private Integer pageNo = 0;

	private Integer pageSize = 20;

	private Integer systemCodeId;

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_MERCHANTBANK)
	private String merchantBank;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_MERCHANTIBAN)
	private String merchantIban;

	private Date fromProcessingDate;

	private Date toProcessingDate;

	public List<SortDTO> getSort() {
		if (this.sort == null || this.sort.isEmpty()) {
			return new ArrayList<SortDTO>();
		}
		return this.sort;
	}

	public void setSort(List<SortDTO> sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTransactionDate(String transactionDate) {
		if (transactionDate == null || transactionDate.isEmpty()) {
			this.transactionDate = null;
		} else {
			try {
				this.transactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setProcessingDate(String processingDate) {
		if (processingDate == null || processingDate.isEmpty()) {
			this.processingDate = null;
		} else {
			try {
				this.processingDate = new SimpleDateFormat("dd/MM/yyyy").parse(processingDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFromProcessingDate(String fromProcessingDate) {
		if (fromProcessingDate == null || fromProcessingDate.isEmpty()) {
			this.fromProcessingDate = null;
		} else {
			try {
				this.fromProcessingDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromProcessingDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setToProcessingDate(String toProcessingDate) {
		if (toProcessingDate == null || toProcessingDate.isEmpty()) {
			this.toProcessingDate = null;
		} else {
			try {
				this.toProcessingDate = new SimpleDateFormat("dd/MM/yyyy").parse(toProcessingDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTransactionTime(String transactionTime) {
		if (transactionTime == null || transactionTime.isEmpty()) {
			this.transactionTime = null;
		} else {
			try {
				this.transactionTime = new SimpleDateFormat("dd/MM/yyyy").parse(transactionTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void setMerchantSettlDate(String merchantSettlDate) {
		if (merchantSettlDate == null || merchantSettlDate.isEmpty()) {
			this.merchantSettlDate = null;
		} else {
			try {
				this.merchantSettlDate = new SimpleDateFormat("dd/MM/yyyy").parse(merchantSettlDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
