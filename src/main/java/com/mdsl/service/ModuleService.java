package com.mdsl.service;

import com.mdsl.model.dto.response.ActivityModuleResponseDto;
import com.mdsl.model.dto.response.ModuleActivityResponseDto;
import com.mdsl.model.objects.FrontUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final JdbcTemplate jdbcTemplate;

    private static final String MODULES_BY_USER_SQL =
        "SELECT a.ACTIVITY_ID, a.ACTIVITY_CODE, a.ACTIVITY_DESC, " +
        "a.IS_MENU, a.HAS_SCREEN, a.PARENT_ACTIVITY_ID, " +
        "MAX(ra.ACCESS_VIEW)    AS ACCESS_VIEW, " +
        "MAX(ra.ACCESS_ADD)     AS ACCESS_ADD, " +
        "MAX(ra.ACCESS_UPDATE)  AS ACCESS_UPDATE, " +
        "MAX(ra.ACCESS_DELETE)  AS ACCESS_DELETE, " +
        "MAX(ra.ACCESS_CHECKER) AS ACCESS_CHECKER, " +
        "bapi.API_URL, bapi.IS_GET_API " +
        "FROM MD_CFG_ACTIVITY a " +
        "JOIN MD_ENT_ROLE_ACTIVITY ra ON ra.ACTIVITY_ID = a.ACTIVITY_ID " +
        "JOIN MD_ENT_USER_ROLE ur ON ur.ROLE_ID = ra.ROLE_ID " +
        "   AND ur.INST_ID = ? AND ur.USER_ID = ? " +
        "LEFT JOIN MD_CFG_ACTIVITY_API caa ON caa.ACTIVITY_ID = a.ACTIVITY_ID " +
        "LEFT JOIN MD_BKD_API bapi ON bapi.API_ID = caa.API_ID " +
        "GROUP BY a.ACTIVITY_ID, a.ACTIVITY_CODE, a.ACTIVITY_DESC, " +
        "   a.IS_MENU, a.HAS_SCREEN, a.PARENT_ACTIVITY_ID, " +
        "   bapi.API_URL, bapi.IS_GET_API " +
        "ORDER BY a.PARENT_ACTIVITY_ID NULLS FIRST, a.ACTIVITY_ID";

    public List<ModuleActivityResponseDto> getModulesActivitiesByUser(int instId, int userId) {
        // activityId → row data (one entry per activity, URLs collected)
        Map<Integer, ActivityRow> rowMap = new LinkedHashMap<>();

        jdbcTemplate.query(MODULES_BY_USER_SQL, rs -> {
            int actId = rs.getInt("ACTIVITY_ID");
            ActivityRow row = rowMap.get(actId);
            if (row == null) {
                row = new ActivityRow();
                row.activityId    = actId;
                row.activityCode  = rs.getString("ACTIVITY_CODE");
                row.activityDesc  = rs.getString("ACTIVITY_DESC");
                row.isMenu        = toChar(rs.getString("IS_MENU"));
                row.hasScreen     = toChar(rs.getString("HAS_SCREEN"));
                Object pid        = rs.getObject("PARENT_ACTIVITY_ID");
                row.parentId      = pid != null ? ((Number) pid).intValue() : null;
                row.accessView    = toChar(rs.getString("ACCESS_VIEW"));
                row.accessAdd     = toChar(rs.getString("ACCESS_ADD"));
                row.accessUpdate  = toChar(rs.getString("ACCESS_UPDATE"));
                row.accessDelete  = toChar(rs.getString("ACCESS_DELETE"));
                row.accessChecker = toChar(rs.getString("ACCESS_CHECKER"));
                rowMap.put(actId, row);
            }
            String apiUrl = rs.getString("API_URL");
            if (apiUrl != null) {
                FrontUrl fu = new FrontUrl();
                fu.setUrl(apiUrl);
                fu.setIsMenu(rs.getString("IS_GET_API"));
                row.urls.add(fu);
            }
        }, instId, userId);

        // Build parentId → [childIds] map; collect roots (no parent in result set)
        Map<Integer, List<Integer>> children = new LinkedHashMap<>();
        List<Integer> roots = new ArrayList<>();

        for (ActivityRow row : rowMap.values()) {
            if (row.parentId == null || !rowMap.containsKey(row.parentId)) {
                roots.add(row.activityId);
            } else {
                children.computeIfAbsent(row.parentId, k -> new ArrayList<>()).add(row.activityId);
            }
        }

        return roots.stream()
                .map(id -> buildModule(id, rowMap, children))
                .collect(Collectors.toList());
    }

    private ModuleActivityResponseDto buildModule(
            int activityId,
            Map<Integer, ActivityRow> rowMap,
            Map<Integer, List<Integer>> children) {

        ActivityRow row = rowMap.get(activityId);
        if (row == null) return null;

        List<ActivityModuleResponseDto> activities = new ArrayList<>();
        List<ModuleActivityResponseDto> subModules = new ArrayList<>();

        for (Integer childId : children.getOrDefault(activityId, Collections.emptyList())) {
            if (children.containsKey(childId)) {
                ModuleActivityResponseDto sub = buildModule(childId, rowMap, children);
                if (sub != null) subModules.add(sub);
            } else {
                ActivityRow childRow = rowMap.get(childId);
                if (childRow != null) activities.add(toActivityDto(childRow));
            }
        }

        return ModuleActivityResponseDto.builder()
                .moduleId(row.activityId)
                .moduleDesc(row.activityDesc)
                .activities(activities)
                .subModule(subModules)
                .build();
    }

    private ActivityModuleResponseDto toActivityDto(ActivityRow row) {
        return ActivityModuleResponseDto.builder()
                .activityId(row.activityId)
                .activityCode(row.activityCode)
                .activityDesc(row.activityDesc)
                .isMenu(row.isMenu)
                .hasScreen(row.hasScreen)
                .url(row.urls.isEmpty() ? null : row.urls)
                .accessView(row.accessView)
                .accessAdd(row.accessAdd)
                .accessUpdate(row.accessUpdate)
                .accessDelete(row.accessDelete)
                .accessChecker(row.accessChecker)
                .build();
    }

    private static char toChar(String value) {
        return (value != null && !value.isEmpty()) ? value.charAt(0) : '0';
    }

    /** Flat holder used while processing result set rows. */
    private static class ActivityRow {
        int activityId;
        String activityCode;
        String activityDesc;
        char isMenu;
        char hasScreen;
        Integer parentId;
        char accessView;
        char accessAdd;
        char accessUpdate;
        char accessDelete;
        char accessChecker;
        List<FrontUrl> urls = new ArrayList<>();
    }
}
