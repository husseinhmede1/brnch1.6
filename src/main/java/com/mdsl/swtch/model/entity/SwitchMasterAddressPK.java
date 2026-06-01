package com.mdsl.swtch.model.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchMasterAddressPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer addressId;
	private String institutionId;
}
