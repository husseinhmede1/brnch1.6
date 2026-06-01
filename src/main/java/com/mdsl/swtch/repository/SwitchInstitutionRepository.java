package com.mdsl.swtch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchInstitution;

@Repository
public interface SwitchInstitutionRepository extends JpaRepository<SwitchInstitution, String>{

}
