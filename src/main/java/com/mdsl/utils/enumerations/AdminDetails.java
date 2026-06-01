package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum AdminDetails {
    USERNAME("systemadmin@mas"),
    ROLE("Administrator");

    private final String value;

    AdminDetails(String value) {
        this.value = value;
    }
}
