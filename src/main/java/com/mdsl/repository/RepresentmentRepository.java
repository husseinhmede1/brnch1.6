package com.mdsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdsl.model.entity.Representment;

public interface RepresentmentRepository extends JpaRepository<Representment, Integer> {

}
