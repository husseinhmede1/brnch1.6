package com.mdsl.utils.enumerations;

import java.time.format.DateTimeFormatter;

public enum DateFormatEnum {

    DD_MM_YYYY("dd/MM/yyyy");

    private final DateTimeFormatter formatter;

    DateFormatEnum(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
