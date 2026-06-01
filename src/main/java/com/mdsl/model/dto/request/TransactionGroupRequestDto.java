package com.mdsl.model.dto.request;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.mdsl.model.entity.DefaultTransactionId;
import com.mdsl.model.entity.TransactionChargeDetails;
import com.mdsl.utils.ResponseCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionGroupRequestDto {

	@Valid
	private List<TransactionChargesDetailDto>  chargesDetailDtos;

	@Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INST)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INST)
	private String institutionId;

	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID)
	private String transactionGroupId;

	@Size(min = 1, max = 50, message = ResponseCode.CFG_INVALID_TRANSACTION_GROUP_NAME)
	private String transactionGroupName;

	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_TRANSACTION_GROUP_DESCRIPTION)
	private String transactionGroupDescription;

	private char status;
}
