package com.mdsl.model.entity;

import java.sql.Date;
import java.util.Set;

import javax.persistence.*;
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
@Entity(name = "MD_ACQ_ACTIVITY_PKG_DTL")
public class ActivityPackageDetail {

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_PKG_DETAIL_SEQ", allocationSize = 1)
	private Integer packageDetailId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;

	@Column(name = "PKG_ID")
	String activityPackage;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "PKG_ID", referencedColumnName = "PKG_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	ActivityPackage activityPackageEntity;
	
	@Column(name = "PKG_LINE")
	private Integer packageLine;

	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;

	@Column(name = "ASSIGNED_TRANS_ID")
	private String assignedTranId;
	
	@Column(name = "issuer_acq_profile")
	String issuerAcqProfile;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "issuer_acq_profile", referencedColumnName = "issuer_acq_profile", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private IssuerProfile issuerAcqProfileEntity;
	

	@Column(name="TRANS_ID_ACCT_GROUP")
	private String tranGroup;

	@Column(name = "FEE_METHOD")
	String chargeMethod;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "SCHEME_CODE", referencedColumnName = "CARD_SCHEME_ID")
	CardScheme cardScheme;

	@Column(name = "START_DATE")
	private java.util.Date startDate;

	@Column(name = "END_DATE")
	private java.util.Date endDate;

	@Column(name = "PERCENTAGE_AMOUNT",columnDefinition = "Decimal(6,3)")
	private Float percentageAmount;

	@Column(name = "FIX_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float fixAmount;

	@Column(name = "MINIMUN_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float minAmount;

	@Column(name = "MAXIMUM_AMOUNT",columnDefinition = "Decimal(15,3)")
	private Float maxAmount;

	@Column(name = "TIPS",columnDefinition = "CHAR(1)")
	private Character tips;

	@Column(name = "status")
	private Character status;

	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;

	@Column(name = "date_create")
	private Date dateCreate;

	@OneToMany(orphanRemoval = true, mappedBy = "activityPackageDetail")
	//@JoinColumn(name = "PKG_ID")
	private Set<ActivityPackageTier> activityPackageTier;

}
