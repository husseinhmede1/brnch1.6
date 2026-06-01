package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdsl.model.entity.MerchantType;

public interface MerchantTypeRepository extends JpaRepository<MerchantType, Integer> {

}
