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
@Entity(name = "MD_MCC_TABLE")
public class MCCList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_MCC_LIST_SEQ", allocationSize = 1)
	private Integer mccId;

	@NotNull
	@Column(name = "MCC", columnDefinition = "varchar(5)")
	private String mcc;

	@Column(name = "DESCRIPTION", columnDefinition = "varchar(100)")
	private String description;

	@NotNull
	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String createdBy;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_create")
	private Date createdDate;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="MERCHANT_TYPE_ID")
 	SystemCode merchantType;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CARD_SCHEME_ID", referencedColumnName = "CARD_SCHEME_ID")
	CardScheme cardSchemeTypeMapping;
}
