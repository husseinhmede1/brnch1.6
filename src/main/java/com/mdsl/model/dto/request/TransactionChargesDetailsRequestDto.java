package com.mdsl.model.dto.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.mdsl.model.entity.DefaultTransactionId;

import lombok.Data;

@Data
public class TransactionChargesDetailsRequestDto {

	private Integer transactionChargeDetailsId;
	private String description;
	private String defaultTransactionId;
	private String institutionId;
}