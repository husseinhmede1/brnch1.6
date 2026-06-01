package com.mdsl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class JwtResponseDto {
	private String token;
	private String refreshToken;
	private String type;
	private UserNewResponseDto user;

	public JwtResponseDto(String accessToken, UserNewResponseDto User) {
		this.token = accessToken;
		this.user = User;
	}
}