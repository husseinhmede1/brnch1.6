package com.mdsl.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.AccountingTemplateHDRSub;

@Repository
public interface AccountingTemplateHDRSubRepository extends JpaRepository<AccountingTemplateHDRSub, Integer>{
	List<AccountingTemplateHDRSub> findByAcctTemplateHdrId(int acctTemplateHdrId);
	boolean existsByInstitutionIdAndBankCodeAndAcctTemplateHdrSubIdNot(String institutionId, String bankCode, int acctTemplateHdrSubId);

	boolean existsByInstitutionIdAndBankCode(String institutionId, String bankCode);
	
	boolean existsByInstitutionIdAndBankCodeAndAcctTemplateHdrId(String institutionId, String bankCode, Integer acctTemplateHdrId);
	
	@Transactional
	void deleteByAcctTemplateHdrId(int acctTemplateHdrId);
}
