package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManualMerchantTransactionRequestDto {

	@NotNull(message = ResponseCode.INVALID_MERCHANT_TRANSACTION_ID)
	@Min(value = 0, message = ResponseCode.INVALID_MERCHANT_TRANSACTION_ID)
	@Max(value = 999999999, message = ResponseCode.INVALID_MERCHANT_TRANSACTION_ID)
	private int merchantTransactionId;

	@NotNull(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;

	@Size(min = 1, max = 19, message = ResponseCode.INVALID_PAN)
	private String pan;

	@Max(value=9999, message = ResponseCode.INVALID_CARD_NBR)
	private int cardSeqNbr;

	@Size(min=0, max=19, message=ResponseCode.INVALID_CARD_NBR)
	@Pattern(regexp = "^[a-zA-Z0-9*]*$", message = ResponseCode.INVALID_CARD_NBR)
	@NotNull(message = ResponseCode.INVALID_CARD_NBR)
	private String cardNumber;

//	@NotNull(message = ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION_ID)
	@NotEmpty(message = ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION)
	@NotNull(message = ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION)
	private String transactionId;

	@NotEmpty(message = ResponseCode.INVALID_REVERSAL_FLAG)
	private String reversalFlag;

	private int systemCodeId;

	private String comments;

	@NotNull(message = ResponseCode.INVALID_ENTITY_ID)
	private String outletId;

	@NotNull(message = ResponseCode.CFG_INVALID_TERMINAL_ID)
	private String terminalId;

	@NotNull(message = ResponseCode.INVALID_AMOUNT)
	private float transactionAmount;

	@NotNull(message = ResponseCode.CFG_INVALID_CURRENCY)
	private int transactionCurrencyId;

	@NotNull(message = ResponseCode.INVALID_AMOUNT)
	private float tipsAmount;

	private int tipsCurrencyId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
	private Date transactionDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
	private Date expiryDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
	private Date fromTransactionDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
	private Date toTransactionDate;

	private String authorizationNumber;

//	private String reason;

	private List<SortDTO> sort;

	private String sortOrder = "";

	private Integer pageNo = 0;

	private Integer pageSize = 20;
}
