package com.mdsl.model.dto.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityRequestDto {
	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	@NotEmpty(message = ResponseCode.CFG_INVALID_ENTITY_ID)
	@Size(max = 30, message = ResponseCode.CFG_INVALID_ENTITY_ID)
	private String entityId;
	@Size(max = 30, message = ResponseCode.ENT_INVALID_ENTITY_PARENT_ID)
	private String parentId;
	@NotNull(message = ResponseCode.CFG_INVALID_BUSINESS_TYPE)
	
	private int businessTypeId;
	@NotEmpty
	@Size(max = 50, message = ResponseCode.ENT_INVALID_ENTITY_NAME)
	private String entityName;
//	@NotEmpty
	@Size(max = 100, message = ResponseCode.ENT_INVALID_ENTITY_NAME_ALT)
	private String entityNameAlt;
//	@NotEmpty
	@Size(max = 50, message = ResponseCode.ENT_INVALID_DBA_NAME)
	private String dbaName;
//	@NotEmpty
	@Size(max = 100, message = ResponseCode.ENT_INVALID_DBA_NAME_ALT)
	private String dbaNameAlt;
	@NotNull(message = ResponseCode.ENT_INVALID_DEFAULT_MCC_ID)
	private int mccId;
	@NotNull(message = ResponseCode.ENT_INVALID_ENTITYLEVELS_CODE)
	private int entityLevelId;
	@Pattern(regexp = "[Y|N]", message = ResponseCode.ENT_INVALID_ON_HOLD_ID)
	private String onHoldInd;
	@Pattern(regexp = "[Y|N]", message = ResponseCode.ENT_INVALID_HOT_MERCHANTFLAD_ID)
	private String hotMerchantFlag;
	private Integer activityPackageId;
	private String nonActivityPackageId;
	@Size(max = 30, message = ResponseCode.ENT_INVALID_COMPANY_REG_NUM)
	private String companyRegisterNBR;
	@Size(max = 30, message = ResponseCode.ENT_INVALID_COMPANY_VAT_NUM)
	private String companyVatNBR;
	private int bankCodeId;
	@Size(max = 30, message = ResponseCode.ENT_INVALID_DEF_ACC_NUM)
	private String defAccountNumber;
	@Size(max = 30, message = ResponseCode.ENT_INVALID_DEF_IBAN)
	private String defIBAN;
	@NotNull
//	@Size(max = 3, message = ResponseCode.INVALID_DEF_SETTLEMENT_CUR)
	private int defSettlementCurrency;
	private Date contractDate;
	private Date expStartDate;
	private Date actualStartDate;
	private Date terminationDate;
	private Date lastTransDate;
	@Pattern(regexp = "[D|T|P]", message = ResponseCode.ENT_INVALID_ENTITY_STATUS)
	private String entityStatus;
	@Pattern(regexp = "[Y|N]", message = ResponseCode.ENT_INVALID_ASSOCIATED_PAYMENT)
	private String associatedPayment;
	@Pattern(regexp = "[T|C]", message = ResponseCode.ENT_INVALID_PAYMENT_METHOD)
	private String paymentMethod;
	@Pattern(regexp = "[D|W|M]", message = ResponseCode.ENT_INVALID_PAYMENT_FREQUENCY)
	private String paymentFrequency;
	@Pattern(regexp = "[P|B|E]", message = ResponseCode.ENT_INVALID_STATEMENT_TYPE)
	private String statementType;
	private int addValueDateDays;
	private int salesmanId;
	private int employeeInchargeId;
	@Pattern(regexp = "[Y|N]", message = ResponseCode.ENT_INVALID_ESTATEMENT_TO_ENTITY_ID)
	private String eStatementToEntity;
	private String search;
	private String fromDate;
	private String toDate;
	private String entityLevel;

	private List<SortDTO> sort;

	private String sortOrder = "";

	private Integer pageNo = 0;

	private Integer pageSize = 20;

	private char status;
	
	private Byte isCloned; 
	
	private Integer acctTemplateHdrId;
	
	private char updateFlag; 
	
	


	public void setContractDate(String contractDate) {
		if (contractDate.isEmpty()) {
			this.contractDate = null;
		} else {
			try {
				this.contractDate = new SimpleDateFormat("dd/MM/yyyy").parse(contractDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public void setExpStartDate(String expStartDate) {
		if (expStartDate.isEmpty()) {
			this.expStartDate = null;
		} else {
			try {
				this.expStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(expStartDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public void setActualStartDate(String actualStartDate) {
		if (actualStartDate.isEmpty()) {
			this.actualStartDate = null;
		} else {
			try {
				this.actualStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(actualStartDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public void setTerminationDate(String terminationDate) {
		if (terminationDate.isEmpty()) {
			this.terminationDate = null;
		} else {
			try {
				this.terminationDate = new SimpleDateFormat("dd/MM/yyyy").parse(terminationDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public void setLastTransDate(String lastTransDate) {
		if (lastTransDate.isEmpty()) {
			this.lastTransDate = null;
		} else {
			try {
				this.lastTransDate = new SimpleDateFormat("dd/MM/yyyy").parse(lastTransDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}


	public List<SortDTO> getSort() {
		if (this.sort == null || this.sort.isEmpty()) {
			return new ArrayList<>();
		}
		return this.sort;
	}


	
	
	


}
