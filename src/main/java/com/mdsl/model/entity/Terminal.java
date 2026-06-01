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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "MD_POINT_OF_SALES")
public class Terminal implements Cloneable, Serializable {

	@Id
	@Column(name = "RECORD_SEQ_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq_id")
	@SequenceGenerator(name = "record_seq_id", sequenceName = "MD_TERMINAL_SEQ", allocationSize = 1)
	private int recordSequenceId;

	@Column(name="TERMINAL_ID")
	private String terminalId;

	@ManyToOne
	@JoinColumn(name = "TERMINAL_TYPE", referencedColumnName = "TERMINAL_TYPE")
	private TerminalTypes terminalTypes;

	@Column(name = "ECOMMERCE_FLAG", columnDefinition = "CHAR(1)")
	private Character eCommerceFlag;

	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;

	@Column(name = "TERMINATION_DATE")
	private Date terminationDate;

	@Column(name = "SERIAL_NUMBER", columnDefinition = "varchar(30)")
	private String serialNumber;

//	@ManyToOne
//	@JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID")
//	@NotNull
//	private Institution institution;
	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	@NotNull
	private Institution institutionEntity;

	@Column(name = "INSTITUTION_ID")  // Add this for actual insert/update
	@NotNull
	private String institution;

	@ManyToOne
	@JoinColumn(name = "DEFAULT_CURR_CODE", referencedColumnName = "CURRENCY_CODE")
	private Currency currency;

	@Column(name = "DEFAULT_MCC")
	private String mccList;

	@Column(name = "USER_CREATE", columnDefinition = "varchar(40)")
	@NotNull
	private String userCreate;

	@Column(name = "DATE_CREATE")
	@NotNull
	private Date dateCreate;

	@Column(name = "status")
	private Character status;

    @Column(name = "ENTITY_ID")
    private String entities;
	
	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private Entities entitiesObject;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
