package com.mdsl.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
	private Integer userId;
	private String username; 
	private String firstName; 
	private String lastName;
	private String email;
	private String instId; 
	private String instName;
	private char status; 
}
