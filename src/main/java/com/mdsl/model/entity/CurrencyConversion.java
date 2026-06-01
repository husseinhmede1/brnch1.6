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
@Entity(name = "MD_CURRENCY_conversion")
public class CurrencyConversion {

	@Id
	@Column(name="record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_CFG_CURRENCY_CONVERSION_SEQ", allocationSize = 1)
	private Integer currencyConversionId;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="INSTITUTION_ID")
	private Institution institution;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="BASE_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency baseCurrency;
	
	@NotNull
	@Column(name = "ROUNDING_RULE",columnDefinition = "varchar(4)")
	private String roundingRule;
	
	@NotNull
	@Column(name = "RATE_EXPRESSION",columnDefinition = "varchar(1)")
	private String rateExpression;
	
	@Column(name = " MID_RATE_USED",columnDefinition = "varchar(1)")
	private String midRateUsed;

	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@NotNull
	@Column(name = "date_create")
	private Date dateCreate;
}