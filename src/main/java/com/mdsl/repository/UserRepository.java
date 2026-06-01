package com.mdsl.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserInstitutionMapping;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//	Boolean existsByUsername(String username);

	Boolean existsByUsernameIgnoreCase(String username);

	/*
	 * Boolean existsByBranch(Branch branch);
	 * 
	 * List<User> getByBranch(Branch branch);
	 * 
	 * List<User> findByStatus (char status);
	 * 
	 * List<User> findByBranch (Branch branch);
	 * 
	 * List<User> findByBranchAndStatus (Branch branch, char status);
	 * 
	 * Optional<User> findByUserIdAndBranch (int userId, Branch branch);
	 * 
	 * @Query("SELECT new com.mdsl.model.entity.User(userId, username, password) FROM MD_ENT_USER WHERE USERNAME = :username"
	 * ) User getUsernamePassword(String username);
	 */
	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET LAST_LOGIN_DATE = SYSDATE, PASSWORD_RETRIES = 0 WHERE USER_ID = :userId")
	int updateLastLoginDate(int userId);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET PASSWORD_RETRIES = PASSWORD_RETRIES + 1, UPDATED_DATE = SYSDATE WHERE USER_ID = :userId")
	int incrementPasswordRetries(int userId);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET PASSWORD = :password, PASSWORD_RETRIES = 0, LAST_PASSWORD_CHANGE = SYSDATE, UPDATED_DATE = SYSDATE WHERE USER_ID = :userId")
	int updatePassword(String password, int userId);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET PASSWORD = :password, STATUS = 1, PASSWORD_RETRIES = 0, LAST_PASSWORD_CHANGE = SYSDATE, UPDATED_DATE = SYSDATE WHERE USER_ID = :userId")
	int forceUpdatePassword(String password, int userId);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET STATUS = :status, UPDATED_DATE = :currentDate " +
			",UPDATED_BY = CASE WHEN :updatedBy IS NOT NULL THEN :updatedBy ELSE UPDATED_BY END " +
			"WHERE USER_ID = :userId")
	int updateUserStatus(char status, int userId, Timestamp currentDate,Integer updatedBy);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET STATUS = :status, UPDATED_DATE = :currentDate, PASSWORD_RETRIES = :passwordRetries, UPDATED_BY=:updatedBy WHERE USER_ID = :userId")
	int updateUserStatusAndPasswordRetries(char status, int userId, Timestamp currentDate, int passwordRetries,String updatedBy);

	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_USER SET PREFERED_LANGUAGE = :prefLang, UPDATED_DATE = SYSDATE WHERE USER_ID = :userId")
	int updatePrefLang(int userId, char prefLang);

//	@Query("from MD_ENT_USER  where lower(EMAIL)=:email and USER_ID!=:id")
	List<User> findByEmailEqualsIgnoreCaseAndUserIdNot(String email,int id);

	@Query("SELECT u FROM MD_ENT_USER u WHERE u.userId = :id ")
	Optional<User> findByIdAndNotAdmin(@Param("id") int id);

	@Query("from MD_ENT_USER  where LOWER(USERNAME)=:userName and USER_ID!=:id")
	List<User> findByUserNameAndUserId(String userName,int id);
	
	Optional<User> findByUsername(String username);

//	@Query("from MD_ENT_USER where INSTITUTION_ID = :instId")
//	List<User> findUserByInstitutionId(int instId, Sort by);


	@Query("SELECT uim FROM MD_USER_INST_MAPP uim WHERE uim.institution.id = :instId "+
		   " AND uim.user.userId NOT IN ( "+
		   "SELECT u.userId "+
           "FROM MD_ENT_USER u "+
           "WHERE u.username = :adminUsername) "+
		   "ORDER BY mappingId ASC")
	List<UserInstitutionMapping> findUserByInstitutionId(String instId,String adminUsername);

	Optional<User> findByEmailEqualsIgnoreCase(String email);

	@Query("from MD_ENT_USER where userId=:userId")
	Optional<User> findByUserId(Integer userId);


	//List<User> findByEmailIgnoreCaseAndUserId(String email, Integer userId);

	
	
}
