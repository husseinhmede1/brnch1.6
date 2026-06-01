package com.mdsl.model.dto.request;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.TransactionGroup;

import com.mdsl.utils.ResponseCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionChargesDetailDto {

	@Size(min = 1, max = 12, message = ResponseCode.CFG_INVALID_TRANSACTION_CHARGES_DETAILS_ID)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_TRANSACTION_CHARGES_DETAILS_ID)
	private String transactionChargeDetailsId;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_TRANSACTION_GROUP_DESCRIPTION)
	private String description;

	@Size(min = 1, max = 12, message = ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_DEFAULT_TRANSACTION)
	private String defaultTransactionId;

	@Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INST)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INST)
	private String institutionId;
}
