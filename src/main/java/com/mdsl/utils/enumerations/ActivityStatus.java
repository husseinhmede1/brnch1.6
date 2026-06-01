package com.mdsl.utils.enumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActivityStatus {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED");

    @Getter
    private final String name;
}
