package com.mdsl.model.dto.response;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mdsl.utils.ResponseCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Data
public class TerminalTypesDto {
	
	private int terminalTypesId;
	
	@Size(min = 1, max = 10, message = ResponseCode.CFG_INVALID_TERMINAL_TYPE)
	private String terminalType;
	
	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_MAKE_NAME)
	private String makeName;
	
	@Size(min = 0, max = 30, message = ResponseCode.CFG_INVALID_MAKE_MODEL)
	private String makeModel;
	
	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_POSCAPABILITY)
	private String posCapability;
	
	@Size(min = 0, max = 100, message = ResponseCode.CFG_INVALID_FREETEXT)
	private String freeText;
	
	@NotBlank(message = ResponseCode.CFG_INVALID_STATUS)
	@Size(min = 0, max = 1, message = ResponseCode.CFG_INVALID_STATUS)
	@Pattern(regexp = "[0-1]", message = ResponseCode.CFG_INVALID_STATUS)
	private String status;
}