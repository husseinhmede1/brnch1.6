package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@JsonIgnoreProperties({ "role", "createdBy", "updatedBy" })
@Entity(name = "MD_ENT_ROLE_ACTIVITY")
public class RoleActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_ACTIVITY_ID")
	@SequenceGenerator(name = "ROLE_ACTIVITY_ID", sequenceName = "MD_ROLE_ACTIVITY_SEQ", allocationSize = 1)
	@Column(name = "ROLE_ACTIVITY_ID")
	private int roleActivityId;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Role role;

	
	@ManyToOne
    @JoinColumn(name = "ACTIVITY_ID") 
    private Activity activity;
	 

	@Column(name = "ACCESS_VIEW")
	private String accessView;

	@Column(name = "ACCESS_ADD")
	private String accessAdd;

	@Column(name = "ACCESS_UPDATE")
	private String accessUpdate;

	@Column(name = "ACCESS_DELETE")
	private String accessDelete;

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
}