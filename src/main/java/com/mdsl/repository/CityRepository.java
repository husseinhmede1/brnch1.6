package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.City;
import com.mdsl.model.entity.Country;
import com.mdsl.model.entity.Province;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>{
	
	Optional<City> findByCntryCodeAndCityName(Country cntry, String cityName);
	Optional<City> findByCityAbbrev(String cityAbbrev);

	List<City> findByCntryCode(Country cntry);

	List<City> findByProvStateAbbrev(Province pvnc);

	
}
