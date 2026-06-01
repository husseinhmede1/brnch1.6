package com.mdsl.utils.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChargeTypemasterEnum {
    ONE_TIME("O"),
    Recurring("R");
    private final String code;
}
