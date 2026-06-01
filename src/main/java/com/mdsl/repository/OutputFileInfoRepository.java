package com.mdsl.repository;

import com.mdsl.model.entity.OutputFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputFileInfoRepository extends JpaRepository<OutputFileInfo, Integer> {

    @Query(value = "SELECT POSITION " +
            "  FROM MD_OUTPUT_FILE_FIELDS_INFO " +
            " WHERE     OUTPUT_FILE_TYPE = :outputFileType " +
            "       AND SUB_SUMMARY = :merchantSubSummary" +
            "       AND SUM_PER_ACCOUNT = :summaryBy ",nativeQuery = true)
    String findCodeColumn(@Param("outputFileType") String outputFileType,
                          @Param("merchantSubSummary") String merchantSubSummary,
                          @Param("summaryBy") String summaryBy);
}
