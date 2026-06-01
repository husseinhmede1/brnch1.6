package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.TerminalTypes;

@Repository
public interface TerminalTypesRepository extends JpaRepository<TerminalTypes,Integer>{
	
	Optional<TerminalTypes> findByTerminalTypeEqualsIgnoreCase(String terminalType);

	List<TerminalTypes> findByStatus(String value, Sort by);

	List<TerminalTypes> findByUserCreate(String string);

	List<TerminalTypes> findByTerminalTypeEqualsIgnoreCaseAndTerminalTypesIdNot(String terminalType,
			Integer terminalTypesId);
}
