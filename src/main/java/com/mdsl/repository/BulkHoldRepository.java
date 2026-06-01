package com.mdsl.repository;

import com.mdsl.model.entity.BulkHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BulkHoldRepository extends JpaRepository<BulkHold, Integer> {


    @Query(value = "SELECT DISTINCT FILE_ID " +
            "FROM MD_BULK_HOLD " +
            "WHERE PROCESSING_FLAG = 'N'",nativeQuery = true)
    List<String> getDistinctFileNames(String institutionId);
}