package com.mdsl.model.mapper;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.ContactRequestDto;
import com.mdsl.model.dto.response.ContactResponseDto;
import com.mdsl.model.entity.Address;
import com.mdsl.model.entity.Contact;

@Mapper
public interface ContactMapper {
	ContactResponseDto toDto (Contact contact);
	Contact toEntityContact(ContactRequestDto contactRequestDto);
	Address toEntityAddress(ContactRequestDto contactRequestDto);

	@Mapping(source="institution.institutionId",target="institutionId")
	@Mapping(source="entityObject.entityId",target="entityId")
	@Mapping(source="address.addressId",target="addressId")
	@Mapping(source="address.cntryCode.cntryId",target="cntryId")
	@Mapping(source="address.cityCode.cityId",target="cityId")
	@Mapping(source="address.address1",target="address1")
	@Mapping(source="address.address2",target="address2")
	@Mapping(source="address.address3",target="address3")
	@Mapping(source="address.address4",target="address4")
	@Mapping(source="address.postalCodeZip",target="postalCodeZip")
	@Mapping(source="address.geoCode",target="geoCode")
	@Mapping(source="address.phone1",target="phone1")
	@Mapping(source="address.phoneExternal",target="phoneExternal")
	@Mapping(source="address.phone2",target="phone2")
	@Mapping(source="address.mobile1",target="mobile1")
	@Mapping(source="address.fax",target="fax")
	@Mapping(source="address.emailAddress1",target="emailAddress1")
	@Mapping(source="address.emailAddress2",target="emailAddress2")
	@Mapping(source="address.url",target="url")
	@Mapping(source="address.mobile2",target="mobile2")
	@Mapping(source="address.freeText1",target="freeText1")
	@Mapping(source="address.freeText2",target="freeText2")
	ContactRequestDto toreqDto (Contact contact);
}
