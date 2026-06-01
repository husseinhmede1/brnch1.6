package com.mdsl.utils;

import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.dto.request.PaginatedRequestDto;
import com.mdsl.model.dto.request.PaginationRequestDto;
import com.mdsl.model.entity.User;
import com.mdsl.service.UserDetailsImpl;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.DateFormatEnum;
import com.mdsl.utils.enumerations.SortingEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class CommonServices {

    public Optional<Long> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetailsImpl) {

            UserDetailsImpl userDetails = (UserDetailsImpl) principal;

            return Optional.of(Long.parseLong(userDetails.getId()+""));
        }

        return Optional.empty();
    }


    public Optional<Integer> getLoggedInUserInt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetailsImpl) {

            UserDetailsImpl userDetails = (UserDetailsImpl) principal;

            return Optional.of(Integer.parseInt(userDetails.getId()+""));
        }

        return Optional.empty();
    }

    public Long generateUUID() {
        UUID uuid = UUID.randomUUID();
        long seed = Math.abs(uuid.getMostSignificantBits());

        // Restrict to 8 digits
        long eightDigitId = seed % 100_000_000L;

        // Ensure it's not shorter than 8 digits (optional)
        if (eightDigitId < 10_000_000L) {
            eightDigitId += 10_000_000L;
        }
        return eightDigitId;
    }
}