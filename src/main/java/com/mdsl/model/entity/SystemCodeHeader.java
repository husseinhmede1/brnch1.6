package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MD_SYSTEM_CODES_HEADER")
public class SystemCodeHeader {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_SYSTEM_CODES_HEADER_SEQ", allocationSize = 1)
	private Integer systemCodeHeaderId;

	@Column(name = "code_prefix",columnDefinition = "varchar(30)")
	private String codePrefix;
	
	@Column(name = "code_pattern",columnDefinition = "CHAR(1)")
	private Character codePattern;
	
	@Column(name = "MAX_SUFFIX_LENGTH")
	private Integer maxSuffixLength;
	
	@Column(name = "description",columnDefinition = "varchar(100)")
	private String description;
	
	@Column(name="USER_FLAG")
	private Character userFlag;
	
}
