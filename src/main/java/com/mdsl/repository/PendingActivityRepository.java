package com.mdsl.repository;

import com.mdsl.model.entity.PendingActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingActivityRepository extends JpaRepository<PendingActivity, Integer> {
}