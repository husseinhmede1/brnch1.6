package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.mdsl.model.entity.ChargeMethod;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.SystemCode;

@Repository
@Transactional(rollbackOn = Exception.class)
public interface SystemCodeRepository extends JpaRepository<SystemCode, Integer> {

//	List<SystemCode> findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCase(String codePrefix, String codeSuffix, Sort by);
	List<SystemCode> findBySystemCodeHeader_SystemCodeHeaderIdAndCodeSuffixIgnoreCase(int systemCodeHeaderId,
			String codeSuffix, Sort by);

	List<SystemCode> findBySystemCodeHeader_SystemCodeHeaderIdAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(
			int systemCodeHeaderId, String codeSuffix, String institutionId, Sort by);
	
	List<SystemCode> findByDescription(String description);

    @Query(nativeQuery = false,value= "from MD_system_codes s where (s.institution.institutionId=:institutionId OR s.institution.institutionId='SYSTEM') AND  s.codePrefix=:codePrefix AND s.status=:status" )
	List<SystemCode> findByCodePrefixIgnoreCaseAndInstitution_InstitutionId(@Param("codePrefix") String codePrefix, @Param("institutionId") String institutionId,@Param("status") char status, Sort by);

	@Query(nativeQuery = false,value= "from MD_system_codes s where (s.institution.institutionId=:institutionId) AND  s.codePrefix=:codePrefix AND s.codeSuffix=:codeSuffix AND s.status=:status" )
	Optional<SystemCode> findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(@Param("codePrefix") String codePrefix,@Param("codeSuffix") String codeSuffix, @Param("institutionId") String institutionId,@Param("status") char status);


	List<SystemCode> findByCodePrefixIgnoreCaseAndStatus(String codePrefix, char status, Sort by);

	List<SystemCode> findByStatus(char charAt, Sort by);

	List<SystemCode> findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionId(String codePrefix,String codeSuffix, String institutionId);

	List<SystemCode> findByCodePrefixIgnoreCaseAndCodeSuffixIgnoreCaseAndInstitution_InstitutionIdAndSystemCodeIdNot(String codePrefix,String codeSuffix,
			String institutionId, int systemCodeId);

	List<SystemCode> findByCreatedBy(String string);

	List<SystemCode> findByCodePrefixAndInstitutionAndStatus(String paramString, Institution paramInstitution, Character status);
	
	Optional<SystemCode> findByCodePrefixAndCodeValueAndInstitution_InstitutionId(String paramString, String codeValue, String paramInstitution);
	
	Optional<SystemCode> findByCodePrefixAndCodeSuffixAndInstitution_InstitutionId(String codePrefix, String codeSuffix, String paramInstitution);
	
	@Query("SELECT s from MD_system_codes s WHERE s.codePrefix = :codePrefix and BITAND(substr(s.codeValue, 1, 1), :codeResult) = :codeResult")
	List<SystemCode> findByCodePrefixAndCodeValuess(@Param("codePrefix") String codePrefix, @Param("codeResult") String codeResult);
	
	@Query("SELECT s FROM MD_system_codes s " +
		       "WHERE s.codePrefix IN (:codePrefix, 'CUSTOM_FIELD_ID') " +
		       "AND (" +
		       "    (BITAND(substr(s.codeValue, :codeColumn, 1), :codeResult) = :codeResult) OR " +
		       "    (BITAND(substr(s.codeValue, :codeColumn2, 1), :codeResult) = :codeResult)" +
		       ")")
		List<SystemCode> findByCodePrefixAndCodeValue(
		    @Param("codePrefix") String codePrefix, 
		    @Param("codeColumn") String codeColumn, 
		    @Param("codeColumn2") String codeColumn2, 
		    @Param("codeResult") String codeResult
		);
	
	List<SystemCode> findByInstitutionOrInstitution(Institution paramInstitution1, Institution paramInstitution2, Sort sort);

	@Query(value = "select * from md_system_codes where code_prefix = :codePrefix and institution_id = :institutionId and code_value = :codeValue", nativeQuery = true)
	Optional<SystemCode> findByCodeSuffixCodeValueInstitution(@Param("codePrefix") String codePrefix, @Param("codeValue") String codeValue,@Param("institutionId") String institutionId);
    Optional<SystemCode> findByCodePrefixAndCodeSuffixAndStatus(String prefix,String suffix,Character status);
    Optional<SystemCode> findByCodePrefixAndCodeSuffixAndInstitution(String prefix,String suffix,Institution institution);

}
