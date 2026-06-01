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
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name="MD_ADT_USER_ACCESS")
public class UserAccess {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ACCESS_ID")
	@SequenceGenerator(name="USER_ACCESS_ID", sequenceName = "USER_ACCESS_SEQ", allocationSize = 1)
	@Column(name="USER_ACCESS_ID")
	private int userAccessId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@Column(name="LOGIN_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP", updatable = false)
	private Timestamp loginDate;
	
	@Column(name="JWT")
	private String jwt;

	@Column(name="REFRESH_JWT")
	private String refreshJwt;
	
	@Column(name="SOURCE_IP")
	private String sourceIp;
}