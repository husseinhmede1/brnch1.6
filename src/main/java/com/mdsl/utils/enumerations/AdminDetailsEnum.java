package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum AdminDetailsEnum {
    USERNAME("systemadmin@mas"),
    ROLE("Administrator");

    private final String value;

    AdminDetailsEnum(String value) {
        this.value = value;
    }
}
