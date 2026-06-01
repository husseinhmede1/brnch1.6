package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	List<UserRole> getByRole(Role role);
	
	List<UserRole> getByUser(User user);
	
	int countByRole(Role role);
	
	void deleteByRole(Role role);
	
	void deleteByUser(User user);
	
	Boolean existsByRole (Role role);
	
	List<UserRole> findAllByUser (User user);

	UserRole findByUserAndInstitution(User user, Institution institution);

	UserRole findByUser(User user);

	List<UserRole> findByCreatedBy(User user);

	List<UserRole> findByUser_UserIdAndInstitution_InstitutionId(int id, String institutionId);

	List<UserRole> findByInstitution_InstitutionId(String institutionId);
}
