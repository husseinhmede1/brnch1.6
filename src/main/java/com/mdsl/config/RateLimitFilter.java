package com.mdsl.config;

import com.mdsl.framework.DatabaseMessageSource;
import com.mdsl.model.entity.BlockedIp;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.model.entity.User;
import com.mdsl.repository.BlockedIpRepository;
import com.mdsl.repository.SystemCodeRepository;
import com.mdsl.repository.UserRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.CodePrefixEnum;
import com.mdsl.utils.enumerations.CodeSuffixEnum;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import com.mdsl.utils.enumerations.StatusEnum;
import com.mdsl.utils.enumerations.UserStatusEnum;
import java.time.LocalDateTime;


import lombok.RequiredArgsConstructor;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    private final BlockedIpRepository blockedIpRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final DatabaseMessageSource databaseMessageSource;
    private final SystemCodeRepository systemCodeRepository;


    private final Map<String, Deque<Long>> requestHistory = new ConcurrentHashMap<>();

    private long getCapacity(String institutionId) {

        Optional<SystemCode> capacity =
                systemCodeRepository
                        .findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(
                                CodePrefixEnum.RATE_LIMIT.getValue(),
                                CodeSuffixEnum.CAPACITY.getValue(),
                                institutionId,
                                StatusEnum.ENABLED.getValue()
                        );

        return capacity
                .map(systemCode ->
                        Long.parseLong(systemCode.getCodeValue()))
                .orElse(0L);
    }

    private long getDuration(String institutionId) {

        Optional<SystemCode> duration =
                systemCodeRepository
                        .findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(
                                CodePrefixEnum.RATE_LIMIT.getValue(),
                                CodeSuffixEnum.DURATION_SEC.getValue(),
                                institutionId,
                                StatusEnum.ENABLED.getValue()
                        );

        return duration
                .map(systemCode ->
                        Long.parseLong(systemCode.getCodeValue()))
                .orElse(0L);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Locale locale = request.getLocale();

        String ip = request.getRemoteAddr();

        /*
         * GET INSTITUTION ID FROM HEADER
         */
        final String institutionId = request.getHeader("instId");

        /*
         * BLOCK IF IP EXISTS
         */
        if (blockedIpRepository.existsByIpAddress(ip)) {
            List<String> responseMessage =databaseMessageSource.getMessageFromKey( ResponseCode.IP_BLOCKED, locale, false);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            if (!responseMessage.isEmpty()) {
                response.getWriter().write(
                        "{ \"message\": \"" + responseMessage.get(0) + "\" }"
                );
            }
            response.getWriter().flush();
            return;
        }

        /*
         * BLOCK IF USER STATUS = 3
         */
        String authorizationHeader = request.getHeader("Authorization");
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                if (jwtUtils.validateJwtToken(token)) {
                    String userIdClaim = jwtUtils.getClaimFromToken( token,
                                    claims -> claims.get("userId", String.class));

                    if (userIdClaim != null) {
                        Integer userId = Integer.valueOf(userIdClaim);
                        User user = userRepository.findById(userId).orElse(null);
                        if (user != null && user.getStatus() == UserStatusEnum.BLOCKED.getValue()) {
                            List<String> responseMessage = databaseMessageSource.getMessageFromKey( ResponseCode.USER_BLOCKED, locale,false);
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            if (!responseMessage.isEmpty()) {
                                response.getWriter().write("{ \"message\": \"" +responseMessage.get(0) +"\" }"
                                );
                            }
                            response.getWriter().flush();
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * UNIQUE KEY = IP + INSTITUTION
         */
        String rateLimitKey = ip;

        long capacityValue = getCapacity(institutionId);
        long durationValue = getDuration(institutionId);

        if (capacityValue <= 0 || durationValue <= 0) {
            filterChain.doFilter(request, response);
            return;
        }

        Deque<Long> timestamps =
                requestHistory.computeIfAbsent(
                        rateLimitKey,
                        key -> new ConcurrentLinkedDeque<>()
                );

        long now = System.currentTimeMillis();

        long windowStart =
                now - Duration.ofSeconds(durationValue).toMillis();

        /*
         * REMOVE REQUESTS OUTSIDE WINDOW
         */
        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
                timestamps.pollFirst();
            }
            if (timestamps.size() < capacityValue) {
                timestamps.addLast(now);
                response.addHeader(
                        "X-Rate-Limit-Remaining",
                        String.valueOf(capacityValue - timestamps.size())
                );
                filterChain.doFilter(request, response);
                return;
            }
        }
        /*
         * STRICT RATE LIMIT
         */
        if (timestamps.size() < capacityValue) {
            timestamps.addLast(now);
            response.addHeader(
                    "X-Rate-Limit-Remaining",
                    String.valueOf(
                            capacityValue - timestamps.size()
                    )
            );

            filterChain.doFilter(request, response);

        } else {
            if (!blockedIpRepository.existsByIpAddress(ip)) {
                Integer userId = null;
                try {
                    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                        String token = authorizationHeader.substring(7);

                        if (jwtUtils.validateJwtToken(token)) {
                            String userIdClaim =jwtUtils.getClaimFromToken(token,claims -> claims.get("userId",String.class));
                            if (userIdClaim != null) {
                                userId = Integer.valueOf(userIdClaim);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                BlockedIp blockedIp = new BlockedIp();
                blockedIp.setIpAddress(ip);
                blockedIp.setBlockedAt(LocalDateTime.now());
                blockedIpRepository.save(blockedIp);

                if (userId != null) {
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        user.setStatus(UserStatusEnum.BLOCKED.getValue());
                        userRepository.save(user);
                    }
                }
            }
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            List<String> responseMessage =databaseMessageSource.getMessageFromKey(ResponseCode.TOO_MANY_REQUESTS,locale,false);
            if(!responseMessage.isEmpty()){
                response.getWriter().write("{ \"message\": \"" +responseMessage.get(0) +"\" }");
            }
            response.getWriter().flush();
            return;
        }
    }
}