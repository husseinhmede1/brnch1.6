package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.EntityLevels;

public interface EntityLevelsRepository extends JpaRepository<EntityLevels, Integer>{

	List<EntityLevels> findByInstitution_InstitutionId(String institutionId, Sort by);

//	List<EntityLevels> findByHierarchyLevel(int entityLevels);

	List<EntityLevels> findByTypeDescription(String value);
	
	List<EntityLevels> findByHierarchyLevelAndInstitution_InstitutionId(int entityLevels ,String institutionId);

	Optional<EntityLevels> findByTypeDescriptionAndInstitution_InstitutionId(String value, String institutionId);



	

	

}
