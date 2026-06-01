package com.mdsl.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.BKDParameter;

@Repository
public interface BKDParameterRepository extends JpaRepository<BKDParameter, Integer>{
	@Query(value="SELECT PARAMETER_NAME FROM MD_BKD_PARAMETERS WHERE PARAMETER_ID IN (SELECT PARAMETER_ID FROM MD_BKD_PARAMETERS_SERVICE WHERE PARAMETERS_SERVICE_ID = :parametersServiceId)",nativeQuery=true)
	String findParameterName(@Param("parametersServiceId") Integer parametersServiceId);
	List<BKDParameter> findByParameterIdIn(Set<Integer> parameterIds);
}