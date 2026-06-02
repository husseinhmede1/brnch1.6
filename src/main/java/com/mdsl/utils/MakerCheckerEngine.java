package com.mdsl.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdsl.model.entity.Api;
import com.mdsl.model.entity.PendingActivity;
import com.mdsl.model.entity.User;
import com.mdsl.repository.PendingActivityRepository;
import com.mdsl.service.UserDetailsImpl;
import com.mdsl.utils.enumerations.ActivityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class MakerCheckerEngine {

    private final PendingActivityRepository pendingRepository;
    private final ObjectMapper objectMapper;

    public boolean processIfRequired(Object payload, String clazz, String method, String notes) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }

        HttpServletRequest request = attributes.getRequest();
        Api matchedApi = (Api) request.getAttribute(CommonConstants.MAKER_CHECKER_API_ENTITY);
        if (matchedApi != null && "0".equals(matchedApi.getStp())) {
            try {
                UserDetailsImpl currentMaker = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                String serializedPayload = objectMapper.writeValueAsString(payload);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                User user = new User();
                user.setUserId(currentMaker.getId());

                PendingActivity pendingActivity = PendingActivity.builder()
                        .api(matchedApi)
                        .status(ActivityStatus.PENDING)
                        .notes(notes)
                        .institution(String.valueOf(matchedApi.getInstitution()))
                        .clazz(clazz)
                        .method(method)
                        .payload(serializedPayload)
                        .createdBy(user)
                        .createdDate(now)
                        .updatedDate(null)
                        .updatedBy(null)
                        .build();

                pendingRepository.save(pendingActivity);
                return true;

            } catch (Exception e) {
                throw new RuntimeException("Error processing maker-checker serialization flow", e);
            }
        }

        return false;
    }
}