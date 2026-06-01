package com.mdsl.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
@Entity(name = "SCHEME_SPECIFIC")
public class SchemeSpecific {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCHEME_SPECIFIC_ID")
	@SequenceGenerator(name="SCHEME_SPECIFIC_ID", sequenceName = "MD_SCHEME_SPECIFIC_SEQ", allocationSize = 1)
	@Column(name="SCHEME_SPECIFIC_ID")
	private int schemeSpecificId;
	
	@Column(name="SCHEME_SPECIFIC_NAME")
	private String schemeSpecificName;
}
