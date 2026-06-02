package com.mdsl.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteNonActivityPackageRequestDto {
    private String id;
    private String instId;
    private String remoteAddress;
}
