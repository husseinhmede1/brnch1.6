package com.mdsl.repository;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mdsl.model.entity.SystemCodeHeader;

public interface SystemCodeHeaderRepository extends JpaRepository<SystemCodeHeader, Integer>{

}
