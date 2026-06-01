package com.mdsl.repository;

import com.mdsl.model.entity.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedIpRepository extends JpaRepository<BlockedIp,Long> {
    boolean existsByIpAddress(String ipAddress);
}
