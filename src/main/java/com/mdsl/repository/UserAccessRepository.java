package com.mdsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.User;
import com.mdsl.model.entity.UserAccess;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Integer>{
	
	Boolean existsByUser(User user);
	
	UserAccess getByUser(User user);

	Optional<UserAccess> findByUser(User user);

	Optional<UserAccess> findByUserUserId(Integer userId);
}
