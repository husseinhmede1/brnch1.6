package com.mdsl.model.entity;

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
@Entity(name = "MD_system_codes")
public class SystemCode {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_SYSTEM_CODES_SEQ", allocationSize = 1)
	private int systemCodeId;
	
	@ManyToOne
	@JoinColumn(name="INSTITUTION_ID")
	private Institution institution;
	
	@Column(name = "code_suffix",columnDefinition = "varchar(18)")
	private String codeSuffix;

	@Column(name = "code_prefix",columnDefinition = "varchar(30)")
	private String codePrefix;
	
	@ManyToOne
	@JoinColumn(name="CODE_HDR_ID")
	private SystemCodeHeader systemCodeHeader;

	@Column(name = "code_value",columnDefinition = "varchar(100)")
	private String codeValue;
	
	@Column(name = "description",columnDefinition = "varchar(100)")
	private String description;

	@NotNull
	@Column(name="user_create",columnDefinition = "varchar(40)")
	private String createdBy;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_create")
	@NotNull
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
}
