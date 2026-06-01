package com.mdsl.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByRoleId(int roleId);
	
	Optional<Role> findByRoleName(String roleName);
	
//	Optional<Role> findByRoleIdAndInstitution (int roleId, Institution institution);
	
//	List<Role> getByInstitution(Institution institution);
	
	List<Role> findByStatusOrderByRoleName (char status);
	
//	List<Role> findByStatusAndInstitutionOrderByRoleName (char status, Institution institution);
	
//	List<Role> findByInstitutionOrderByRoleName (Institution institution);
	
	//@Query("from MD_ACQ_ENTITY_PAYMENT_ACCOUNTS where INSTITUTION_ID = :instId")
//	List<Role> findByInstitution (Institution inst); 
	
//	boolean existsByRoleNameAndInstitution(String roleName, Institution institution);
	
	boolean existsByRoleId (int roleId); 
	
//	boolean existsByRoleIdAndInstitution (int roleId, Institution institution); 
	
	@Transactional
	@Modifying
	@Query("UPDATE MD_ENT_ROLE SET STATUS = :status, UPDATED_DATE = :currentDate, UPDATED_BY = :userId WHERE ROLE_ID = :roleId")
	int updateRoleStatus(int roleId, char status, Integer userId, Date currentDate);

	boolean existsByRoleName(String roleName);

	List<Role> findByCreatedBy(User user);

	List<Role> findByRoleNameIgnoreCaseAndRoleIdNot(String roleName, Integer roleId);
}