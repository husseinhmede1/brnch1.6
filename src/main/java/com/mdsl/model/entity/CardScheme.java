package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name="MD_CARD_SCHEME")
public class CardScheme implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CARD_SCHEME_ID", nullable = false,columnDefinition = "varchar(6)")
	private String cardSchemeId;
	
	@Column(name="CARD_SCHEME_NAME",columnDefinition = "varchar(50)")
	private String cardSchemeName;

	@Column(name="RECORD_SEQUENCE_NUMBER")
	private Integer recordSequenceNumber;
	
	@NotNull
	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String createdBy;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_create")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name="CARD_SCHEME_SPECIFIC",columnDefinition = "varchar(10)")
	private String cardSchemeSpecific;
}