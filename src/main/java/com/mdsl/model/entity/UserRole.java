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
@JsonIgnoreProperties({"user", "createdBy", "updatedBy"})
@Entity(name="MD_ENT_USER_ROLE")
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_ID")
	@SequenceGenerator(name="USER_ROLE_ID", sequenceName = "MD_USER_ROLE_SEQ", allocationSize = 1)
	@Column(name="USER_ROLE_ID")
	private int userRoleId;
	
	@ManyToOne
    @JoinColumn(name = "ROLE_ID")
	private Role role;
	
	@ManyToOne
    @JoinColumn(name = "INST_ID")
	private Institution institution;
	
	@ManyToOne
    @JoinColumn(name = "USER_ID")
	private User user;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;
}