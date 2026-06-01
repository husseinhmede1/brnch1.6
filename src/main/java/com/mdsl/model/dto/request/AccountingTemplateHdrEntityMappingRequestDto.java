package com.mdsl.model.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class AccountingTemplateHdrEntityMappingRequestDto {
	private int id;
	private boolean previouslyAssigned;
    private List<String> entities;
}