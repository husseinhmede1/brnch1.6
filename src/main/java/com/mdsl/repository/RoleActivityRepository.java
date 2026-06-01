package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Activity;
import com.mdsl.model.entity.Role;
import com.mdsl.model.entity.RoleActivity;

@Repository
public interface RoleActivityRepository extends JpaRepository<RoleActivity, Integer>{
	
	List<RoleActivity> getByRole(Role role);
	
	
	
	List<RoleActivity> findAllByRole(Role role);
	

	
	void deleteByRole (Role role);


	@Query("from MD_ENT_ROLE_ACTIVITY where ROLE_ID = :roleId")
	List<RoleActivity> findAllByRoleId(int roleId); 
}
