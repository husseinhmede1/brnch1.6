package com.mdsl.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "record_seq_id", "date", "contactStatus" })
@Entity(name = "MD_ACQ_ENTITY_CONTACTS")
public class Contact implements Cloneable,Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_ID")
	@SequenceGenerator(name = "CONTACT_ID", sequenceName = "MD_CONTACT_SEQ", allocationSize = 1)
	@Column(name = "CONTACT_ID")
	private Integer contactId;

	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	private Institution institution;

    @Column(name = "ENTITY_ID")
    private String entity;
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entityObject;

	@OneToOne
	@JoinColumn(name = "CONTACT_ADDRESS_ID")
	private Address address;

	@Column(name = "FIRST_NAME", columnDefinition = "varchar(25)")
	private String firstName;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "MIDDLE_NAME", columnDefinition = "varchar(20)")
	private String middleName;

	@Column(name = "LAST_NAME", columnDefinition = "varchar(20)")
	private String lastName;

	@Column(name = "PROFESSIONAL_TITLE", columnDefinition = "varchar(5)")
	private String professionalTitle;

	@Column(name = "RECEIVE_ESTATEMENT", columnDefinition = "CHAR(1)")
	private Character receiveEstatement;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(nullable = false, name = "date_create")
	private Date date;

	@Column(name = "CONTACT_STATUS")
	private Character contactStatus;

	@NotNull
	@Column(name = "user_create", columnDefinition = "varchar(40)")
	private String userCreate;

	@NotNull
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int record_seq_id;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
