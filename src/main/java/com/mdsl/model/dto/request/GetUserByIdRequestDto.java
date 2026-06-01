package com.mdsl.model.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByIdRequestDto {
    private Integer userId;
    private Boolean isUserProfile;
}