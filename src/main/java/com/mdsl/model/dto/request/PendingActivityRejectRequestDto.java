package com.mdsl.model.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingActivityRejectRequestDto {
    @NotBlank(message = "Rejection note is required")
    private String note;
}
