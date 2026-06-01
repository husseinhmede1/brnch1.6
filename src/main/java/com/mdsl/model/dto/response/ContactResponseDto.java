package com.mdsl.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContactResponseDto {
	private int contactId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String professionalTitle;
	private char receiveEstatement;
}
