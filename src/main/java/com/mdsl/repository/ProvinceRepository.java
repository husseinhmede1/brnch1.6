package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer>{

	List<Province> findByCntryCode(Country cntry);
	Optional<Province> findByProvStateAbbrev(String provStateAbbrev);
}
