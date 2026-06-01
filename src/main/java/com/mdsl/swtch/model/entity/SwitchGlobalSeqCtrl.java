package com.mdsl.swtch.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
@Entity(name = "GLOBAL_SEQ_CTRL")
public class SwitchGlobalSeqCtrl {
	
	@Id
	@Column(name = "SEQ_NAME")
	private String seqName;
	
	@Column(name = "SITE_ID")
	private Integer siteId;
	
	@Column(name = "LAST_SEQ_NBR")
	private Integer lastSeqNbr;
	
	@Column(name = "MAX_SEQ_NBR")
	private Integer maxSeqNbr;
	
	@Column(name = "SEQ_BLOCK_SIZE")
	private Integer seqBlockSize;

}
