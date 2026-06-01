package com.mdsl.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "MD_BANK_INFO")
public class BankCode implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_BANK_CODE_SEQ", allocationSize = 1)
	private Integer bankCodeId;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "BANK_CODE",columnDefinition = "varchar(10)")
	private String bankCode;
	
	@Column(name = "BANK_NAME",columnDefinition = "varchar(50)")
	private String bankName;
	
	@Column(name = "ALT_BANK_NAME",columnDefinition = "varchar(100)")
	private String altBankName;
	
	@Column(name = "SWIFT_CODE",columnDefinition = "varchar(15)")
	private String swiftCode;
	
	@NotNull
	@Column(name="user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_create")
	private Date dateCreate;	
	
	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
}
