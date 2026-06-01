package com.mdsl.swtch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchPosTermType;

@Repository
public interface SwitchPosTermTypeRepository extends JpaRepository<SwitchPosTermType, String>{
	
}
