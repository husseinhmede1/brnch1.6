package com.mdsl.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateParseUtil {

    public static Date parseDate(String dateStr,DateTimeFormatter dateFormat) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            LocalDate localDate = LocalDate.parse(dateStr.trim(), dateFormat);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(
                    "Invalid date format,"+dateFormat+", value : "+dateStr
            );
        }
    }
}
