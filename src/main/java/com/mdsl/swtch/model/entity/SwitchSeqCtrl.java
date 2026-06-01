package com.mdsl.swtch.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SEQ_CTRL")
@IdClass(SwitchSeqCtrlPK.class) 
public class SwitchSeqCtrl {
	
	@Id
	@Column(name = "SEQ_NAME")
	private String seqName;
	
	@Id
	@Column(name = "INSTITUTION_ID")
	private String institutionId;
	
	@Id
	@Column(name = "SITE_ID")
	private Integer siteId;
	
	@Column(name = "LAST_SEQ_NBR")
	private Integer lastSeqNbr;
	
	@Column(name = "MAX_SEQ_NBR")
	private Integer maxSeqNbr;
	
	@Column(name = "SEQ_BLOCK_SIZE")
	private Integer seqBlockSize;

}
