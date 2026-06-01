package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ActivityPackage;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.NonActivityPackage;

@Repository
public interface EntitiesRepository extends JpaRepository<Entities, String> {
	
	Optional<Entities> findByInstitution_InstitutionIdAndEntityName(String institutionId, String entityName);
	
	Optional<Entities> findByEntityId(String entitiesId);
	
	Optional<Entities> findByEntityIdAndInstitution(String entitiesId,Institution institution);

	Optional<Entities> findByEntityIdAndInstitution_InstitutionId(String entityId,String instId);

	Page<Entities> findAll(Pageable pageable);

	Page<Entities> findByInstitution_InstitutionId(PageRequest pageRequest, String institutionId);

	Page<Entities> findByInstitution_InstitutionIdAndEntityNameIgnoreCaseContainingOrInstitution_InstitutionIdAndDbaNameIgnoreCaseContaining(
			PageRequest pageRequest, String institutionId, String search, String institutionId2, String search2);
	
	List<Entities> findByEntityNameIgnoreCaseAndEntityStatusAndStatusAndInstitution(String entityName,char entityStatus,char status,Institution institution);
	
	List<Entities> findByEntityNameIgnoreCaseAndEntityStatusAndStatusAndEntityIdAndInstitution(String entityName,char entityStatus,char status,int entityId,Institution institution);
	
	List<Entities> findByEntityNameIgnoreCaseAndEntityStatusAndStatusAndIsClonedAndInstitution(String entityName,char entityStatus,char status,Byte isCloned,Institution institution);
	
	List<Entities> findByEntityNameIgnoreCaseAndEntityStatusAndStatusAndIsClonedAndEntityIdAndInstitution(String entityName,char entityStatus,char status,Byte isCloned,int entityId,Institution institution);

	List<Entities> findByActivityFeePKGEntity(ActivityPackage activityPackage);

	List<Entities> findByNonactivityFeePKG(NonActivityPackage nonActivityPackage);

	List<Entities> findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatus(String institutionId,
			String entityName, char charAt, char charAt2);

	List<Entities> findByInstitution_InstitutionIdAndEntityNameIgnoreCaseAndEntityStatusAndStatusAndEntityId(
			String institutionId, String entityName, char charAt, char status, String entityId);

	List<Entities> findByInstitution_InstitutionId(String institutionId, Sort by);
	
	List<Entities> findByStatus(char charAt, Sort by);

//	List<Entities> findByActivityFeePKG_PackageId(String packageId,Sort by);

//	List<Entities> findByActivityFeePKG_PackageIdAndInstitution_InstitutionId(String packageId,String instId,Sort by);
	List<Entities> findByActivityFeePKGEntity_PackageIdAndInstitution_InstitutionId(String packageId,String instId,Sort by);

	List<Entities> findByNonactivityFeePKGEntity_PackageIdAndInstitution_institutionId(String packageId,String instId, Sort by);

	List<Entities> findByEntityIdContaining(String string, Sort by);

	List<Entities> findByEntityLevels(int hierarchyLevel);
	List<Entities> findByEntityLevelsAndInstitution(int hierarchyLevel,Institution institution);

	List<Entities> findByDefaultMCC(String mcc);

	List<Entities> findByEntityLevelsAndInstitution_InstitutionId(Integer hierarchyLevel, String institutionId);

	List<Entities> findByUserCreate(String string);

	List<Entities> findByAcctTemplateHdrId(int paramInt);
	List<Entities> findByAcctTemplateHdrIdAndInstitution_InstitutionId(int paramInt, String institutionId);

	@Query("FROM MD_ACQ_ENTITY WHERE INSTITUTION_ID = :instId AND (:entityId IS NULL OR :entityId = '' OR ENTITY_ID = :entityId) AND "
			+ "(:parentId IS NULL OR :parentId = '' OR PARENT_ID = :parentId) AND "
			+ "(:mcc IS NULL OR :mcc = '' OR DEFAULT_MCC = :mcc) AND "
			+ "(:entityName IS NULL OR :entityName = '' OR UPPER(ENTITY_NAME) = UPPER(:entityName))")
	List<Entities> searchByFilters(String instId,
			String entityId, String parentId, String mcc, String entityName, Sort by);
	
	List<Entities> findByParentIdEntity_EntityId(String parentId);

	List<Entities> findByEntityLevelsAndInstitution_InstitutionIdAndParentIdEntity_EntityId(
			Integer hierarchyLevel, String institutionId, String parentId);

	@Query(value = "SELECT MD_ACQ_ENTITY_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	int findEntitySeqNextValue();
	
	
	@Query(
		    value = "SELECT * " +
		        " FROM MD_ACQ_ENTITY e " +
		        " WHERE e.INSTITUTION_ID = :institutionId " +
		        " AND ( :search IS NULL OR " +
		        "       LOWER(e.ENTITY_NAME) LIKE LOWER('%' || :search || '%') " +
		        "    OR LOWER(e.DBA_NAME) LIKE LOWER('%' || :search || '%') ) " +
		        " AND ( :parentId IS NULL OR e.PARENT_ID = :parentId ) " +
		        " AND ( :businessTypeId IS NULL OR e.BUSINESS_TYPE = :businessTypeId ) " +
		        " AND ( :mccId IS NULL OR e.DEFAULT_MCC = (SELECT MCC FROM MD_MCC_TABLE WHERE RECORD_SEQ_ID=:mccId)) " +
		        " AND ( :entityStatus IS NULL OR e.ENTITY_STATUS = :entityStatus ) " +
		        " AND ( :entityId IS NULL OR e.ENTITY_ID = :entityId ) " +
		        " AND ( :entityLevelId IS NULL OR e.ENTITY_LEVEL =(SELECT HIERARCHY_LEVEL FROM MD_ACQ_ENTITY_LEVELS WHERE RECORD_SEQ_ID=:entityLevelId)  ) " +
		        " AND ( :entityName IS NULL OR " +
		        "       LOWER(e.ENTITY_NAME) LIKE LOWER('%' || :entityName || '%') ) " +
		        " AND ( :hotMerchantFlag IS NULL OR e.HOT_MERCHANT_FLAG = :hotMerchantFlag ) " +
		        " AND ( :fromDate IS NULL OR e.DATE_CREATE >= TO_DATE(:fromDate, 'YYYY-MM-DD') ) " +
		        " AND ( :toDate IS NULL OR e.DATE_CREATE <= TO_DATE(:toDate, 'YYYY-MM-DD') ) ",
		    countQuery = "SELECT COUNT(*) " +
		        " FROM MD_ACQ_ENTITY e " +
		        " WHERE e.INSTITUTION_ID = :institutionId " +
		        " AND ( :search IS NULL OR " +
		        "       LOWER(e.ENTITY_NAME) LIKE LOWER('%' || :search || '%') " +
		        "    OR LOWER(e.DBA_NAME) LIKE LOWER('%' || :search || '%') ) " +
		        " AND ( :parentId IS NULL OR e.PARENT_ID = :parentId ) " +
		        " AND ( :businessTypeId IS NULL OR e.BUSINESS_TYPE = :businessTypeId ) " +
		        " AND ( :mccId IS NULL OR e.DEFAULT_MCC = (SELECT MCC FROM MD_MCC_TABLE WHERE RECORD_SEQ_ID=:mccId)) " +
		        " AND ( :entityStatus IS NULL OR e.ENTITY_STATUS = :entityStatus ) " +
		        " AND ( :entityId IS NULL OR e.ENTITY_ID = :entityId ) " +
		        " AND ( :entityLevelId IS NULL OR e.ENTITY_LEVEL =(SELECT HIERARCHY_LEVEL FROM MD_ACQ_ENTITY_LEVELS WHERE RECORD_SEQ_ID=:entityLevelId)  ) " +
		        " AND ( :entityName IS NULL OR " +
		        "       LOWER(e.ENTITY_NAME) LIKE LOWER('%' || :entityName || '%') ) " +
		        " AND ( :hotMerchantFlag IS NULL OR e.HOT_MERCHANT_FLAG = :hotMerchantFlag ) " +
		        " AND ( :fromDate IS NULL OR e.DATE_CREATE >= TO_DATE(:fromDate, 'YYYY-MM-DD') ) " +
		        " AND ( :toDate IS NULL OR e.DATE_CREATE <= TO_DATE(:toDate, 'YYYY-MM-DD') ) ",
		    nativeQuery = true
		)
		Page<Entities> searchEntities(
		    Pageable pageable,
		    @Param("institutionId") String institutionId,
		    @Param("search") String search,
		    @Param("parentId") String parentId,
		    @Param("businessTypeId") String businessTypeId,
		    @Param("mccId") String mccId,
		    @Param("entityStatus") String entityStatus,
		    @Param("entityId") String entityId,
		    @Param("fromDate") String fromDate,
		    @Param("toDate") String toDate,
		    @Param("entityLevelId") String entityLevelId,
		    @Param("entityName") String entityName,
		    @Param("hotMerchantFlag") String hotMerchantFlag
		);
	
	
    @Query(value="SELECT * FROM MD_ACQ_ENTITY  WHERE TERMINATION_DATE IS NOT NULL AND TERMINATION_DATE > SYSDATE",nativeQuery=true)
    List<Entities> findAllWithFutureTermination();


}