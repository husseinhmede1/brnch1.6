package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.mdsl.model.dto.response.JobScheduledMonitoringResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MD_JOB_DEFINITION")

@NamedNativeQuery(
	    name = "job_monitoring_query",
	    query =" SELECT J.JOB_ID " +
	    		"		 AS jobId, " +
	    		"	 J.JOB_NAME " +
	    		"		 AS jobName, " +
	    		"	 S.START_TIME " +
	    		"		 AS startTime, " +
	    		"	 DECODE (J.LAST_RUN_RESULT, " +
	    		"			 '2', 'Force Stop', " +
	    		"			 '3', 'Excecuted Successfully', " +
	    		"			 '4', 'Executed With Errors') " +
	    		"		 AS lastRunStatus, " +
	    		"	 LR.LAST_RUN_START " +
	    		"		 AS lastRunStart, " +
	    		"	 LR.LAST_RUN_END " +
	    		"		 AS lastRunEnd, " +
	    		"	 DECODE (J.ENABLED, " +
	    		"			 1, 'In Progress', " +
	    		"			 decode(s.job_id,j.job_id,'Scheduled','Non Scheduled') " +
	    		"			) " +
	    		"		 AS status " +
	    		" FROM MD_JOB_DEFINITION J " +
	    		"	 left JOIN " +
	    		"	 (SELECT * " +
	    		"		FROM (SELECT START_DATE " +
	    		"						 AS LAST_RUN_START, " +
	    		"					 END_DATE " +
	    		"						 AS LAST_RUN_END, " +
	    		"					 JOB_ID, " +
	    		"					 STATUS, " +
	    		"					 SCHEDULE_EXEC_ID, " +
	    		"					 ROW_NUMBER () " +
	    		"					 OVER (PARTITION BY JOB_ID " +
	    		"						   ORDER BY START_DATE DESC, END_DATE DESC) " +
	    		"						 AS ROWNUMBER " +
	    		"				FROM MD_JOB_EXECUTION_LOG) " +
	    		"	   WHERE ROWNUMBER = 1 AND JOB_ID IS NOT NULL) LR " +
	    		"		 ON LR.JOB_ID = J.JOB_ID " +
	    		"	 left JOIN " +
	    		"	 (SELECT JOB_ID, START_DATE AS START_TIME " +
	    		"		FROM (SELECT JOB_ID, " +
	    		"					 START_DATE, " +
	    		"					 STATUS, " +
	    		"					 ROW_NUMBER () " +
	    		"						 OVER (PARTITION BY JOB_ID ORDER BY START_DATE ASC) " +
	    		"						 AS ROWNUMBER " +
	    		"				FROM MD_JOB_SCHEDULED_EXEC) " +
	    		"	   WHERE ROWNUMBER = 1 AND JOB_ID IS NOT NULL AND STATUS = 0) S " +
	    		"		 ON S.JOB_ID = J.JOB_ID " +
	    		" WHERE J.INST_ID = :instId " +
	    		" ORDER BY JOBID ",
	    resultSetMapping = "job_monitoring_response_dto"
)
@SqlResultSetMapping(
	    name = "job_monitoring_response_dto",
	    classes = @ConstructorResult(
	        targetClass = JobScheduledMonitoringResponseDto.class,
	        columns = {
	            @ColumnResult(name = "jobId", type = Integer.class),
	            @ColumnResult(name = "jobName", type = String.class),
	            @ColumnResult(name = "startTime", type = Date.class),
	            @ColumnResult(name = "lastRunStatus", type = String.class),
	            @ColumnResult(name = "lastRunStart", type = Date.class),
	            @ColumnResult(name = "lastRunEnd", type = Date.class),
	            @ColumnResult(name = "status", type = String.class),
	        }
	    )
)
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_ID")
	@SequenceGenerator(name = "JOB_ID", sequenceName = "MD_JOB_DEFINITION_SEQ", allocationSize = 1)
	@Column(name = "JOB_ID", nullable = false)
	private Integer jobId;
	
	@Column(name = "JOB_NAME")
	private String jobName;
	
	@Column(name = "JOB_DESCRIPTION")
	private String jobDescription;
	
	@Column(name = "ENABLED")
	private String enabled;
	
	@Column(name = "INST_ID")
	private String institution;
	
	@Column(name = "ALERT_SUCCESS")
	private String alertSuccess;
	
	@Column(name = "SUCCESS_EMAIL")
	private String successEmail;
	
	@Column(name = "ALERT_FAILURE")
	private String alertFailure;
	
	@Column(name = "FAIL_EMAIL")
	private String failEmail;
	
	@Column(name = "LAST_EXEC_ID")
	private Integer lastExecId;
	
	@Column(name = "LAST_RUN_RESULT")
	private String lastRunResult;
	
	@Column(name = "STATUS")
	private String status;

	@Column(name = "FREQUENCY")
	private Integer frequency;
	
	@Column(name = "START_DATE")
	private Timestamp startDate;

	@Column(name = "END_DATE")
	private Timestamp endDate;
	
	@Column(name = "RECURRING")
	private String recurring;
	
	@Column(name = "RECURRING_FREQ")
	private Integer recurringFreq;
	
	@Column(name = "MONTH_DAY")
	private Integer monthDay;
	
	@Column(name = "MAX_EXEC_TIME")
	private Integer maxExceTime;
	
	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
	@NotFound (action=NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "job", fetch = FetchType.EAGER)
	@OrderBy("priority ASC")
	private List<JobDefinitionTask> jobDefinitionTask;
}