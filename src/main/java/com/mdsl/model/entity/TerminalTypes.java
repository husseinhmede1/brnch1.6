package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="MD_POINT_OF_SALES_TYPE")
public class TerminalTypes implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_TERMINAL_TYPES_SEQ", allocationSize = 1)
	private Integer terminalTypesId;
	
	@Column(name="TERMINAL_TYPE",columnDefinition = "varchar(5)")
	@NotNull
	private String terminalType;
	
	@Column(name="MAKE_NAME",columnDefinition = "varchar(30)")
	private String makeName;
	
	@Column(name="MAKE_MODEL",columnDefinition = "varchar(30)")
	private String makeModel;
	
	@Column(name="POS_CAPABILITY",columnDefinition = "varchar(100)")
	private String posCapability;
	
	@Column(name="FREE_TEXT",columnDefinition = "varchar(100)")
	private String freeText;

	@Column(name="USER_CREATE",columnDefinition = "varchar(40)")
	@NotNull
	private String userCreate;
	
	@Column(name="DATE_CREATE")
	@NotNull
	private Date dateCreate;

	@Column(name="status",columnDefinition = "varchar(1)")
	private String status;

}
