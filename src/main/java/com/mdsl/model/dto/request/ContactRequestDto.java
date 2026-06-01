package com.mdsl.model.dto.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContactRequestDto {

	@NotNull(message = ResponseCode.CNT_INVALID_CONTACT_ID)
	@Digits(integer = 10, fraction = 0, message = ResponseCode.CNT_INVALID_CONTACT_ID)
	private int contactId;
	
	@NotEmpty(message = ResponseCode.CNT_INVALID_FIRST_NAME)
	@Size(max = 25, message = ResponseCode.CNT_INVALID_FIRST_NAME)
	private String firstName;
	
	@NotEmpty(message = ResponseCode.CNT_INVALID_MIDDLE_NAME)
	@Size(max = 20, message = ResponseCode.CNT_INVALID_MIDDLE_NAME)
	private String middleName;
	
	@NotEmpty(message = ResponseCode.CNT_INVALID_LAST_NAME)
	@Size(max = 20, message = ResponseCode.CNT_INVALID_LAST_NAME)
	private String lastName;
	
	@Size(max = 50, message = ResponseCode.CNT_INVALID_PROF_TITLE)
	private String professionalTitle;
	
	@Size(max = 10, message = ResponseCode.CNT_INVALID_PHONE)
	private String phone;
	
	private char receiveEstatement;

	@Digits(integer = 10, fraction = 0, message = ResponseCode.CNT_INVALID_ADDRESS_ID)
	private int addressId;
	
	@NotEmpty(message = ResponseCode.CFG_INVALID_INSTITUTION_ID)
	private String institutionId;
	
	@Size(max = 30, message = ResponseCode.CFG_INVALID_ENTITY_ID)
	private String entityId;
	
//	@Size(max = 3, message = ResponseCode.INVALID_CNTRY_CODE)
	private int cntryId;
	
//	@Size(max = 3, message = ResponseCode.INVALID_CITY_CODE)
	private int cityId;
	
	@Size(max = 50, message = ResponseCode.CNT_INVALID_ADDRESS)
	private String address1;
	@Size(max = 50, message = ResponseCode.CNT_INVALID_ADDRESS)
	private String address2;
	@Size(max = 50, message = ResponseCode.CNT_INVALID_ADDRESS)
	private String address3;
	@Size(max = 50, message = ResponseCode.CNT_INVALID_ADDRESS)
	private String address4;
	@Size(max = 20, message = ResponseCode.CNT_INVALID_POSTAL_CODE)
	private String postalCodeZip;
	@Size(max = 16, message = ResponseCode.CNT_INVALID_GEOCODE)
	private String geoCode;
	@Size(min = 1, max = 20, message = ResponseCode.CNT_INVALID_PHONE)
	private String phone1;
	@Size(max = 10, message = ResponseCode.CNT_INVALID_PHONE)
	private String phoneExternal;
	@Size(max = 20, message = ResponseCode.CNT_INVALID_PHONE)
	private String phone2;
	@Size(min = 1,max = 20, message = ResponseCode.CNT_INVALID_PHONE)
	private String mobile1;
	@Size(max = 30, message = ResponseCode.CNT_INVALID_FAX)
	private String fax;
	@Size(max = 100, message = ResponseCode.CNT_INVALID_EMAIL)
	private String emailAddress1;
	@Size(max = 100, message = ResponseCode.CNT_INVALID_EMAIL)
	private String emailAddress2;
	@Size(max = 255, message = ResponseCode.CNT_INVALID_URL)
	private String url;
	@Size(max = 20, message = ResponseCode.CNT_INVALID_PHONE)
	private String mobile2;
	@Size(max = 100, message = ResponseCode.CNT_INVALID_FREE_TXT)
	private String freeText1;
	@Size(max = 100, message = ResponseCode.CNT_INVALID_FREE_TXT)
	private String freeText2;

	
	
}
