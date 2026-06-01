package com.mdsl.model.entity.keys;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleApiAccessId implements Serializable {
    private Integer roleId;
    private Integer apiId;
}