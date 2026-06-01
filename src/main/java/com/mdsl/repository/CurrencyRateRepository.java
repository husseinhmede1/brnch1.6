package com.mdsl.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.CurrencyRate;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Terminal;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer>{
	Optional<CurrencyRate> findByCurrencyRateId(int id);

	@Query("FROM MD_CURRENCY_RATE WHERE INSTITUTION_ID = :id")
	List<CurrencyRate> findByInstitution(String id, Sort by);

//	@Query("FROM MD_CURRENCY_RATE WHERE INSTITUTION_ID = :id AND EFFECTIVE_DATE >= :fromDate")
//	List<CurrencyRate> findByInstitutionAndEffectiveDateGreaterThanEqual(int id, Date fromDate, Sort by);
//
//	@Query("FROM MD_CURRENCY_RATE WHERE INSTITUTION_ID = :id AND EFFECTIVE_DATE <= :toDate")
//	List<CurrencyRate> findByInstitutionAndEffectiveDateLessThanEqual(int id, Date toDate, Sort by);
//
//	@Query("FROM MD_CURRENCY_RATE WHERE INSTITUTION_ID = :id AND EFFECTIVE_DATE BETWEEN :fromDate and :toDate")
//	List<CurrencyRate> findByInstitutionAndEffectiveDateBetween(int id, Date fromDate, Date toDate, Sort by);
	
	Page<CurrencyRate> findByInstitution_InstitutionId(String institutionId,Pageable pageable);

	Page<CurrencyRate> findByInstitution_InstitutionIdAndEffectiveDateGreaterThanEqual(String institutionId, Date fromDate,
			Pageable pageable);

	Page<CurrencyRate> findByInstitution_InstitutionIdAndEffectiveDateLessThanEqual(String institutionId, Date toDate,
			Pageable pageable);

	Page<CurrencyRate> findByInstitution_InstitutionIdAndEffectiveDateBetween(String institutionId, Date fromDate,
			Date toDate, Pageable pageable);

	List<CurrencyRate> findByUserCreate(String string);

}
