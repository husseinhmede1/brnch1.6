package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.LayoutDetails;

@Repository 
public interface LayoutDetailsRepository extends JpaRepository<LayoutDetails, Integer>{
	Boolean existsByLayoutIdAndDetailsIdNotIn(Integer layoutId, List<Integer> detailsId);
	
	List<LayoutDetails> findByLayoutId(Integer layoutId);
	
	Optional<LayoutDetails> findByLayoutIdAndDetailsId(Integer layoutId, Integer detailsId);

	@Transactional
	@Modifying
	@Query("DELETE FROM MD_CFG_LAYOUT_DETAILS WHERE LAYOUT_ID = :layoutId")
	void deleteAllByLayoutId(Integer layoutId);

	List<LayoutDetails> findByLayoutIdIn(List<Integer> layoutIds);
}