package com.mdsl.model.entity;

import java.sql.Timestamp;

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

@JsonIgnoreProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="MD_OUTPUT_FILE_TEMPLATES_HDR")
public class OutputFileTemplateHdr
{
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OUTPUT_TEMPLATE_HDR_ID")
  @SequenceGenerator(name="OUTPUT_TEMPLATE_HDR_ID", sequenceName="MD_OUTPUT_FILE_TEMPLATES_HDR_SEQ", allocationSize=1)
  @Column(name="OUTPUT_TEMPLATE_HDR_ID")
  private Integer outputTemplateHdrId;

  @Column(name="INSTITUTION_ID")
  private String institutionId;

  @Column(name="OUTPUT_FILE_TYPE")
  private String outputFileType;

  @Column(name="OUTPUT_DESCRIPTION")
  private String outputDescription;

  @Column(name="SUM_PER_ACCOUNT")
  private String sumPerAccount;

  @Column(name="MERCHANT_SUB_SUMMARY")
  private String merchantSubSummary;
  
  @Column(name="INST_SUB_SUMMARY")
  private String instSubSummary;

  @Column(name="OUTPUT_FORMAT")
  private String outputFormat;

  @Column(name="SEPARATOR")
  private String separator;

  @Column(name="CREATED_BY")
  private Integer createdBy;

  @Column(name="CREATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdDate;

  @Column(name="UPDATED_BY")
  private Integer updatedBy;

  @Column(name="UPDATED_DATE", columnDefinition="DATE DEFAULT CURRENT_TIMESTAMP")
  private Timestamp updatedDate;
  
  @Column(name="OUTPUT_FILE_TYPE_ABBR")
  private String outputFileTypeAbbr;
}