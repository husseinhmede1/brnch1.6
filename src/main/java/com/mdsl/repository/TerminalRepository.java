package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Terminal;
import com.mdsl.model.entity.TerminalTypes;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Integer> {

	@Modifying
	@Query("UPDATE Terminal t SET t.status = :status" + " WHERE t.terminalId = :terminalId")
	void updateStatus(int terminalId, char status);

	List<Terminal> findByStatus(char value, Sort by);

	@Query("from Terminal t where t.institutionEntity.institutionId=:institutionId")
	List<Terminal> findByInstitutionEntity(String institutionId, Sort by);

	@Query("from Terminal t where t.entitiesObject.entityId=:entitiesId")
	List<Terminal> findByEntitiesObject(String entitiesId, Sort by);

	@Query("from Terminal t where t.entitiesObject.entityId=:entitiesId And t.institutionEntity.institutionId=:institutionId ")
	List<Terminal> findByEntitiesObjectAndInstitutionEntity(String entitiesId, String institutionId, Sort by);

	@Query("from Terminal t")
	Page<Terminal> findAll(Pageable pageable);

	@Query("from Terminal t where t.entitiesObject.entityId = :entityId")
	Page<Terminal> findAllByEntitiesObject(@Param("entityId") String entityId, Pageable pageable);

	List<Terminal> findByEntitiesObject_EntityId(String entityId);

	Optional<Terminal> findByTerminalId(String terminalId);

	List<Terminal> findByMccList(String mcc);

	List<Terminal> findByTerminalIdAndInstitutionEntity(String terminalId, Institution institution);

	Optional<Terminal> findByTerminalIdAndInstitutionEntity_InstitutionId(String termninalId, String instId);

	List<Terminal> findByUserCreate(String string);

	List<Terminal> findByTerminalTypes_TerminalType(String terminalType);

}
