package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

	@Query("FROM MD_GENERAL_ADDRESS WHERE ENTITY_ID = :entitiesId")
	List<Address> findAddressByEntityId(@Param("entitiesId") String entitiesId, Sort by);

	List<Address> findByEntitiesObject_EntityId(String entityId);

	List<Address> findByUserCreate(String string);
	
	@Query("SELECT COUNT(*) FROM MD_GENERAL_ADDRESS WHERE CITY_CODE = :cityAbbrev")
	Integer findByCityAbbrev(String cityAbbrev);


}
