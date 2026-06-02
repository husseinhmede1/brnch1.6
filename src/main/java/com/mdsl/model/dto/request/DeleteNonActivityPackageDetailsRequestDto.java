package com.mdsl.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteNonActivityPackageDetailsRequestDto {
    private int id;
    private String instId;
    private String remoteAddress;
}
