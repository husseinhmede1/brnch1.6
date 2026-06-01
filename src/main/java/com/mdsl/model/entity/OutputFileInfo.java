package com.mdsl.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@Entity(name = "MD_OUTPUT_FILE_FIELDS_INFO")
public class OutputFileInfo {

    @Id
    @Column(name = "record_seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
    @SequenceGenerator(name = "record_seq_id", sequenceName = "MD_OUTPUT_FILE_FIELDS_INFO_SEQ", allocationSize = 1)
    private Integer recordSeqId;

    @Column(name = "OUTPUT_FILE_TYPE")
    private String outputFileType;

    @Column(name = "SUM_PER_ACCOUNT")
    private String sumPerAccount;

    @Column(name = "SUB_SUMMARY")
    private String subSummary;

    @Column(name = "POSITION")
    private Integer position;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DATE_CREATE")
    private Date dateCreate;
}
