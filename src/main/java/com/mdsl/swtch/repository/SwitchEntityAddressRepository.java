package com.mdsl.swtch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.swtch.model.entity.SwitchEntityAddress;

@Repository
public interface SwitchEntityAddressRepository extends JpaRepository<SwitchEntityAddress, String>{

}
