package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ChargeMethod;

@Repository
public interface ChargeMethodRepository extends JpaRepository<ChargeMethod, Integer>{

}
