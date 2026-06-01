package com.mdsl.swtch.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchMasterAddressRequestDto {
	private String address1;
	private String address2;
	private String city;
	private String institutionId;
	private String cntryCode;
	private String entityId;
	private String address3;
	private String address4;
	private String country;
	private String phone1;
	private String phone2;
	private String geocode;
	private String fax;
	private String emailAddress;
	private String url;
}
