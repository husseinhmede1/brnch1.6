package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum HaltTypes {
    PERMANENT ("P"),
    UNTIL_NEXT_EOD("E");

    private final String value;

    HaltTypes(String value) {
        this.value = value;
    }
}
