package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Activity;
import com.mdsl.model.entity.ActivityApi;

@Repository
public interface ActivityApiRepository extends JpaRepository<ActivityApi, Integer>{

	List<ActivityApi> findByActivity_ActivityId(int id, Sort by);

	List<ActivityApi> findByActivity(Activity activity, Sort by);

}
