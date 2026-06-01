package com.mdsl.utils.enumerations;

public enum SortingEnum {
    DESCENDING("false"),
    ASCENDING("true");

    private final String value;

    SortingEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}