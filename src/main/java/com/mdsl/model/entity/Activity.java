package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

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
@JsonIgnoreProperties({"parentActivity", "createdBy", "updatedBy"})
@Entity(name="MD_CFG_ACTIVITY")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_ID")
	@SequenceGenerator(name = "ACTIVITY_ID", sequenceName = "MD_ACTIVITY_SEQ", allocationSize = 1)
	@Column(name = "ACTIVITY_ID")
	private int activityId;

	@Column(name = "ACTIVITY_CODE")
	private String activityCode;

	@Column(name = "ACTIVITY_DESC")
	private String activityDesc;

	@ManyToOne
	@JoinColumn(name = "PARENT_ACTIVITY_ID")
	private Activity parentActivity;

	@Column(name = "IS_MENU")
	private char isMenu;

	@Column(name = "HAS_SCREEN")
	private char hasScreen;

	@Column(name = "ACTIVITY_TYPE")
	private char activityType;

	@Column(name = "ACTIVITY_MODE")
	private char activityMode;

	@Column(name = "ACCESS_VIEW")
	private char accessView;

	@Column(name = "ACCESS_ADD")
	private char accessAdd;

	@Column(name = "ACCESS_UPDATE")
	private char accessUpdate;

	@Column(name = "ACCESS_DELETE")
	private char accessDelete;

	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;

	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;

	@OneToMany(mappedBy = "activity", fetch = FetchType.EAGER)
	private Set<ActivityApi> activityApi;
}