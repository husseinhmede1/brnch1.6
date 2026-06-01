package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_Employee")
public class Employee {

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_EMPLOYEE_SEQ", allocationSize = 1)
	private Integer employeeId;

	@Column(name = "NAME", columnDefinition = "varchar(50)")
	String employeeName;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ROLE_CODE")
	SystemCode employeeRole;

	@Column(name = "PHONE")
	private String employeePhone;

	@Column(name = "EMAIL")
	private String employeeEmail;
	
	@Column(name = "PHONE_EXT", columnDefinition = "varchar(10)")
	String employeeExt;

	@Column(name = "status")
	private Character status;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

}
