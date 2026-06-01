package com.mdsl.model.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.mdsl.model.entity.keys.EntitiesId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(EntitiesId.class)
@Entity(name = "MD_ACQ_ENTITY")
public class Entities  implements Cloneable,Serializable{

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_SEQ_ID")
	@SequenceGenerator(name = "RECORD_SEQ_ID", sequenceName = "MD_ACQ_ENTITY_SEQ", allocationSize = 1)
	@Column(name = "RECORD_SEQ_ID", nullable = false)
	private int record_seq_id;

	@Id
	@Column(name = "ENTITY_ID", nullable = false,columnDefinition = "varchar(30)")
	private String entityId;
	@Id
	@ManyToOne
	@JoinColumn(name = "INSTITUTION_ID")
	Institution institution;

	@Column(name="DEFAULT_MCC")
	private String defaultMCC;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "BUSINESS_TYPE")
	SystemCode businessType;

	@Column(name = "ENTITY_LEVEL")
	private Integer entityLevels;

	@Column(name = "ACTIVITY_FEE_PKG", columnDefinition = "varchar(30)")
	String activityFeePKG;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "ACTIVITY_FEE_PKG", referencedColumnName = "PKG_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	ActivityPackage activityFeePKGEntity;
	
	@Column(name = "NON_ACTIVITY_FEE_PKG", columnDefinition = "varchar(30)")
	String nonactivityFeePKG;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "NON_ACTIVITY_FEE_PKG", referencedColumnName = "PKG_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	NonActivityPackage nonactivityFeePKGEntity;
	
	@Column(name = "DEF_BANK_CODE", columnDefinition = "varchar(30)")
	private String defBankCode;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumns({
	    @JoinColumn(name = "DEF_BANK_CODE", referencedColumnName = "BANK_CODE", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	private BankCode defBankCodeEntity;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "SALESMAN_ID")
	Employee salesman;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "EMPLOYEE_INCHARGE")
	Employee employeeIncharge;

	@Column(name = "PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private String parentId;

	@ManyToOne
	@JoinColumns({
	    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
	    @JoinColumn(name = "INSTITUTION_ID", referencedColumnName = "INSTITUTION_ID", insertable = false, updatable = false)
	})
	@NotFound(action = NotFoundAction.IGNORE)
	private Entities parentIdEntity;
	
	@NotNull
	@Column(name = "ENTITY_NAME",columnDefinition = "varchar(50)")
	private String entityName;

	@Column(name = "ENTITY_NAME_ALT",columnDefinition = "varchar(100)")
	private String entityNameAlt;

	@Column(name = "DBA_NAME",columnDefinition = "varchar(50)")
	private String dbaName;

	@Column(name = "DBA_Name_ALT",columnDefinition = "varchar(100)")
	private String dbaNameAlt;

	@Column(name = "ENTITY_STATUS",columnDefinition = "CHAR(1)")
	private Character entityStatus;

	@Column(name = "CONTRACT_DATE")
	private Date contractDate;

	@Column(name = "EXP_START_DATE")
	private Date expStartDate;

	@Column(name = "ACTUAL_START_DATE")
	private Date actualStartDate;

	@Column(name = "TERMINATION_DATE")
	private Date terminationDate;

	@Column(name = "ON_HOLD_IND",columnDefinition = "CHAR(1)")
	private Character onHoldInd;

	@Column(name = "HOT_MERCHANT_FLAG",columnDefinition = "CHAR(1)")
	private Character hotMerchantFlag;

	@Column(name = "COMPANY_TYPE",columnDefinition = "varchar(5)")
	private String companyType;

	@Column(name = "LAST_TRANS_DATE")
	private Date lastTransDate;

	@Column(name = "COMPANY_REGISTER_NBR",columnDefinition = "varchar(30)")
	private String companyRegisterNBR;

	@Column(name = "COMPANY_VAT_NBR",columnDefinition = "varchar(30)")
	private String companyVatNBR;

	@Column(name = "ENTITY_ADDRESS_ID")
	private Integer entityAddressId;

	@Column(name = "ASSOCIATED_PAYMENT",columnDefinition = "CHAR(1)")
	private Character associatedPayment;

	@Column(name = "PAYMENT_METHOD",columnDefinition = "varchar(12)")
	private String paymentMethod;

	@Column(name = "PAYMENT_FREQUENCY",columnDefinition = "varchar(12)")
	private String paymentFrequency;

	@Column(name = "ADD_VALUE_DATE_DAYS")
	private Integer addValueDateDays;

	@Column(name = "DEF_ACCOUNT_NUMBER",columnDefinition = "varchar(30)")
	private String defAccountNumber;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "DEF_SETTLEMENT_CURRENCY", referencedColumnName = "CURRENCY_CODE")
	private Currency defaultSettlementCurrency;

	@Column(name = "DEF_IBAN",columnDefinition = "varchar(30)")
	private String defIBAN;

	@Column(name = "STATEMENT_TYPE",columnDefinition = "CHAR(1)")
	private Character statementType;

	@Column(name = "ESTATEMENT_TO_ENTITY",columnDefinition = "CHAR(1)")
	private Character eStatementToEntity;

	@Column(name = "user_create",columnDefinition = "varchar(40)")
	private String userCreate;

	@Column(name = "date_create")
	private Date dateCreate;

	@Column(name = "status",columnDefinition = "CHAR(1)")
	private Character status;

	@Column(name = "ACCT_TEMPLATE_HDR_ID")
	private Integer acctTemplateHdrId;

	@Column(name = "is_cloned")
	private Byte isCloned =0;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
    public String getParentEntityId() {
        return parentIdEntity != null ? parentIdEntity.getEntityId() : null;
    }
}



