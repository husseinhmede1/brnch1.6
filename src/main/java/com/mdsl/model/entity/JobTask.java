package com.mdsl.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;

import com.mdsl.model.dto.response.BKDParameterResponseDto;

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
@Entity(name = "MD_JOB_TASK")

@NamedNativeQuery(
	    name = "job_task_parameters",
	    query ="SELECT P.PARAMETER_ID AS parameterId, SP.PARAMETERS_SERVICE_ID AS parameterServiceId, P.PARAMETER_NAME AS parameterName, SP.IS_MANDATORY as isMandatory "
	    		+ "FROM MD_JOB_TASK T JOIN MD_BKD_SERVICE S ON T.SERVICE_ID = S.SERVICE_ID "
	    		+ "JOIN MD_BKD_PARAMETERS_SERVICE SP ON SP.SERVICE_ID = S.SERVICE_ID "
	    		+ "JOIN MD_BKD_PARAMETERS P ON SP.PARAMETER_ID = P.PARAMETER_ID "
	    		+ "WHERE T.TASK_ID = :taskId ",
	    resultSetMapping = "parameters_response_dto"
)
@SqlResultSetMapping(
	    name = "parameters_response_dto",
	    classes = @ConstructorResult(
	        targetClass = BKDParameterResponseDto.class,
	        columns = {
	            @ColumnResult(name = "parameterId", type = Integer.class),
	            @ColumnResult(name = "parameterServiceId", type = Integer.class),
	            @ColumnResult(name = "parameterName", type = String.class),
	            @ColumnResult(name = "isMandatory", type = String.class),
	        }
	    )
)
public class JobTask {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_ID")
	@SequenceGenerator(name = "TASK_ID", sequenceName = "MD_JOB_TASK_SEQ", allocationSize = 1)
	@Column(name = "TASK_ID")
	private Integer taskId;
	
	@Column(name = "TASK_NAME")
	private String taskName;
	
	@ManyToOne
	@JoinColumn(name = "SERVICE_ID")
	private BKDService service;
	
	@Column(name = "TASK_DESCRIPTION")
	private String taskDescription;

	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
}