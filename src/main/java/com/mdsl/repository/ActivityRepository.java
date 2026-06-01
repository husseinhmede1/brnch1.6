package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Activity;
import com.mdsl.model.entity.Institution;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	Activity findByActivityCode(String activityCode);
	
//	Activity findByActivityCodeAndInstitution(String activityCode, Institution institution);

//	Optional<Activity> findByActivityIdAndInstitution (int activityId, Institution institution);
	
	List<Activity> getByParentActivity(Activity activity);

	Optional<Activity> findByActivityId(int activityId);

//	List<Activity> findByInstitutionAndModule(Institution institution, com.mdsl.model.entity.Module module);
}