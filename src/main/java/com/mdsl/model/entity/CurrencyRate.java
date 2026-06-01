package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_CURRENCY_RATE")
public class CurrencyRate {

	@Id
	@Column(name="record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_CFG_CURRENCY_RATE_SEQ", allocationSize = 1)
	private Integer currencyRateId;
	
	
	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="INSTITUTION_ID")
	private Institution institution;

	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;
	
	@NotNull
	@Column(name="EFFECTIVE_DATE")
 	private Date effectiveDate;
	
	@NotNull
	@Column(name="BUY_RATE",columnDefinition = "Decimal(18,9)")
	private Float buyRate;
	
	@NotNull
	@Column(name="MID_RATE",columnDefinition = "Decimal(18,9)")
	private Float midRate;
	
	@NotNull
	@Column(name="SELL_RATE",columnDefinition = "Decimal(18,9)")
	private Float sellRate;

	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
}