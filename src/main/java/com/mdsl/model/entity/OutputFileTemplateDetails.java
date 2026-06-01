package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties
@Transactional
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="MD_OUTPUT_FILE_TEMPLATE_DTLS")
public class OutputFileTemplateDetails {
	
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OUTPUT_TEMPLATE_DTL_ID")
  @SequenceGenerator(name="OUTPUT_TEMPLATE_DTL_ID", sequenceName="MD_OUTPUT_FILE_TEMPLATE_DTLS_SEQ", allocationSize=1)
  @Column(name="OUTPUT_TEMPLATE_DTL_ID")
  private Integer outputTemplateDtlId;

  @Column(name="INSTITUTION_ID")
  private String institutionId;
  
  @Column(name="OUTPUT_TEMPLATE_HDR_ID")
  private Integer outputTemplateHdrId;
  
  @Column(name="FIELD_SECTION")
  private String fieldSection;

  @Column(name="FIELD_SEQUENCE")
  private Integer fieldSequence;

  @Column(name="FIELD_ID")
  private Integer fieldId;

  @Column(name="FIELD_PAD")
  private String fieldPad;

  @Column(name="FIELD_PAD_CHAR")
  private String fieldPadChar;

  @Column(name="FIELD_LENGTH")
  private Integer fieldLength;
  
  @Column(name="FIELD_FORMAT")
  private String fieldFormat;

  @Column(name="CREATED_BY")
  private Integer createdBy;

  @Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdDate;

  @Column(name="UPDATED_BY")
  private Integer updatedBy;

  @Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp updatedDate;
  
  @Column(name="FIELD_CSYNTAX")
  private String fieldCsyntax;
}
