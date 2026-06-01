package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecificSortPaginationRequestDto{

	@Min(value=0, message=ResponseCode.INVALID_OFFSET)
	@Max(value=999999, message=ResponseCode.INVALID_OFFSET)
	int offset;
		
	@Min(value=0, message=ResponseCode.INVALID_PAGE_SIZE)
	@Max(value=100, message=ResponseCode.INVALID_PAGE_SIZE)
	int pageSize;
}