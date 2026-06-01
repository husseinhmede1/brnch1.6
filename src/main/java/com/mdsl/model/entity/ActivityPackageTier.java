package com.mdsl.model.entity;

import java.sql.Date;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({ "dateCreate", "recordSeqId", "userCreate" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_ACQ_ACTIVITY_PKG_TIER")
public class ActivityPackageTier {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_PKG_TIER_SEQ", allocationSize = 1)
	private Integer activityPackageTierId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;

	@ManyToOne()
	@JoinColumn(name = "PKG_DETAIL_ID", referencedColumnName = "record_seq_id")
	private ActivityPackageDetail activityPackageDetail;

	@Column(name = "PKG_LINE")
	private Integer packageLine;

	@Column(name = "TIER_LINE")
	private Integer tireLine;

	@Column(name = "TIER_TYPE",columnDefinition = "varchar(6)")
	private String tireType;

	@Column(name = "TIER_CUMUL_ON")
	private String tireCumlOn;

	@Column(name = "PERCENTAGE_AMOUNT",columnDefinition = "Decimal(6,3)")
	private Float percentageAmount;

	@Column(name = "FIX_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float fixAmount;

	@Column(name = "START_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float startAmount;

	@Column(name = "UPTO_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float uptoAmount;

	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;

	@Column(name = "date_create")
	private Date dateCreate;
}