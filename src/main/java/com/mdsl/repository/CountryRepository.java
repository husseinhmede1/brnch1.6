package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.Country;

@Repository
@Transactional(readOnly = true)
public interface CountryRepository extends JpaRepository<Country, Integer>{

	//Country findByCntryCODE(int CNTRY_CODE);

	//void deleteByCntryCODE(int CNTRY_CODE);
	@Modifying
	@Query("UPDATE MD_COUNTRY SET CNTRY_STATUS = :cntryStatus WHERE cntryId = :id")
	void UpdateStatus(int id, char cntryStatus);

	List<Country> findByCntryStatus(char value, Sort sort);

	List<Country> findByUserCreate(String string);
	
	Optional<Country> findByCntryCode(String cntryCode);
	
	Optional<Country> findByCntryCodeAndCntryStatus(String cntryCode, Character cntryStatus);
	

}
