package com.mdsl.model.dto.response;

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
public class AddressResponseDto {
	private int addressId;
	private String institutionId;
	private String entityId;
	private int cntryId;
	private int cityId;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String postalCodeZip;
	private String geoCode;
	private String phone1;
	private String phoneExternal;
	private String phone2;
	private String mobile1;
	private String fax;
	private String emailAddress1;
	private String emailAddress2;
	private String url;
	private String mobile2;
	private String freeText1;
	private String freeText2;
}