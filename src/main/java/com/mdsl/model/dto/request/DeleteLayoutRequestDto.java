package com.mdsl.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteLayoutRequestDto {

    private int layoutId;
    private String remoteAddress;
    private String instId;

}
