package com.mdsl.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
@Entity(name = "MD_FREQUENCY_MASTER")
public class FrequencyMaster implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FREQUENCY_MASTER_ID")
	@SequenceGenerator(name = "FREQUENCY_MASTER_ID", sequenceName = "FREQUENCY_MASTER_CODE_SEQ", allocationSize = 1)
	@Column(name = "FREQUENCY_MASTER_ID")
	private Integer frequencyId;
	
	@Column(name = "FREQUENCY")
	private String frequency;
}
