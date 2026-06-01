package com.mdsl.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
@Entity(name="MD_JOB_VALIDATOR")
public class JobValidator {
	@Id
	@Column(name="JOB_CRON_STRING")
	private String conString;
}