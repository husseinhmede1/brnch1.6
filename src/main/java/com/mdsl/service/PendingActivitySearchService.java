package com.mdsl.service;

import com.mdsl.model.dto.request.PendingActivitySearchRequestDto;
import com.mdsl.model.dto.response.PageablePendingActivityResponseDto;
import com.mdsl.model.dto.response.PaginationApiResponseDto;
import com.mdsl.model.dto.response.PendingActivityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PendingActivitySearchService {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_FROM =
        " FROM MD_EXT_PENDING_ACTIVITY pa " +
        " LEFT JOIN MD_BKD_API a ON a.API_ID = pa.API_ID " +
        " LEFT JOIN MD_ENT_USER u ON u.USER_ID = pa.CREATED_BY " +
        " WHERE pa.INST_ID = ? ";

    private static final String SELECT_COLS =
        "SELECT pa.PENDING_ACTIVITY_ID, a.API_DESC, a.API_URL, pa.STATUS, pa.NOTES, " +
        "pa.INST_ID, pa.CLASS, pa.METHOD, " +
        "TO_CHAR(pa.CREATED_DATE, 'YYYY-MM-DD HH24:MI:SS') AS CREATED_DATE, " +
        "pa.CREATED_BY, u.USERNAME ";

    public PageablePendingActivityResponseDto search(PendingActivitySearchRequestDto req, int instId) {

        List<Object> params = new ArrayList<>();
        params.add(String.valueOf(instId));

        StringBuilder where = new StringBuilder(BASE_FROM);
        appendFilters(where, params, req);

        // Count query
        String countSql = "SELECT COUNT(*) " + where;
        int total = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

        // Data query with Oracle pagination
        String dataSql = SELECT_COLS + where +
            " ORDER BY pa.CREATED_DATE DESC " +
            " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        params.add(req.getOffset() * req.getPageSize());
        params.add(req.getPageSize());

        List<PendingActivityResponseDto> rows = jdbcTemplate.query(dataSql, rs -> {
            List<PendingActivityResponseDto> list = new ArrayList<>();
            while (rs.next()) {
                list.add(PendingActivityResponseDto.builder()
                    .pendingActivityId(rs.getInt("PENDING_ACTIVITY_ID"))
                    .apiDesc(rs.getString("API_DESC"))
                    .apiUrl(rs.getString("API_URL"))
                    .status(rs.getString("STATUS"))
                    .notes(rs.getString("NOTES"))
                    .instId(rs.getString("INST_ID"))
                    .clazz(rs.getString("CLASS"))
                    .method(rs.getString("METHOD"))
                    .createdDate(rs.getString("CREATED_DATE"))
                    .createdById(rs.getObject("CREATED_BY") != null ? rs.getInt("CREATED_BY") : null)
                    .createdByUsername(rs.getString("USERNAME"))
                    .build());
            }
            return list;
        }, params.toArray());

        PaginationApiResponseDto pagination = PaginationApiResponseDto.builder()
            .pageNumber(req.getOffset())
            .pageSize(req.getPageSize())
            .totalNumberOfRecords(total)
            .build();

        return PageablePendingActivityResponseDto.builder()
            .pendingActivities(rows)
            .paginationResponseDto(pagination)
            .build();
    }

    private void appendFilters(StringBuilder where, List<Object> params, PendingActivitySearchRequestDto req) {
        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            where.append(" AND pa.STATUS = ? ");
            params.add(req.getStatus().trim());
        }
        if (req.getApiId() != null) {
            where.append(" AND pa.API_ID = ? ");
            params.add(req.getApiId());
        }
        if (req.getFromDate() != null && !req.getFromDate().isBlank()) {
            where.append(" AND TRUNC(pa.CREATED_DATE) >= TO_DATE(?, 'YYYY-MM-DD') ");
            params.add(req.getFromDate().trim());
        }
        if (req.getToDate() != null && !req.getToDate().isBlank()) {
            where.append(" AND TRUNC(pa.CREATED_DATE) <= TO_DATE(?, 'YYYY-MM-DD') ");
            params.add(req.getToDate().trim());
        }
    }
}
