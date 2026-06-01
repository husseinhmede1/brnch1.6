package com.mdsl.utils;

import com.mdsl.utils.enumerations.DateFormatEnum;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Named("DateMapper")
public class DateMapper {

    @Named("stringToDate")
    public Date stringToDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return DateParseUtil.parseDate(date, DateFormatEnum.DD_MM_YYYY.getFormatter());
    }
}
