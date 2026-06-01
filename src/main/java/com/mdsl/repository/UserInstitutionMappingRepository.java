package com.mdsl.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Institution;
import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserInstitutionMapping;

@Repository
public interface UserInstitutionMappingRepository extends JpaRepository<UserInstitutionMapping, Integer>{

	List<UserInstitutionMapping> findByUser(User user);

	UserInstitutionMapping findByUserAndInstitution(User user, Institution institution);

	List<UserInstitutionMapping> findByUser_UserIdAndInstitution_InstitutionId(int id, String instId);

	List<UserInstitutionMapping> findByInstitution_InstitutionId(String institutionId);

}
