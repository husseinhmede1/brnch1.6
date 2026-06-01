package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonActivityFeeQueryRequestDto {

	private int nonActivityFeeQueryId;

	@Nullable
	@Size(min = 1, max = 23, message = ResponseCode.CFG_INVALID_REF_NBR)
	private String microfilmRefNbr;
	
	@Nullable
	@Min(value = 1, message = ResponseCode.CFG_INVALID_REF_NBR)
	@Max(value = 99, message = ResponseCode.CFG_INVALID_REF_NBR)
	private Integer refNbrSeq;

	private String entityId;

	private String institutionId;

	private String transactionId;

	private Date transactionDate;

	private float transactionAmount;

	private Integer transactionCurrencyId;

	@Size(max = 10, message = ResponseCode.INVALID_REVERSAL_REASON)
	private String reversalReason;

	private char manualEntry;

	@Size(max = 100, message = ResponseCode.LOG_INVALID_DESC)
	private String transDesc;

	private Date processingDate;

	@Nullable
	@Size(max = 30, message = ResponseCode.CFG_INVALID_REF_NBR)
	private String processingRefNbr;

	private String reason;

	private String description;
	
	private Date fromProcessingDate;

	private Date toProcessingDate;
	
	private List<SortDTO> sort;

	private String sortOrder = "";
	
	private Integer pageNo = 0;
	  
	private Integer pageSize = 20;
	
	private char reversalFlag;

	public void setTransactionDate(String transactionDate) {
		try {
			this.transactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setProcessingDate(String processingDate) {
		try {
			this.processingDate = new SimpleDateFormat("dd/MM/yyyy").parse(processingDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setFromProcessingDate(String fromProcessingDate) {
		try {
			this.fromProcessingDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromProcessingDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setToProcessingDate(String toProcessingDate) {
		try {
			this.toProcessingDate = new SimpleDateFormat("dd/MM/yyyy").parse(toProcessingDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
