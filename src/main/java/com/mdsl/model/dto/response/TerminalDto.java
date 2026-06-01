package com.mdsl.model.dto.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.model.entity.Institution;
import com.mdsl.utils.ResponseCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalDto {

	private int terminalId;
 	private int terminalTypeId;
	@Size(min = 0, max = 1, message = ResponseCode.CFG_INVALID_STATUS)
 	private char eCommerceFlag;

	private Date actualStartDate;

	private Date terminationDate;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_SERIAL_NUMBER)
	private String serialNumber;

	@NotNull
	private int institutionId;


	private int currencyId;

	private int mccId;

	@Size(min = 0, max = 1, message = ResponseCode.CFG_INVALID_STATUS)
	@Pattern(regexp = "[0-1]", message = ResponseCode.CFG_INVALID_STATUS)
	private char status;
	
	@NotNull
	private int entityId;
	
	
	

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(int terminalId) {
		this.terminalId = terminalId;
	}

	

	public int getTerminalTypeId() {
		return terminalTypeId;
	}

	public void setTerminalTypeId(int terminalTypeId) {
		this.terminalTypeId = terminalTypeId;
	}

	public char geteCommerceFlag() {
		return eCommerceFlag;
	}

	public void seteCommerceFlag(char eCommerceFlag) {
		this.eCommerceFlag = eCommerceFlag;
	}	

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate){
		try {
			this.actualStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(actualStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Date getTerminationDate(){
		return terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		try {
			this.terminationDate = new SimpleDateFormat("dd/MM/yyyy").parse(terminationDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public int getMccId() {
		return mccId;
	}

	public void setMccId(int mccId) {
		this.mccId = mccId;
	}

	

}
