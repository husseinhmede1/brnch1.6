package com.mdsl.repository;

import com.mdsl.model.entity.RoleApiAccess;
import com.mdsl.model.entity.keys.RoleApiAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleApiAccessRepository
        extends JpaRepository<RoleApiAccess, RoleApiAccessId> {

    Optional<RoleApiAccess> findByRoleIdAndApiId(
            Integer roleId,
            Integer apiId
    );

    List<RoleApiAccess> findByRoleIdInAndApiId(
            List<Integer> roleIds,
            Integer apiId
    );

    @Transactional
    @Modifying
    @Query(value = "BEGIN" +
            "        DBMS_MVIEW.REFRESH('MV_ROLE_API_ACCESS', 'C');" +
            "    END;", nativeQuery = true)
    void refreshRoleApiAccessMv();
}