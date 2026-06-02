package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.mdsl.model.objects.ObjectAndScope;
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
@JsonIgnoreProperties({"createdBy", "updatedBy"})
@Entity(name="MD_BKD_API")
@NamedNativeQuery(
		name = "find_objects_and_scope",
		query ="Select distinct A.API_DESC as objectName" +
				" from MD_BKD_API A where A.ALLOW_STP = 1 AND A.INST_ID =:instId order by A.API_DESC",
		resultSetMapping = "objects_and_scope"
)
@SqlResultSetMapping(
		name = "objects_and_scope",
		classes = @ConstructorResult(
				targetClass = ObjectAndScope.class,
				columns = {
						@ColumnResult(name = "objectName", type = String.class),
				}
		)
)

public class Api {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "API_ID")
	@SequenceGenerator(name="API_ID", sequenceName = "MD_API_SEQ", allocationSize = 1)
	@Column(name = "API_ID", nullable = false)
	private int apiId;

	@Column(name="API_CODE")
	private String apiCode;

	@Column(name="API_DESC")
	private String apiDesc;

	@Column(name="API_URL")
	private String apiUrl;

	@Column(name="LOGIN_REQUIRED")
	private char loginRequired;

	@Column(name="ROLE_REQUIRED")
	private char roleRequired;

	@Column(name="ENABLED")
	private char enabled;

	@Column(name="INST_ID")
	private Integer institution;

	@Column(name="API_FUNCTION")
	private String apiFunction;

	@Column(name="API_OBJECT")
	private String apiObject;

	@Column(name="ALLOW_STP")
	private String allowStp;

	@Column(name="ALLOW_OVERRIDE")
	private String allowOverride;

	@Column(name="STP")
	private String stp;

	@Column(name="DETAILS_NEEDED")
	private Integer detailsNeeded;

	@OneToMany(mappedBy = "api", fetch = FetchType.EAGER)
	private Set<ActivityApi> activityApi;

	@Column(name = "IS_GET_API")
	private String isGetApi;
	
	@Column(name="CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;

	@Column(name="UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;
}