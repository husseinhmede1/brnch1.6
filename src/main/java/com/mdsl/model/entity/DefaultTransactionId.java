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
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "MD_default_trans_id")
public class DefaultTransactionId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="record_seq_id")
	@SequenceGenerator(name="record_seq_id", sequenceName="MD_DEF_TRANS_SEQ", allocationSize=1)
	@Column(name = "record_seq_id")
	private Integer recordSequenceId;

	@Column(name = "trans_id", nullable = false, columnDefinition = "varchar(12)")
	private String transactionId;

	@Column(name = "description", columnDefinition = "varchar(30)")
	private String description;

	@Column(name = "sign_flag", columnDefinition = "CHAR(1)")
	private Character signFlag;
	
	@Column(name="trans_usage")
	private String transUsage;

	@Column(name = "user_create", columnDefinition = "varchar(40)")
	@NotNull
	private String userCreate;

	@Column(name = "date_create")
	@NotNull
	private Date dateCreate;

	@Column(name = "INSTITUTION_ID", insertable = false, updatable = false)
	private String institutionId;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;
	
	@Column(name = "status")
	private Character status;

}
