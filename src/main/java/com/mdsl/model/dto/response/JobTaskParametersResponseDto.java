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
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
public class JobTaskParametersResponseDto {

    private Integer jobTaskParamId;	
    private Integer jobTaskId;
    private Integer parametersServiceId;
    private Integer parameterId;
    private String parameterValue;
    private String parameterName;
    private String isMandatory;
}