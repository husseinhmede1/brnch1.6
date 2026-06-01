package com.mdsl.model.dto.request;
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
public class AddressRequestDto {
    @NotNull(message = ResponseCode.CNT_INVALID_ADDRESS_ID)
    private int addressId;
    @NotEmpty(message = ResponseCode.CFG_INVALID_INST_CODE)
    private String institutionId;
    @NotEmpty(message = ResponseCode.INVALID_ENTITY_ID)
    private String entityId;
    private int cntryId;
    private int cityId;
    @Size(max = 50, message = ResponseCode.INVALID_ADDRESS)
    private String address1;
    @Size(max = 50, message = ResponseCode.INVALID_ADDRESS)
    private String address2;
    @Size(max = 50, message = ResponseCode.INVALID_ADDRESS)
    private String address3;
    @Size(max = 50, message = ResponseCode.INVALID_ADDRESS)
    private String address4;
    @Size(max = 20, message = ResponseCode.INVALID_POSTAL_CODE)
    private String postalCodeZip;
    @Size(max = 16, message = ResponseCode.INVALID_GEO_CODE)
    private String geoCode;
    @Size(min=1, max = 20, message = ResponseCode.INVALID_PHONE)
    private String phone1;
    @Size(max = 10, message = ResponseCode.INVALID_PHONE)
    private String phoneExternal;
    @Size(max = 20, message = ResponseCode.INVALID_PHONE)
    private String phone2;
    @Size(min=1,max = 20, message = ResponseCode.INVALID_PHONE)
    private String mobile1;
    @Size(max = 30, message = ResponseCode.INVALID_FAX)
    private String fax;
    @Size(max = 100, message = ResponseCode.INVALID_EMAIL)
    private String emailAddress1;
    @Size(max = 100, message = ResponseCode.INVALID_EMAIL)
    private String emailAddress2;
    @Size(max = 255, message = ResponseCode.INVALID_URL)
    private String url;
    @Size(max = 20, message = ResponseCode.INVALID_PHONE)
    private String mobile2;
    @Size(max = 100, message = ResponseCode.INVALID_FREE_TEXT)
    private String freeText1;
    @Size(max = 100, message = ResponseCode.INVALID_FREE_TEXT)
    private String freeText2;
}

