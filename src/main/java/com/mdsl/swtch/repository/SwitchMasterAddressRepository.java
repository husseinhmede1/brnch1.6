package com.mdsl.swtch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchMasterAddress;

@Repository
public interface SwitchMasterAddressRepository extends JpaRepository<SwitchMasterAddress, Integer>{
	Optional<SwitchMasterAddress> findByAddressIdAndInstitutionId(Integer addressId, String institutionId);
}
