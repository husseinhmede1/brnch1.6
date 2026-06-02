package com.mdsl.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteJobRequestDto {

    private int jobId;
    private String instId;
    private String remoteAddress;

}
