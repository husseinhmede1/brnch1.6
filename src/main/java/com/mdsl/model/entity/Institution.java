package com.mdsl.model.entity;

import java.util.Date;

//import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({ "dateCreate", "userCreate" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_INSTITUTION")
public class Institution {

	@Id
 	@Column(name = "INSTITUTION_ID", nullable = false, columnDefinition = "varchar(10)")
	private String institutionId;

	@Column(name = "INSTITUTION_NAME", unique = true, columnDefinition = "varchar(50)")
	private String institutionName;

	@Column(name = "INSTITUTION_NAME_ALT", columnDefinition = "varchar(100)")
	private String institutionTypeAlt;

 	@Column(name = "RECORD_SEQ_ID")
    private Integer recordSeqId;

	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;

	@Column(name = "date_create")
	private Date dateCreate;

	@Column(name = "status")
	private Character status;

 	@ManyToOne
	@JoinColumn(name = "INST_TYPE")
 	private SystemCode institutionType;

}
