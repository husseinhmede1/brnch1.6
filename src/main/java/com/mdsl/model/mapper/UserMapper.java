package com.mdsl.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdsl.model.dto.request.UserRequestDto;
import com.mdsl.model.dto.response.UserInfoResponseDto;
import com.mdsl.model.dto.response.UserNewResponseDto;
import com.mdsl.model.dto.response.UserResponseDto;
import com.mdsl.model.entity.User;

@Mapper
public interface UserMapper {

	@Mapping(source = "institution", target = "institution")
	@Mapping(source = "institution1.institutionId", target = "defaultInstitutionId")
	@Mapping(source = "institution1.institutionName", target = "defaultInstitutionName")
	@Mapping(source = "preferedLanguage.systemCodeId", target = "preferedLanguageSystemCodeId")
	@Mapping(source = "preferedLanguage.codeSuffix", target = "preferedLanguageCodeSuffix")
	@Mapping(source = "preferedLanguage.codePrefix", target = "preferedLanguageCodePrefix")
	@Mapping(source = "preferedLanguage.codeValue", target = "preferedLanguageCodeValue")
	@Mapping(source = "preferedLanguage.description", target = "preferedLanguageCodeDescription")
	UserResponseDto toDto(User user);
	
	@Mapping(source = "institution", target = "institution")
	@Mapping(source = "institution1.institutionId", target = "defaultInstitutionId")
	@Mapping(source = "institution1.institutionName", target = "defaultInstitutionName")
	@Mapping(source = "preferedLanguage.systemCodeId", target = "preferedLanguageSystemCodeId")
	@Mapping(source = "preferedLanguage.codeSuffix", target = "preferedLanguageCodeSuffix")
	@Mapping(source = "preferedLanguage.codePrefix", target = "preferedLanguageCodePrefix")
	@Mapping(source = "preferedLanguage.codeValue", target = "preferedLanguageCodeValue")
	@Mapping(source = "preferedLanguage.description", target = "preferedLanguageCodeDescription")
	UserNewResponseDto toNewResponseDto(User user);

	@Mapping(source = "institution1.institutionId", target = "instId")
	@Mapping(source = "institution1.institutionName", target = "instName")
	UserInfoResponseDto toDtoInfo(User user);

	@Mapping(source = "defaultInstitutionId", target = "institution1.institutionId")
	@Mapping(source = "preferedLanguage", target = "preferedLanguage.systemCodeId")
//	@Mapping(source = "institution1.institutionName", target = "defaultInstitutionName")
	User toEntity(UserRequestDto userRequestDto);
}
