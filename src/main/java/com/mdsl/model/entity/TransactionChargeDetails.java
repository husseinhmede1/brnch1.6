package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MD_TRANS_ACCT_GROUP_DTL")
public class TransactionChargeDetails {
	
	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_TRANSACTION_CHARGES_DETAILS_ID_SEQ", allocationSize = 1)
	private Integer transactionChargeDetailsId;

	@Column(name = "description",columnDefinition = "varchar(50)")
	private String description;

	@Column(name = "TRANS_ID")
	private String defaultTransactionId;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

	@Column(name = "USER_CREATE",columnDefinition = "varchar(40)")
	@NotNull
	private String userCreate;

	@Column(name = "DATE_CREATE")
	@NotNull
	private Date dateCreate;
	
	@Column(name = "TRANS_ID_ACCT_GROUP")
	private String transactionGroup;
}
