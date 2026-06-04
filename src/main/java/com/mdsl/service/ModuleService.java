package com.mdsl.service;

import com.mdsl.model.dto.response.ActivityPermissionDto;
import com.mdsl.model.dto.response.ApiUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    private final JdbcTemplate jdbcTemplate;

    public ModuleService(@Qualifier("appJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String MODULES_BY_USER_SQL =
        "SELECT a.ACTIVITY_ID, a.ACTIVITY_CODE, a.ACTIVITY_DESC, " +
        "a.IS_MENU, a.HAS_SCREEN, " +
        "MAX(ra.ACCESS_VIEW)    AS ACCESS_VIEW, " +
        "MAX(ra.ACCESS_ADD)     AS ACCESS_ADD, " +
        "MAX(ra.ACCESS_UPDATE)  AS ACCESS_UPDATE, " +
        "MAX(ra.ACCESS_DELETE)  AS ACCESS_DELETE, " +
        "MAX(ra.ACCESS_CHECKER) AS ACCESS_CHECKER, " +
        "bapi.API_CODE, bapi.API_URL, bapi.API_FUNCTION " +
        "FROM MD_CFG_ACTIVITY a " +
        "JOIN MD_ENT_ROLE_ACTIVITY ra ON ra.ACTIVITY_ID = a.ACTIVITY_ID " +
        "JOIN MD_ENT_USER_ROLE ur ON ur.ROLE_ID = ra.ROLE_ID " +
        "   AND ur.INST_ID = ? AND ur.USER_ID = ? " +
        "LEFT JOIN MD_CFG_ACTIVITY_API caa ON caa.ACTIVITY_ID = a.ACTIVITY_ID " +
        "LEFT JOIN MD_BKD_API bapi ON bapi.API_ID = caa.API_ID " +
        "GROUP BY a.ACTIVITY_ID, a.ACTIVITY_CODE, a.ACTIVITY_DESC, " +
        "   a.IS_MENU, a.HAS_SCREEN, " +
        "   bapi.API_CODE, bapi.API_URL, bapi.API_FUNCTION " +
        "ORDER BY a.ACTIVITY_ID";

    public List<ActivityPermissionDto> getModulesActivitiesByUser(int instId, int userId) {

        // activityId → dto (collect URLs per activity across multiple rows)
        Map<Integer, ActivityPermissionDto> dtoMap = new LinkedHashMap<>();

        jdbcTemplate.query(MODULES_BY_USER_SQL, rs -> {
            int actId = rs.getInt("ACTIVITY_ID");

            ActivityPermissionDto dto = dtoMap.get(actId);
            if (dto == null) {
                dto = ActivityPermissionDto.builder()
                        .activityId(actId)
                        .activityCode(rs.getString("ACTIVITY_CODE"))
                        .activityDesc(rs.getString("ACTIVITY_DESC"))
                        .isMenu(rs.getString("IS_MENU"))
                        .hasScreen(rs.getString("HAS_SCREEN"))
                        .accessView(nvl(rs.getString("ACCESS_VIEW")))
                        .accessAdd(nvl(rs.getString("ACCESS_ADD")))
                        .accessUpdate(nvl(rs.getString("ACCESS_UPDATE")))
                        .accessDelete(nvl(rs.getString("ACCESS_DELETE")))
                        .accessChecker(nvl(rs.getString("ACCESS_CHECKER")))
                        .urls(new ArrayList<>())
                        .build();
                dtoMap.put(actId, dto);
            }

            String apiUrl = rs.getString("API_URL");
            if (apiUrl != null) {
                dto.getUrls().add(ApiUrlDto.builder()
                        .apiCode(rs.getString("API_CODE"))
                        .apiUrl(apiUrl)
                        .apiFunction(rs.getString("API_FUNCTION"))
                        .build());
            }
        }, String.valueOf(instId), userId);

        return new ArrayList<>(dtoMap.values());
    }

    private static String nvl(String value) {
        return (value != null && !value.isEmpty()) ? value : "0";
    }
}
