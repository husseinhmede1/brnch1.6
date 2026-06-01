package com.mdsl.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MD_JOB_THREAD_TRACKER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobThreadTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_thread_seq")
    @SequenceGenerator(name = "job_thread_seq", sequenceName = "JOB_THREAD_TRACKER_SEQ",allocationSize = 1)
    @Column(name = "JOB_THREAD_ID")
    private Integer jobThreadId;

    @Column(name = "JOB_ID")
    private Integer job;

    @Column(name = "THREAD_ID")
    private Long threadId;
    
    @Column(name="THREAD_NAME")
    private String threadName;

    @Column(name = "STATUS", length = 50)
    private String status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;
}