package com.mdsl.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="MD_INSTITUTION_CONTROL")
public class InstitutionControl {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RECORD_SEQ_ID")
	@SequenceGenerator(name="RECORD_SEQ_ID", sequenceName="MD_INSTITUTION_SEQ", allocationSize=1)
	@Column(name="RECORD_SEQ_ID")
	private Integer recordSeqId;
	
	@Column(name="INSTITUTION_ID")
	private String institutionId;
	
	@Column(name="BASE_CURRENCY")
	private String baseCurrency;
	
	@Column(name="MERCHANT_RATE_USAGE")
	private String merchantRateUsage;
	
	@Column(name="WEEK_DAY")
	private String weekDay;
	
	@Column(name="OUTPUT_DIRECTORY")
	private String outputDirectory;
	
	@Column(name="INPUT_DIRECTORY")
	private String inputDirectory;
	
	@Column(name="DISCOUNT_RETURN_ON")
	private Integer discountReturnOn;
	
	@Column(name="EOD_PROCESS_BY_TXN")
	private String eodProcessByTxn;

}
