package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name="MD_Trans_ACCT_Group_HDR")
public class TransactionGroup implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_TRANS_GROUP_SEQ", allocationSize = 1)
	private int transactionGroupId;
	
	@Column(name="TRANS_ID_ACCT_GROUP",columnDefinition = "varchar(50)")
	@NotNull
	private String transactionGroupName;
	
	@Column(name="DESCRIPTION",columnDefinition = "varchar(100)")
	private String transactionGroupDescription;
	
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;
	
	@Column(name="USER_CREATE",columnDefinition = "varchar(40)")
	@NotNull
	private String userCreate;
	
	@Column(name="DATE_CREATE")
	@NotNull
	private Date dateCreate;

	@Column(name="status")
	private Character status;
}
