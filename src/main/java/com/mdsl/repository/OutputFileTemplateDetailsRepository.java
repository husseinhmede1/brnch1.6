package com.mdsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.OutputFileTemplateDetails;

@Repository
public interface OutputFileTemplateDetailsRepository extends JpaRepository<OutputFileTemplateDetails, Integer> {
	List<OutputFileTemplateDetails> findByOutputTemplateHdrId(int outputTemplateHdrId);
	List<OutputFileTemplateDetails> findByOutputTemplateHdrIdAndFieldSection(int outputTemplateHdrId, String fieldSection);
	void deleteByOutputTemplateHdrId(int outputTemplateHdrId);
}
