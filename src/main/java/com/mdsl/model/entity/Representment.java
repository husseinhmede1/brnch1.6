package com.mdsl.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "MD_REPRESENTMENT")
public class Representment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MD_REPRESENTMENT_ID")
	@SequenceGenerator(name = "REPRESENTMENT_ID", sequenceName = "MD_REPRESENTMENT_SEQ", allocationSize = 1)
	@Column(name = "MD_REPRESENTMENT_ID")
	private int rePresentmentId;

	@Column(name = "REPRESENTMENT_REASON")
	private String rePresentmentReason;
}
