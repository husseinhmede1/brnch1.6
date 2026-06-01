package com.mdsl.model.dto.request;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.*;

import com.mdsl.model.dto.response.SortDTO;
import com.mdsl.model.entity.Institution;
import com.mdsl.utils.ResponseCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalRequestDto {

	@Size(min = 0, max = 10, message = ResponseCode.CFG_INVALID_TERMINAL_ID)
	@Size(min = 0, max = 12, message = ResponseCode.CFG_INVALID_TERMINAL_ID)
	private String terminalId;

	@NotNull
	@NotNull(message = ResponseCode.CFG_INVALID_TERMINAL_TYPE_ID)
	private int terminalTypeId;

	private char eCommerceFlag;

	private String actualStartDate;

	private String terminationDate;

	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_SERIAL_NUMBER)
	private String serialNumber;

	@NotEmpty
	@NotBlank(message = ResponseCode.CFG_INVALID_INST)
	@Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_INST)
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = ResponseCode.CFG_INVALID_INST)
	private String institutionId;

	@NotNull
	@Size(min = 1, max = 3, message = ResponseCode.CFG_INVALID_CURRENCY)
	@Pattern(regexp = "^[0-9]*$", message = ResponseCode.CFG_INVALID_CURRENCY)
	private String currencyId;

	@NotNull
	@NotNull(message=ResponseCode.CFG_INVALID_MCC)
	@Size(min = 1, max = 5, message = ResponseCode.CFG_INVALID_MCC)
	@Pattern(regexp = "^[0-9]*$", message = ResponseCode.CFG_INVALID_CURRENCY)
	private String mccId;

	@Pattern(regexp = "[0-1]", message = ResponseCode.CFG_INVALID_STATUS)
	private String status;

	@NotEmpty
	@NotEmpty(message = ResponseCode.INVALID_ENTITY_ID)
	@Size(max = 30, message = ResponseCode.INVALID_ENTITY_ID)
	private String entityId;

	private List<SortDTO> sort;

	private String sortOrder = "";

	private Integer pageNo = 0;

	private Integer pageSize = 20;
	
	private char updateFlag;

 	public List<SortDTO> getSort() {
		if (this.sort == null || this.sort.isEmpty()) {
			return new ArrayList<SortDTO>();
		}
		return this.sort;
	}
}
