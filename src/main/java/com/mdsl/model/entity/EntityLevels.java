package com.mdsl.model.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_ACQ_ENTITY_LEVELS")
public class EntityLevels implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "record_seq_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "ENTITY_LEVELS_ID_SEQ", allocationSize = 1)
	private Integer entityLevelId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;

	@Column(name = "HIERARCHY_level")
	private Integer hierarchyLevel;

	@Column(name = "type_description", columnDefinition = "varchar(40)")
	private String typeDescription;

	@Column(name = "id_length")
	private Integer idLength;

	@Column(name = "generation_method")
	private Integer generationMethod;

	@Column(name = "STATEMENT_FLAG", columnDefinition = "CHAR(1)")
	private Character statementFlag;

	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;

	@Column(name = "date_create")
	private Date dateCreate;

}
