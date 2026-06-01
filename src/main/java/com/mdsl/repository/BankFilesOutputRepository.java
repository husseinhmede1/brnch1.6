package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.BankFilesOutput;

@Repository
public interface BankFilesOutputRepository extends JpaRepository<BankFilesOutput, Integer> {
	List<BankFilesOutput> findByInstitutionId(String instId);
	
	@Query("SELECT DISTINCT bfo.bankCode FROM MD_BANK_FILES_OUTPUTS bfo WHERE bfo.institutionId = :instId")
    List<String> getDistinctBankCodesByInstitutionId(@Param("instId") String instId);
	
	@Query("SELECT DISTINCT bfo.outputFileType FROM MD_BANK_FILES_OUTPUTS bfo WHERE bfo.institutionId = :instId")
    List<String> getDistinctOutputFileTypesByInstitutionId(@Param("instId") String instId);
	
	@Query("SELECT DISTINCT bfo.outputFileTypeAbbr FROM MD_BANK_FILES_OUTPUTS bfo WHERE bfo.institutionId = :instId AND bfo.outputFileType LIKE CONCAT(:outputFileType, '%')")
    List<String> getOutputFileTypeAbbrByStartsWith(@Param("instId") String instId, @Param("outputFileType") String outputFileType);
	
	List<BankFilesOutput> findByOutputTemplateHdrId(int outputTemplateHdrId);
	Optional<BankFilesOutput> findByBankCodeAndOutputTemplateHdrId(String bankCode, int outputTemplateHdrId);
	//Optional<BankFilesOutput> findByOutputTemplateHdrId(int outputTemplateHdrId);
	void deleteByOutputTemplateHdrId(int outputTemplateHdrId);
	boolean existsByInstitutionIdAndBankCodeAndOutputFileTypeAndOutputFileTypeAbbr(String institutionId, String bankCode, String outputFileType, String outputFileTypeAbbr);
	boolean existsByInstitutionIdAndBankCodeAndOutputFileTypeAndOutputFileTypeAbbrAndBankFilesOutputIdNot(String institutionId, String bankCode, String outputFileType, String outputFileTypeAbbr, int bankFilesOutputId);
	boolean existsByBankCodeAndOutputTemplateHdrId(String bankCode, int outputTemplateHdrId);
}
