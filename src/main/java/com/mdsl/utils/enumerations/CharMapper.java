package com.mdsl.utils.enumerations;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Named("CharMapper")
public class CharMapper {

    @Named("stringToChar")
    public Character stringToChar(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.charAt(0);
    }
}
