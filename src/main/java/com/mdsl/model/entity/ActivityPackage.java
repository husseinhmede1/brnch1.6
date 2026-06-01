package com.mdsl.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties
@Entity(name = "MD_ACQ_ACTIVITY_PKG")
public class ActivityPackage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_SEQ_ID")
	@SequenceGenerator(name = "RECORD_SEQ_ID", sequenceName = "MD_ACT_PKG_SEQ", allocationSize = 1)
	@Column(name="RECORD_SEQ_ID")
	private Integer recordSeqId;

	@Column(name = "PKG_ID", nullable = false)
	private String packageId;

	@Column(name = "PKG_NAME",columnDefinition = "varchar(50)")
	private String packageName;

	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;
	
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Column(name = "date_create")
	private Date dateCreate;

	@Column(name = "status")
	private Character status;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID", insertable = false, updatable = false) 
	private Institution institution;


	@OneToMany(orphanRemoval = true, mappedBy = "activityPackage")
	private Set<ActivityPackageDetail> activityPackageDetail;
}
