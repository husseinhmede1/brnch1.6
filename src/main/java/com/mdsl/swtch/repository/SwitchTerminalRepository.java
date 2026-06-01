package com.mdsl.swtch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchTerminal;

@Repository
public interface SwitchTerminalRepository extends JpaRepository<SwitchTerminal, Integer>{
	Optional<SwitchTerminal> findByTerminalIdHigh(String terminalIdHigh);
}
