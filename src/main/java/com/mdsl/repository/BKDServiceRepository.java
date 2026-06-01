package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.BKDService;

@Repository
public interface BKDServiceRepository extends JpaRepository<BKDService, Integer>{
	@Modifying
	@Transactional
	@Query(value="UPDATE MD_BKD_SERVICE SET BATCHSIZE = :batchSize, UPDATED_DATE = SYSDATE, UPDATED_BY = :userId WHERE SERVICE_ID=:serviceId",nativeQuery=true)
	void updateBatchSize(int serviceId, int batchSize, int userId);

	@Query(value = "SELECT filter FROM MD_BKD_SERVICE where SERVICE_MODE  = 'file2db' " +
			"UNION ALL " +
			"SELECT 'Financial Transactions' as FILTER from dual " +
			"UNION ALL " +
			"SELECT 'Remittance' as FILTER from dual ", nativeQuery = true)
	List<String> getFileTypes();
}