package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import com.mdsl.model.objects.ObjectAndScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Api;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {

    Api getByApiCode(String apiCode);

    List<Api> getByApiUrl(String apiUrl);

    Api findByApiUrl(String apiUrl);

	@Query(value =
			"SELECT * FROM MD_BKD_API A" +
					"    WHERE REGEXP_LIKE(:apiUrl, A.API_URL)" +
					"      AND A.API_FUNCTION = :apiFunction" +
					"      AND A.INST_ID = :institution" +
					"      AND ROWNUM = 1"
			, nativeQuery = true)
	Optional<Api> findByApiUrlAndApiFunctionAndInstitution(
			@Param("apiUrl") String apiUrl,
			@Param("apiFunction") String apiFunction,
			@Param("institution") Integer institution
	);

	Page<Api> findByInstitutionAndAllowStpAndApiDescIgnoreCase (Integer institution, String allowStp, Pageable pageable, String object);

	Page<Api> findByInstitutionAndAllowStp (Integer institution, String allowStp, Pageable pageable);

	@Query(value = "Select * from MD_BKD_API where INST_ID =:instId and API_FUNCTION =:method and REGEXP_LIKE(:url, API_URL)", nativeQuery = true)
	Optional<Api> findApi (int instId, String method, String url);

	@Query(name = "find_objects_and_scope", nativeQuery = true)
	List<ObjectAndScope> findApiObjects (@Param("instId") int instId);

	Optional<Api> findByInstitutionAndApiId (Integer institution, int apiId);

	Optional<Api> findByInstitutionAndApiUrl (Integer institution, String apiUrl);

	@Query(value = "select DISTINCT * FROM "
			+ " MD_BKD_API AL"
			+ " WHERE AL.INST_ID = :institution AND AL.ALLOW_STP=1", nativeQuery = true)
	List<Api> findApiByInstitution(Integer institution);

	@Transactional
	@Modifying
	@Query("UPDATE MD_BKD_API SET STP = :stp, UPDATED_DATE = SYSDATE, UPDATED_BY =:userId WHERE API_ID = :apiId")
	void updateApiStpFlag(int apiId, String stp, int userId);

	@Transactional
	@Modifying
	@Query("DELETE FROM MD_BKD_API WHERE INST_ID = :institution")
	void deleteAllByInstitution(Integer institution);

	@Query(value = "Select * from MD_BKD_API where INST_ID =:instId and API_FUNCTION =:method and API_URL =:url", nativeQuery = true)
	Optional<Api> findApiByInstMethodAndUrl (int instId, String method, String url);

	@Query(value="SELECT API_LIST_ID FROM MD_BKD_API WHERE API_URL IN (select prop_value from md_cfg_global_props where prop_name = :propName and inst_id = :instId) and inst_id = :instId",nativeQuery=true)
	List<Integer> getApiIdsByPropNameAndInstitution(String propName, int instId);
}