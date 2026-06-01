package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.InstitutionType;

@Repository
public interface InstitutionTypeRepository extends JpaRepository<InstitutionType, Integer>{

}
