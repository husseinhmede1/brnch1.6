package com.mdsl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.ActivityPackageTier;

@Repository
public interface ActivityPackageTierRepository extends JpaRepository<ActivityPackageTier, Integer>{
//	@Query("FROM MD_ACQ_ACTIVITY_PKG_TIER WHERE PKG_DETAIL_ID = :pkgdetailId")
//	List<ActivityPackageTier> findByPackageDetailId(int pkgdetailId);

	List<ActivityPackageTier> findByActivityPackageDetail_PackageDetailId(int pkgDetailId, Sort by);

	List<ActivityPackageTier> findByUserCreate(String string);
}
