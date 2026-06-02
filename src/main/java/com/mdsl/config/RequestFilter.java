package com.mdsl.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mdsl.model.dto.response.ErrorDto;
import com.mdsl.model.dto.response.SuccessfulResponseDto;
import com.mdsl.model.entity.UserAccess;
import com.mdsl.model.entity.UserRole;
import com.mdsl.repository.InstitutionRepository;
import com.mdsl.repository.UserAccessRepository;
import com.mdsl.service.*;
import com.mdsl.utils.CommonConstants;
import com.mdsl.utils.CustomContentCachingRequestWrapper;
import com.mdsl.utils.enumerations.StatusEnum;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdsl.model.dto.response.FilterErrorDto;
import com.mdsl.model.entity.Api;
import com.mdsl.repository.ApiRepository;
import com.mdsl.utils.ApiListUtis;
import com.mdsl.utils.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    private final JwtUtils jwtTokenUtil;
    private final ApiRepository apiRepository;
    private final ObjectMapper objectMapper;
    private final UserAccessRepository userAccessRepository;
    private final InstitutionRepository institutionRepository;
    private final RoleApiAccessService roleApiAccessService;

    private boolean corsFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                               FilterChain chain) throws IOException, ServletException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // IMPORTANT: Allowed all the domains
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, PATCH, DELETE, HEAD");
        httpServletResponse.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, "
                        + "Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, "
                        + "Authorization, instId");
        httpServletResponse.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Content-Disposition");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.addIntHeader("Access-Control-Max-Age", 3600);
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())
                || CorsUtils.isPreFlightRequest(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//			chain.doFilter(httpServletRequest, httpServletResponse);
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<UserAccess> userAccess;
        String username;
        String jwtToken;
        String storedJwt;
        String storedRefreshJwt;
        char userLevel;
        Integer userId;
        Claims claims;
        Optional<Api> api = null;

        String reqURL = request.getRequestURI();

        if (reqURL.contains("/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (corsFilter(request, response, filterChain)) {
            return;
        }

        String httpMethod = request.getMethod();
        logger.debug("@RequestFilter#doFilterInternal: httpMethod [{}]", httpMethod);

        logger.debug("@RequestFilter#doFilterInternal: Request URL[{}]", reqURL);

        if (!reqURL.contains("/help/") && !reqURL.contains("/about/")
                && !reqURL.contains("swagger") && !reqURL.contains("/authenticate") && !reqURL.contains("/refresh") && !reqURL.contains("/api-docs")) {
//            String origReqURL = getRequestURL(reqURL);
//            logger.debug("@RequestFilter#doFilterInternal: URL[{}]", origReqURL);

            api = apiRepository.findByApiUrlAndApiFunctionAndInstitution(reqURL, httpMethod.trim(), (Objects.isNull(request.getHeader("instId")) || request.getHeader("instId").equalsIgnoreCase("null") || request.getHeader("instId") == null) ? 1 : Integer.parseInt(request.getHeader("instId")));
            if (api.isEmpty()) {
                logger.debug("@RequestFilter#doFilterInternal: Invalid API");
                handleFailedAccess(response, ResponseCode.CFG_INVALID_API, HttpStatus.BAD_REQUEST, request);
                return;
            }
            request.setAttribute(CommonConstants.MAKER_CHECKER_API_ENTITY, api);
            api.ifPresent(value -> logger.debug("@RequestFilter#doFilterInternal: API ID[{}] - LoginRequired[{}] - RoleRequired[{}]", value.getApiId(), value.getLoginRequired(), value.getRoleRequired()));

            if (api.get().getLoginRequired() == '1') {
                logger.debug("Remote IP Address: {}", request.getRemoteAddr());

                final String requestTokenHeader = request.getHeader("Authorization");
                // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
                if (Objects.isNull(requestTokenHeader) || !requestTokenHeader.startsWith("Bearer ")) {
                    logger.warn("@RequestFilter#doFilterInternal: JWT Token does not begin with Bearer String");
                    handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.UNAUTHORIZED, request);
                    return;
                }

                //Retrieve the username from the token.
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUserNameFromJwtToken(jwtToken);
                } catch (ExpiredJwtException e) {
                    logger.error("@RequestFilter#doFilterInternal: " + e.getMessage());
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token expired\", \"message\": \"Please refresh your token\"}");
                    return;
                } catch (Exception e) {
                    logger.error("@RequestFilter#doFilterInternal: Unable to get JWT Token");
                    handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.UNAUTHORIZED, request);
                    return;
                }

                if (Objects.isNull(username) || !Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    handleFailedAccess(response, ResponseCode.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED, request);
                    return;
                }

                //Retrieve the user information and the user access from the DB
                claims = jwtTokenUtil.parseAndValidate(jwtToken);
                userLevel = claims.get("userLevel", String.class).charAt(0);
                userId = Integer.valueOf(claims.get("userId", String.class));

                UserDetailsImpl userDetails = new UserDetailsImpl(userId, username, null, userLevel, new ArrayList<>());
                userAccess = userAccessRepository.findByUserUserId(userId);
                if (userAccess.isEmpty()) {
                    handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.BAD_REQUEST, request);
                    return;
                }
                storedJwt = userAccess.get().getJwt().trim();
                storedRefreshJwt = userAccess.get().getRefreshJwt();

                //Validate that the received token in the request is equal to the token in the database for the specified user
                if (!storedJwt.equalsIgnoreCase(jwtToken.trim())) {
                    handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.UNAUTHORIZED, request);
                    return;
                }

                if (jwtTokenUtil.validateJwtToken(storedRefreshJwt)) {
                    if (jwtTokenUtil.validateJwtToken(jwtToken)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        request.setAttribute("userId", userDetails.getId());
                    } else {
                        handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.UNAUTHORIZED, request);
                        return;
                    }
                } else {
                    handleFailedAccess(response, ResponseCode.VAL_INVALID_ACCESS, HttpStatus.UNAUTHORIZED, request);
                    return;
                }

//                String header_InstId = request.getHeader("instId");
//                if (!NumberUtils.isCreatable(header_InstId)) {
//                    handleFailedAccess(response, ResponseCode.CFG_INVALID_INST, HttpStatus.BAD_REQUEST, request);
//                    return;
//                }
//
//                institution = institutionRepository.findByInstitutionId(header_InstId).orElse(null);
//                if (institution == null) {
//                    handleFailedAccess(response, ResponseCode.CFG_INVALID_INST, HttpStatus.BAD_REQUEST, request);
//                    return;
//                }

                //Validate if the user has access to the specified API based on the user roles
                //The view VW_USER_ROLES_ACCESS contains all the needed information
                //To check if a user has access we need to find a record in view VW_USER_ROLES_ACCESS
                //For the user id, api id and the access view/update/delete set to true depending on the HTTP method
                if (api.get().getRoleRequired() == StatusEnum.ENABLED.getValue()) {
                    boolean userHasRoleAccess;
                    userHasRoleAccess = checkUserRole(userAccess.get().getUser().getUserRoles().stream()
                            .map(e -> e.getRole().getRoleId()).collect(Collectors.toList()), api.get(), httpMethod);

                    logger.debug("@RequestFilter#doFilterInternal: User {} access to the requested API", userHasRoleAccess ? "has" : "doesn't have");
                    if (!userHasRoleAccess) {
                        logger.warn("@RequestFilter#doFilterInternal: Access Denied");
                        handleFailedAccess(response, ResponseCode.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED, request);
                        return;
                    }

//                    if (!httpMethod.trim().equalsIgnoreCase("GET") && !origReqURL.trim().equalsIgnoreCase("/checker/pending-activities")) {
//                        if (apiApiListResponseDto.get().getApiListId() == null) {
//                            handleFailedAccess(response, ResponseCode.CFG_INVALID_API, HttpStatus.NOT_FOUND, request, isValidIp);
//                            return;
//                        } else {
//                            RequestContextHolder.getRequestAttributes().setAttribute("API_LIST_OBJECT", apiApiListResponseDto.get().getDescription(), WebRequest.SCOPE_REQUEST);
//                        }
//                        if (apiApiListResponseDto.get().getStp() == '0') {//case maker checker else do nothing
//                            isStp = true;
//                            setActivityUUID(institution, String.valueOf(apiApiListResponseDto.get().getApiId()), header_BranchId, header_InstId, httpMethod, apiApiListResponseDto.get().getScopeId(), apiApiListResponseDto.get().getApiListId(), request.getRemoteAddr());
//                        }
//                    }
                }
            } else {
                logger.warn("@RequestFilter#doFilterInternal: Login is not required");
            }
        }

        CustomContentCachingRequestWrapper requestWrapper = new CustomContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        /*
         * 1. Write a function in common service that takes HttpStatus code and response code and returns NbeHttpStatus
         * 		Inside the function fetch NBE status from DB based on HttpStatusCode and RespCode P.S if not found return default value
         * 2. If HttpStatus is 200 dont enter the condition
         * 3. Call the function inside the try
         * 4. Check online how to read the http status from the response and how to update the response (read write data into HttpServletResponse inside filter in SpringBoot)
         */

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            response.setHeader("X-Powered-By", null);
            int statusCode = responseWrapper.getStatus();
            logger.debug("@RequestFilter#doFilter: Status Code: " + statusCode);

            byte[] responseBody = responseWrapper.getContentAsByteArray();
            String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
            Map<String, String> errorCodes = new HashMap<>();

            try {
                JSONObject jsonObject = new JSONObject(responseBodyString);
                if (responseBodyString.contains("errors")) {
                    JSONArray errors = jsonObject.getJSONArray("errors");
                    if (!errors.isEmpty()) {
                        logger.debug("@RequestFilter#doFilter: Parsing Error response");
                        for (int i = 0; i < errors.length(); i++) {
                            String error = String.valueOf(errors.get(i)).split(":")[0].trim();
                            logger.debug("@RequestFilter#doFilter: Error Code {}", error);
                            errorCodes.put(error, String.valueOf(errors.get(i)));
                        }
                    }
                }

                if (!errorCodes.isEmpty()) {
                    responseWrapper.resetBuffer();
                    responseWrapper.getWriter().flush();
                    responseWrapper.getWriter().write(objectMapper.writeValueAsString(ErrorDto.builder().errors(new ArrayList<>(errorCodes.keySet())).build()));
                }
            } catch (JSONException e) {
                //logger.error("@RequestFilter#doFilter Error parsing JSON response: " + e.getMessage());
            }

        } catch (IOException e) {
            logger.error("@RequestFilter#doFilterInternal: " + e.getMessage());
            handleFailedAccess(response, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        }
        finally {
            if (api != null && "0".equals(api.get().getStp()) && response.getStatus()==HttpStatus.OK.value()) {
                SuccessfulResponseDto messageContent = new SuccessfulResponseDto();
                messageContent.setMessage("Your activity is pending approval");
                String json = objectMapper.writeValueAsString(messageContent);
                responseWrapper.resetBuffer();
                responseWrapper.getWriter().flush();
                responseWrapper.getWriter().write(json);
                responseWrapper.setStatus(HttpStatus.OK.value());
            }
        }
        responseWrapper.copyBodyToResponse();
    }

    private boolean checkUserRole(List<Integer> roleIds, Api api, String httpMethod) {
        if (httpMethod.equalsIgnoreCase("GET") || "1".equalsIgnoreCase(api.getIsGetApi())) {
            return roleApiAccessService.hasAccessView(roleIds, api.getApiId());
        } else if (httpMethod.equalsIgnoreCase("POST")) {
            boolean accessAdd = roleApiAccessService.hasAccessAdd(roleIds, api.getApiId());
            boolean accessUpdate = roleApiAccessService.hasAccessUpdate(roleIds, api.getApiId());
            return (accessAdd || accessUpdate);
        } else if (httpMethod.equalsIgnoreCase("DELETE")) {
            return roleApiAccessService.hasAccessDelete(roleIds, api.getApiId());
        }
        return false;
    }

    private void handleFailedAccess(HttpServletResponse response, String responseMsg, HttpStatus httpStatus,
                                    HttpServletRequest request) throws IOException {
        Locale locale = request.getLocale();
        FilterErrorDto errorResponse = new FilterErrorDto();
        errorResponse.setHttpStatus(httpStatus);
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getURL (String requestURL) {
        if (requestURL.endsWith("/signout") || requestURL.contains("/signout")) {
            return "/signout";
        }
        if (requestURL.endsWith("/about") || requestURL.contains("/about")) {
            return "/about";
        }
        String s1 =  requestURL.substring(requestURL.indexOf("/", requestURL.indexOf("/") + 1));
        return s1.substring(s1.indexOf("/", s1.indexOf("/") + 1));
    }

    private String getRequestURL(String requestURL) {
        System.out.println("Requesturl>>>>>" + requestURL);
        if (requestURL.contains("springfox-swagger") || requestURL.contains("swagger-resources")) {
            return "/swagger-ui.html";
        }
        int slashCount = (int) requestURL.chars().filter(ch -> ch == '/').count();
        // System.out.println(requestURL);
        // System.out.println(slashCount);
        if (slashCount == 2) {
            String[] list = requestURL.split("/");
//			requestURL = "/" + list[1] + "/" + list[2];
            boolean intValue = isNumeric(list[2]);
            if (intValue) {
                requestURL = "/" + list[1];
            } else {
                ApiListUtis apiListUtis = new ApiListUtis();
                String[] apiList = apiListUtis.listOfApiTwoSlashes();
                if (Arrays.stream(apiList).anyMatch(list[2]::equals)) {
                    requestURL = "/" + list[1] + "/" + list[2];
                } else {
                    requestURL = "/" + list[1];
                }
            }
        } else if (slashCount == 3) {
            String[] list = requestURL.split("/");
            requestURL = "/" + list[1];
            boolean intValue1 = isNumeric(list[2]);
            boolean intValue2 = isNumeric(list[3]);
            if (intValue1 && intValue2) {
                requestURL = "/" + list[1];
            } else {

                ApiListUtis apiListUtis = new ApiListUtis();
                String[] apiList = apiListUtis.listOfApiThreeSlashes();
                if (Arrays.stream(apiList).anyMatch(list[2]::equals)) {
                    requestURL = "/" + list[1] + "/" + list[2];
                } else {
                    requestURL = "/" + list[1];
                }
            }
//			if(list[2].equals("password-reset")) {
//				requestURL = requestURL + "/" + list[2];
//			}
//			requestURL = "/" + list[1] + "/" + list[2];

        } else if (slashCount > 3) {
            String[] list = requestURL.split("/");
//			requestURL = "/" + list[3] + "/" + list[4];

            ApiListUtis apiListUtis = new ApiListUtis();
            String[] apiList = apiListUtis.listOfApiThreeSlashes();
            if (Arrays.stream(apiList).anyMatch(list[2]::equals)) {
                requestURL = "/" + list[1] + "/" + list[2];
            } else {
                requestURL = "/" + list[1];
            }
//			requestURL = "/" + list[1];
        }
        return requestURL;
    }
}
