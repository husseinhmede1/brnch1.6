package com.mdsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.FrequencyMaster;

@Repository
public interface FrequencyMasterRepository extends JpaRepository<FrequencyMaster, Integer> {
	Optional<FrequencyMaster> findByFrequencyId(int id);
}