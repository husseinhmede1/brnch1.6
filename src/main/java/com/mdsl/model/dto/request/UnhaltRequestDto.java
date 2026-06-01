package com.mdsl.model.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnhaltRequestDto {
	private String institutionId;
	private List<Integer> acquiringTransactionIds;
}