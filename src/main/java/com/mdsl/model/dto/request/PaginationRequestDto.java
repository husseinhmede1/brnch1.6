package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDto{
	@Min(value=0, message=ResponseCode.INVALID_OFFSET)
	@Max(value=999999, message=ResponseCode.INVALID_OFFSET)
	int offset;
	
	@Min(value=0, message=ResponseCode.INVALID_PAGE_SIZE)
	@Max(value=100, message=ResponseCode.INVALID_PAGE_SIZE)
	int pageSize;
	
	@Pattern(regexp="^[a-zA-Z_ ]*$", message=ResponseCode.INVALID_SORT_BY)
	String sortBy;
	
	@NotEmpty(message=ResponseCode.INVALID_SORT_BY)
	@Pattern(regexp="^[true|false]*$", message=ResponseCode.INVALID_SORT_BY)
	String asc;
}