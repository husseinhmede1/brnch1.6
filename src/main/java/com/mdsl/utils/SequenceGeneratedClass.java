package com.mdsl.utils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.mdsl.model.entity.Institution;

public class SequenceGeneratedClass {

	@Id
	@Column(name = "RECORD_SEQ_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_INSTITUTION_SEQ", allocationSize = 1)
	private int recordSequenceId;
	

	
	
}
