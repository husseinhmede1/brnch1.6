package com.mdsl.model.dto.response;

import com.mdsl.utils.enumerations.SortOrderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortDTO {
	 private String column;
	 private SortOrderEnum sortOrder = SortOrderEnum.ASC;

	  public String getColumn() {
	    if (null != column) return column.toUpperCase();
	    return column;
	  }
}