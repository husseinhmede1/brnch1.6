package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManualNonActivityTransactionRequestDto {

	private int manualNonActivityTransactionId;

	@NotEmpty(message = ResponseCode.CFG_INVALID_INST_CODE)
	private String institutionId;

	private String transactionId;

	//@NotEmpty(message = ResponseCode.MNT_INVALID_REVERSE_FLAG)
	private String reversalFlag;

	private int systemCodeId;

	private String comments;

//	@NotEmpty(message = ResponseCode.CFG_ENTITY_NOT_FOUND)
	private String outletId;

	@NotNull(message = ResponseCode.INVALID_TRANSACTION_AMOUNT)
	private float transactionAmount;

	@NotNull(message = ResponseCode.CFG_INVALID_CURRENCY)
	private int transactionCurrencyId;

	private Date transactionDate;
	
	private Date fromTransactionDate;

	private Date toTransactionDate;
	
	private List<SortDTO> sort;

	private String sortOrder = "";
	
	private Integer pageNo = 0;
	  
	private Integer pageSize = 20;

//	private String reason;

	public void setTransactionDate(String transactionDate) {
		try {
			this.transactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
	public void setFromTransactionDate(String fromTransactionDate) {
		try {
			this.fromTransactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromTransactionDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setToTransactionDate(String toTransactionDate) {
		try {
			this.toTransactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(toTransactionDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
