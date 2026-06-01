package com.mdsl.model.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "password", "createdBy", "updatedBy" })
@Entity(name = "MD_ENT_USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID")
	@SequenceGenerator(name = "USER_ID", sequenceName = "MD_USER_SEQ", allocationSize = 1)
	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "USERNAME", unique = true)
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "MOBILE")
	private String mobile;

	@ManyToOne
	@JoinColumn(name = "PREFFERED_LANGUAGE")
	private SystemCode preferedLanguage;

	@Column(name = "EMAIL", unique = true)
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution1;

	@Column(name = "STATUS")
	private char status;

	@Column(name = "LAST_LOGIN_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP", updatable = false)
	private Timestamp lastLoginDate;

	@Column(name = "CREATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private User createdBy;

	@Column(name = "UPDATED_DATE", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	private User updatedBy;

	@OneToMany(mappedBy = "user")
	private Set<UserRole> userRoles;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "MD_USER_INST_MAPPING", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {@JoinColumn(name = "INSTITUTION_ID") })
	private List<Institution> institution;

	@Column(name = "PASSWORD_RETRIES")
	private int passRetries;

	@Transient
	private boolean isApiAllowed;

	public User(int userId, String username, String password) {
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
}
