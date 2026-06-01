package com.mdsl.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
public class PaginatedRequestDto{
	@Min(value=0, message=ResponseCode.CFG_INVALID_OFFSET)
	@Max(value=999999, message=ResponseCode.CFG_INVALID_OFFSET)
	int offset;
	
	@Min(value=0, message=ResponseCode.CFG_INVALID_PAGE_SIZE)
	@Max(value=100, message=ResponseCode.CFG_INVALID_PAGE_SIZE)
	int pageSize;
	
	@Pattern(regexp="^[a-zA-Z_ ]*$", message=ResponseCode.CFG_INVALID_SORT_BY)
	String sortBy;
	
	@NotEmpty(message=ResponseCode.CFG_INVALID_ASC)
	@Pattern(regexp="^[true|false]*$", message=ResponseCode.CFG_INVALID_ASC)
	String asc;
}