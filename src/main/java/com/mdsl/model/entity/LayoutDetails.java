package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

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
@Entity(name="MD_CFG_LAYOUT_DETAILS")
public class LayoutDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETAILS_ID")
	@SequenceGenerator(name="DETAILS_ID", sequenceName = "MD_LAYOUT_DETAILS_SEQ", allocationSize = 1)
	@Column(name = "DETAILS_ID", nullable = false)			
	private Integer detailsId;
	
	@Column(name = "LAYOUT_ID")			
	private Integer layoutId;
	
	@Column(name = "ELEMENT_ID")			
	private Integer elementId;
	
	@Column(name="ELEMENT_LENGTH")
	private Integer elementLength;
	
	@Column(name="ELEMENT_PADDING_TYPE")
	private String elementPaddingType;
	
	@Column(name="ELEMENT_PADDING_VALUE")
	private String elementPaddingValue;

	@Column(name="ELEMENT_SECTION")
	private String elemetSection;
	
	@Column(name = "ELEMENT_ORDER")			
	private Integer elementOrder;
	
	@Column(name = "ELEMENT_PARENT_ID")			
	private Integer elementParentId;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
	@Transient 
	private String elementName;
}