package com.mdsl.model.entity;

import java.sql.Timestamp;
import javax.persistence.*;

import com.mdsl.utils.enumerations.ActivityStatus;
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
@Entity(name = "MD_EXT_PENDING_ACTIVITY")
public class PendingActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PENDING_ACTIVITY_ID")
    @SequenceGenerator(name = "PENDING_ACTIVITY_ID", sequenceName = "MD_PENDING_ACTIVITY_SEQ", allocationSize = 1)
    @Column(name = "PENDING_ACTIVITY_ID", nullable = false)
    private Integer pendingActivityId;

    @ManyToOne
    @JoinColumn(name = "API_ID")
    private Api api;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private ActivityStatus status;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "INST_ID")
    private Integer institution;

    @Column(name = "CLASS")
    private String clazz;

    @Column(name = "METHOD")
    private String method;

	@Column(name = "PAYLOAD")
    private String payload;

    @Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "CREATED_BY")
    private User createdBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;
}