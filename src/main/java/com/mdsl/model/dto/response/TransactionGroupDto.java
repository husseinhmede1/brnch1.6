package com.mdsl.model.dto.response;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import com.mdsl.model.entity.Institution;

import com.mdsl.utils.ResponseCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionGroupDto {

	@Size(min=0,max = 10, message = ResponseCode.CFG_INVALID_TRANSACTION_GROUP_ID)
	private int transactionGroupId;

	private String transactionGroupName;

	private String transactionGroupDescription;

	private String institutionId;

	private char status;
}
